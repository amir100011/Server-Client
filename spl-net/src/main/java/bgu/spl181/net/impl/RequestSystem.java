package bgu.spl181.net.impl;

import bgu.spl181.net.impl.generalImpls.BidiConnectionImple;
import bgu.spl181.net.impl.generalImpls.connectionImpl;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

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

/*
        switch (reqKind) {

            case "balance":
                reqKind = this.Msg.get(1);
                String ACK;
                AtomicInteger balance = new AtomicInteger(0);
                String UserName = ((String)this.connections.getLoggedInClients().get(connectionId));
                if (reqKind.equals("info")) {
                    balance.set(((UserInfo)this.connections.getUserInfo().get(UserName)).getBalance().get());
                    ACK = "ACK " + balance.toString();
                }//end of ("info") if
                else{//"add"
                    balance = ((UserInfo)this.connections.getUserInfo().get(UserName)).getBalance();
                    Integer Amount = Integer.parseInt(Msg.get(2));
                    int newBalance = balance.get() + Amount;
                    ((UserInfo)this.connections.getUserInfo().get(UserName)).setBalance(newBalance);
                    ACK ="ACK " + newBalance + " add " + Amount.toString();
                }//end of "add" (else)

                this.connections.send(this.connectionId,ACK);


                break;
        }//end of switch-case
        */
    }//end of function
}
