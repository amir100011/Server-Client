package bgu.spl181.net.impl;

import bgu.spl181.net.impl.Blockbuster.singleMovieInfo;
import bgu.spl181.net.impl.generalImpls.BidiConnectionImple;
import bgu.spl181.net.impl.generalImpls.connectionImpl;

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
                    String movieName = reMakeMovieName();
                    boolean Good2GO = this.connections.getMovieDataBase().hasMovie(movieName);
                    if (Good2GO) {
                        singleMovieInfo tmp = this.connections.getMovieDataBase().getSpecificMovie(Msg.get(1));
                        ACK = "ACK " + parametersConcat(tmp);
                    }//end of if
                    else//movie doesn't exists
                        this.connections.send(this.connectionId, "Error request info failed");
                } else
                    ACK = "ACK " + this.connections.getMovieDataBase().getAllMoviesNames();

                this.connections.send(this.connectionId, ACK);
            }
        }//end of switch-case

    }//end of function

    private String parametersConcat(singleMovieInfo tmp) {
        String toReturn = "";
        toReturn = toReturn.concat(tmp.getName());
        toReturn = toReturn.concat(" " + tmp.getAvailableAmount());
        toReturn = toReturn.concat(" " + tmp.getTotalAmount());
        toReturn = toReturn.concat(" " + tmp.getPrice());
        for (String banned : tmp.getBannedCountries()) {
            toReturn = toReturn.concat(" " + banned);
        }
        return toReturn;
    }

    private String reMakeMovieName() {
        String toReturn = "";
        for (int index = 1; index < Msg.size(); index++)
            toReturn = toReturn.concat(" " + Msg.get(index));


        toReturn = toReturn.substring(1);//removing the first space which is not needed
        return toReturn;

    }
}
