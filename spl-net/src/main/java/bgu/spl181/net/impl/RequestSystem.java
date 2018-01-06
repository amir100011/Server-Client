package bgu.spl181.net.impl;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestSystem extends BidiConnectionImpl {

    private LinkedList<String> Msg;
    private ConnectionImpl connections;
    private int connectionId;

    public RequestSystem(LinkedList<String> Msg, ConnectionImpl connection, int connectionID){
        this.Msg = Msg;
        this.connectionId = connectionID;
        this.connections = connection;
    }

    public void requestSystem() {


        String reqKind = this.Msg.get(0);
        String ACK;
        String Error;

        switch (reqKind) {

            case "balance": {
                reqKind = this.Msg.get(1);
                AtomicInteger balance = new AtomicInteger(0);
                String UserName = ((String) this.connections.getLoggedInClients().get(connectionId));
                if (reqKind.equals("info")) {
                    balance.set(((UserInfo) this.connections.getUserInfo().get(UserName)).getBalance().get());
                    ACK = "ACK " + balance.toString();
                }//end of ("info") if
                else {//"add"
                    balance = ((UserInfo) this.connections.getUserInfo().get(UserName)).getBalance();
                    Integer Amount = Integer.parseInt(Msg.get(2));
                    int newBalance = balance.get() + Amount;
                    ((UserInfo) this.connections.getUserInfo().get(UserName)).setBalance(newBalance);
                    ACK = "ACK " + newBalance + " add " + Amount.toString();
                }//end of "add" (else)

                this.connections.send(this.connectionId, ACK);

                break;
            }//end of case balance
            case "info":{

                String movieName= "";

                for (int index = 1; index < Msg.size(); index++)//movie name
                    movieName =movieName + (Msg.get(index)) + " ";
                movieName.substring(movieName.length()-1);//delete last space

                BlockbusterInfo Movies = this.connections.getMovieInfo();

                if(Movies.getTotalCopies().containsKey(movieName)){//movie exists
                    ACK = "ACK " + movieName + " " + Movies.getCopiesLeft().get(movieName).toString();
                    ACK.concat(" " +Movies.getPrices().get(movieName).toString());
                    ACK.concat(" " +Movies.getBannedCountries().get(movieName).toString());
                    ACK.concat(" " +Movies.getTotalCopies().get(movieName).toString());
                    this.connections.send(this.connectionId, ACK);
                }

                else {//movie doesn't exists
                    Error = "Error not such movie exists!";
                    this.connections.send(this.connectionId, Error);
                }//end of else

            }//end of case info
        }//end of switch-case
    }//end of function
}
