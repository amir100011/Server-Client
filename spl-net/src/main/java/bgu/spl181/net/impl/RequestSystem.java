package bgu.spl181.net.impl;

import bgu.spl181.net.impl.Blockbuster.singleMovieInfo;
import bgu.spl181.net.impl.Json.JsonMovie;
import bgu.spl181.net.impl.Users.RentedMovie;
import bgu.spl181.net.impl.generalImpls.BidiConnectionImple;
import bgu.spl181.net.impl.generalImpls.connectionImpl;

import java.util.ArrayList;
import java.util.LinkedList;

public class RequestSystem extends BidiConnectionImple {

    private LinkedList<String> Msg;
    private connectionImpl connections;
    private int connectionId;

    public RequestSystem(LinkedList<String> Msg, connectionImpl connection, int connectionID) {
        this.Msg = Msg;
        this.connectionId = connectionID;
        this.connections = connection;
    }

    public void requestSystem() {


        String reqKind = this.Msg.get(0);
        String ACK = "";

        switch (reqKind) {

            case "balance": {
                reqKind = this.Msg.get(1);
                int balance;
                String userName = ((String) this.connections.getLoggedInClients().get(connectionId));
                if (reqKind.equals("info")) {
                    balance = this.connections.getUserDataBase().GetUser(userName).getBalance();
                    ACK = "ACK " + balance;
                }//end of ("info") if
                else {//"add"
                    Integer Amount = Integer.parseInt(Msg.get(2));
                    this.connections.getUserDataBase().addToBalance(userName, Amount);
                    balance = this.connections.getUserDataBase().GetUser(userName).getBalance();
                    ACK = "ACK " + balance + " add " + Amount.toString();
                }//end of "add" (else)

                this.connections.send(this.connectionId, ACK);

                break;
            }
            case "info": {
                if (Msg.size() > 1) {//we have movie name
                    String movieName = Msg.get(1);
                    boolean Good2GO = this.connections.getMovieDataBase().hasMovie(movieName);
                    if (Good2GO) {
                        singleMovieInfo tmp = this.connections.getMovieDataBase().getSpecificMovie(Msg.get(1));
                        ACK = "ACK " + parametersConcat(tmp);
                        this.connections.send(this.connectionId, ACK);

                    }//end of if
                    else//movie doesn't exist
                        this.connections.send(this.connectionId, "Error request info failed");
                } else {
                    ACK = "ACK " + this.connections.getMovieDataBase().getAllMoviesNames();
                    this.connections.send(this.connectionId, ACK);
                }

                    break;
            }
            case "return":{
                if (Msg.size() > 1){

                    String movieName = Msg.get(1);
                    final String ACK1 = "ACK " + movieName + " success";
                    String userName = (String) this.connections.getLoggedInClients().get(connectionId);
                    boolean Good2Go =  this.connections.getUserDataBase().GetUser(userName).alreadyRented(movieName);

                    if(Good2Go){

                        this.connections.getMovieDataBase().BlockMovies(()-> {
                            if(this.connections.getMovieDataBase().hasMovie(movieName)) {
                                this.connections.getMovieDataBase().addCopy(movieName);
                                this.connections.getUserDataBase().remMovie(movieName,userName);
                                this.connections.send(this.connectionId,ACK1);
                                String broadcast = "movie \" "+ movieName +"\"";
                                broadcast = broadcast.concat(" " + this.connections.getMovieDataBase().getSpecificMovie(movieName).getAvailableAmount());
                                broadcast = broadcast.concat(" " + this.connections.getMovieDataBase().getSpecificMovie(movieName).getPrice());
                                this.connections.broadcast(broadcast);
                            } else //movie doesn't exist
                                this.connections.send(this.connectionId,"Error request return failed");
                        });
                    }
                    else //movie doesn't exist
                        this.connections.send(this.connectionId,"Error request return failed");

                }else //no movie name provided
                    this.connections.send(this.connectionId,"Error request return failed");

                break;
            }

            case "rent":{
                if (Msg.size() > 1) {//we have movie name
                    String movieName = Msg.get(1);
                    String userName = ((String) this.connections.getLoggedInClients().get(connectionId));
                    boolean alreadyRented = this.connections.getUserDataBase().GetUser(userName).alreadyRented(movieName);
                    String userCountry = this.connections.getUserDataBase().GetUser(userName).getCountry();
                    boolean Banned =this.connections.getMovieDataBase().getSpecificMovie(movieName).isBanned(userCountry);
                    if(alreadyRented || Banned){
                        this.connections.send(this.connectionId, "ERROR request rent failed");
                        return;
                    }
                    //if we are here the user is not on the banned list and doesn't have the movie
                    this.connections.getMovieDataBase().BlockMovies(()->{
                                JsonMovie movieDataBase = this.connections.getMovieDataBase();
                                //Check if movies exist and has copies left
                                boolean Good2Go = this.connections.getMovieDataBase().hasAviliableCopiesLeft(movieName);
                                ///if the user has sufficient funds
                                int moviePrice = this.connections.getMovieDataBase().getSpecificMovie(movieName).getPrice();
                                boolean hasSufficientfunds = this.connections.getUserDataBase().GetUser(userName).getBalance() >= moviePrice ;
                                Good2Go = Good2Go && hasSufficientfunds;
                                ////If Good2Go is still true then all test have passed thus we need to actually rent the movie
                                ////first We need to remove the movie from the movie list(decrement it) and then update the movie json
                                ////then we add the movie to the user
                                if(Good2Go) {
                                    RentedMovie rentedMovie = this.connections.getMovieDataBase().RentMovie(movieName);
                                    this.connections.getUserDataBase().addMovie(userName,rentedMovie,moviePrice);
                                    this.connections.send(this.connectionId, "ACK rent"+"\""+movieName +"\""+  "success");

                                    this.connections.broadcast("movie" +parametersConcat(movieDataBase.getSpecificMovie(movieName)));

                                }else
                                    this.connections.send(this.connectionId, "ERROR request rent failed");
                            }
                    );


                }

                break;
            }

            case "addmovie":{
                String userName = (String)this.connections.getLoggedInClients().get(this.connectionId);
                boolean Admin = this.connections.getUserDataBase().GetUser(userName).getIsAdmin();
                if(!Admin || Msg.size() < 5 )
                    this.connections.send(this.connectionId, "ERROR request addmovie");
                ///The addmovie command contains "movie name" ,amount and price
                String movieName = this.Msg.get(2);
                String copies = this.Msg.get(3);
                String MoviePrice = this.Msg.get(4);
                Integer price = null;
                Integer Copies = null;
                try{
                    price = Integer.parseInt(MoviePrice);
                    Copies = Integer.parseInt(copies);
                }catch(NumberFormatException e){ }
                final Integer Price = price;
                final Integer _Copies = Copies;
                if(price == null || price <= 0)
                    this.connections.send(this.connectionId, "ERROR request addmovie");
                ////Check For Banned Movies
                ArrayList<String> bannedCountry = new ArrayList<>();
                if(Msg.size() > 5){//Has BannedMovies
                    for(int i = 5 ; i < Msg.size(); i++)
                        bannedCountry.add(Msg.get(i));
                }

                //Passed all test
                this.connections.getMovieDataBase().BlockMovies(()-> {
                            boolean hasmovie = this.connections.getMovieDataBase().hasMovie(movieName);
                            if (hasmovie)
                                this.connections.send(this.connectionId, "ERROR request addmovie");
                            else {//ALL Test Have Passed Need to Add Movie
                                int id = this.connections.getMovieDataBase().getNewMovieId();
                                singleMovieInfo newmovie = new singleMovieInfo(id, movieName, _Copies, Price , bannedCountry);
                                this.connections.getMovieDataBase().addMovie(newmovie);
                                this.connections.send(this.connectionId, "ACK addmovie "+"\""+newmovie.getName() +"\"" + "success");
                                this.connections.broadcast("movie"+parametersConcatForMovie(newmovie));
                            }
                        }
                );








            }


        }//end of switch-case

    }//end of function

    private String parametersConcat(singleMovieInfo tmp) {
        String toReturn = "";
        toReturn = toReturn.concat("\"" + tmp.getName() +"\"");
        toReturn = toReturn.concat(" " + tmp.getAvailableAmount());
        toReturn = toReturn.concat(" " + tmp.getTotalAmount());
        toReturn = toReturn.concat(" " + tmp.getPrice());
        for (String banned : tmp.getBannedCountries()) {
            toReturn = toReturn.concat(" \"" + banned + "\"");
        }
        return toReturn;
    }

    private String parametersConcatForMovie(singleMovieInfo tmp) {
        String toReturn = "";
        toReturn = toReturn.concat("\""+tmp.getName()+"\"");
        toReturn = toReturn.concat(" " + tmp.getAvailableAmount());
        toReturn = toReturn.concat(" " + tmp.getPrice());
        return toReturn;
    }
}
