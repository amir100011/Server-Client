package bgu.spl181.net.impl.Users;

import bgu.spl181.net.impl.Blockbuster.singleMovieInfo;
import bgu.spl181.net.impl.generalImpls.connectionImpl;

import java.util.ArrayList;
import java.util.LinkedList;

public class adminUserRequestSystem {
    private LinkedList<String> Msg;
    private connectionImpl connections;
    private int connectionId;

    public adminUserRequestSystem(LinkedList<String> Msg, connectionImpl connection, int connectionID) {
        this.Msg = Msg;
        this.connectionId = connectionID;
        this.connections = connection;
    }

    public void adminRequestSystem(){

        String reqKind = this.Msg.get(0);
        String ACK = "";
        switch (reqKind){


            case "addmovie":{
                String userName = (String)this.connections.getLoggedInClients().get(this.connectionId);
                boolean Admin = this.connections.getUserDataBase().GetUser(userName).getIsAdmin();
                if(!Admin || Msg.size() < 4 )
                    this.connections.send(this.connectionId, "ERROR request addmovie failed");
                ///The addmovie command contains "movie name" ,amount and price
                String movieName = this.Msg.get(1);
                String copies = this.Msg.get(2);
                String MoviePrice = this.Msg.get(3);
                Integer price = null;
                Integer Copies = null;
                try{
                    price = Integer.parseInt(MoviePrice);
                    Copies = Integer.parseInt(copies);
                }catch(NumberFormatException e){ }
                final Integer Price = price;
                final Integer _Copies = Copies;
                if(price == null || price <= 0 || Copies <= 0) {
                    this.connections.send(this.connectionId, "ERROR request addmovie failed");
                    break;
                }
                ////Check For Banned Movies
                ArrayList<String> bannedCountry = new ArrayList<>();
                if(Msg.size() > 4){//Has BannedMovies
                    for(int i = 4 ; i < Msg.size(); i++)
                        bannedCountry.add(Msg.get(i));
                }

                //Passed all test
                this.connections.getMovieDataBase().BlockMovies(()-> {
                            boolean hasmovie = this.connections.getMovieDataBase().hasMovie(movieName);
                            if (hasmovie)
                                this.connections.send(this.connectionId, "ERROR request addmovie failed");
                            else {//ALL Test Have Passed Need to Add Movie
                                int id = this.connections.getMovieDataBase().getNewMovieId();
                                singleMovieInfo newmovie = new singleMovieInfo(id, movieName, _Copies, Price , bannedCountry);
                                this.connections.getMovieDataBase().addMovie(newmovie);
                                this.connections.send(this.connectionId, "ACK addmovie "+"\""+newmovie.getName() +"\"" + " success");
                                this.connections.broadcast("movie "+parametersConcatForMovie(newmovie));
                            }
                        }
                );

                break;
            }
            case "remmovie":{


                String userName = (String)this.connections.getLoggedInClients().get(this.connectionId);
                boolean Admin = this.connections.getUserDataBase().GetUser(userName).getIsAdmin();
                if (Admin){
                    if(Msg.size()> 0) {
                        this.connections.getMovieDataBase().BlockMovies(()-> {
                            singleMovieInfo movieToRemmove = this.connections.getMovieDataBase().getSpecificMovie(Msg.get(1));
                            if (movieToRemmove != null) {
                                boolean isRented = !(movieToRemmove.getAvailableAmount() == movieToRemmove.getTotalAmount());
                                if (!isRented) {//movie is not rented
                                    this.connections.getMovieDataBase().remMovie(movieToRemmove);
                                    String ACK1 = "ACK remmovie \"" + movieToRemmove.getName() + "\" success";
                                    String ACK2 = "ACK \"" + movieToRemmove.getName() + "\" removed";
                                    this.connections.send(this.connectionId,ACK1);
                                    this.connections.broadcast(ACK2);
                                    }else
                                    this.connections.send(this.connectionId,"ERROR request remmovie failed");
                                }else
                                this.connections.send(this.connectionId,"ERROR request remmovie failed");
                            }
                        );
                    }else
                        this.connections.send(this.connectionId,"ERROR request remmovie failed");
                }else
                    this.connections.send(this.connectionId,"ERROR request remmovie failed");

                break;
            }
            case "changeprice":{
                String userName = (String)this.connections.getLoggedInClients().get(this.connectionId);
                boolean Admin = this.connections.getUserDataBase().GetUser(userName).getIsAdmin();
                if(!Admin || Msg.size() < 3 )
                    this.connections.send(this.connectionId, "ERROR request changeprice failed");
                String movieName = this.Msg.get(1);
                String MoviePrice = this.Msg.get(2);
                Integer price = null;
                try{
                    price = Integer.parseInt(MoviePrice);
                }catch(NumberFormatException e){ }
                final Integer Price = price;
                if(price == null || price <= 0)
                    this.connections.send(this.connectionId, "ERROR request changeprice failed");
                this.connections.getMovieDataBase().BlockMovies(()-> {
                    boolean hasmovie = this.connections.getMovieDataBase().hasMovie(movieName);
                    if(!hasmovie)
                        this.connections.send(this.connectionId, "ERROR request changeprice failed");
                    else{
                        this.connections.getMovieDataBase().changePrice(Price,movieName);//Changed Price
                        this.connections.send(this.connectionId, "ACK changeprice "+"\""+movieName+"\""+ " success");
                        this.connections.broadcast("movie" + parametersConcatForMovie(this.connections.getMovieDataBase().getSpecificMovie(movieName)));
                    }
                });
            }



        }//end of switch case

    }


    private String parametersConcatForMovie(singleMovieInfo tmp) {
        String toReturn = "";
        toReturn = toReturn.concat("\""+tmp.getName()+"\"");
        toReturn = toReturn.concat(" " + tmp.getAvailableAmount());
        toReturn = toReturn.concat(" " + tmp.getPrice());
        return toReturn;
    }
}
