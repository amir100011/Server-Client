package bgu.spl181.net.impl;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.srv.bidi.ConnectionHandler;
import java.util.LinkedList;

public class BidiConnectionImpl implements BidiMessagingProtocol{

    private ConnectionImpl connections = null;
    private int connectionId = -1;


    public static LinkedList<String> BreakIntoWords(String msg){

        LinkedList<String> Msg = new LinkedList<>();

        while (msg.indexOf(" ") != -1){

            Msg.add(msg.substring(0,msg.indexOf(" ")));
            msg = msg.substring(msg.indexOf(" ")+1);
        }
        Msg.add(msg);

        return Msg;

    }

    @Override
    public void start(int connectionId, ConnectionImpl connections, ConnectionHandler handler) {
        this.connections = connections;
        this.connections.AddConnection(connectionId, handler);
        this.connectionId = connectionId;
    }

    @Override
    public void process(Object message) {


       String msg = message.toString();
       LinkedList<String> Msg = BreakIntoWords(msg);
       String msgStr = Msg.getFirst();//the first word in the message



        switch(msgStr) {
           case "Register":
           {
               String Error = "Error registration failed";
               String ACK = "ACK registration succeeded";

               boolean Good2Go = this.connections.RegisterCondCheck(Msg, this.connectionId);

               if (Good2Go)
                   this.connections.send(this.connectionId, ACK);
               else
                   this.connections.send(this.connectionId, Error);

               break;
           }
           case "Login":{

               String Error = "Error login failed";
               String ACK = "ACK login succeeded";

               boolean Good2Go = this.connections.LoginCondCheck(Msg, this.connectionId);

               if (Good2Go)
                   this.connections.send(this.connectionId, ACK);
               else
                   this.connections.send(this.connectionId, Error);


               break;
               }

            case "SIGNOUT":
            {
                String Error = "Error signout failed";
                String ACK = "ACK signout succeeded";


                boolean Good2Go = this.connections.SignoutCondCheck(Msg, this.connectionId);

                if (Good2Go) {
                    this.connections.send(this.connectionId, ACK);
                    this.connections.disconnect(this.connectionId);
                }//end of if
                else
                    this.connections.send(this.connectionId, Error);
            }

            case "REQUEST":
            {
                requestSystem(Msg);

            }
           default: break;

       }
    }

    public void requestSystem(LinkedList<String> message) {

    }

    /**
     * @return true if the connection should be terminated
     */
    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
