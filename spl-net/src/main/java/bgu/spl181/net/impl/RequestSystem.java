package bgu.spl181.net.impl;

import bgu.spl181.net.impl.generalImpls.BidiConnectionImple;
import bgu.spl181.net.impl.generalImpls.connectionImpl;

import java.util.LinkedList;

public class RequestSystem extends BidiConnectionImple {

    private LinkedList<String> Msg;
    private connectionImpl connections;
    private int connectionId;

    public RequestSystem(LinkedList<String> Msg, connectionImpl connection, int connectionID){
        this.Msg = Msg;
        this.connectionId = connectionID;
        this.connections = connection;
    }

    public void requestSystem() {


        String reqKind = this.Msg.get(0);


        switch (reqKind) {

            case "balance": {
                reqKind = this.Msg.get(1);
                String ACK;
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
            case "info":{
                if (Msg.size()>1) {//we have movie name
                    boolean Good2GO = this.connections.getMovieDataBase().hasMovie();
                }
            }
        }//end of switch-case

    }//end of function
}
