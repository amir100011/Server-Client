package bgu.spl181.net.impl.generalImpls;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.impl.Users.normalUserRequestSystem;
import bgu.spl181.net.impl.Users.UserInfo;
import bgu.spl181.net.srv.bidi.ConnectionHandler;

import java.util.LinkedList;

public class BidiConnectionImple implements BidiMessagingProtocol {

    protected connectionImpl connections = null;
    protected int connectionId = -1;


    public static LinkedList<String> BreakIntoWords(String msg) {

        LinkedList<String> Msg = new LinkedList<>();

        if(msg.startsWith("Register")) {
           msg =  msg.replace("country=\"", "\"country=");
        }

        while (msg.length() > 0) {

            if(msg.startsWith("\"")){
                msg = msg.substring(1,msg.length());
                Msg.add(msg.substring(0,msg.indexOf("\"")));
                msg = msg.substring(msg.indexOf("\"") + 1);
                while(msg.startsWith(" "))
                    msg = msg.substring(1);
            }
            else {
                try {
                    Msg.add(msg.substring(0, msg.indexOf(" ")));
                    msg = msg.substring(msg.indexOf(" ") + 1);
                } catch (StringIndexOutOfBoundsException n){
                    Msg.add(msg);
                    msg = "";
                }
            }
        }
        if(!msg.equals(""))
            Msg.add(msg);

        return Msg;

    }

    @Override
    public void start(int connectionId, connectionImpl connections, ConnectionHandler handler) {
        this.connections = connections;
        this.connections.AddConnection(connectionId, handler);
        this.connectionId = connectionId;
    }

    @Override
    public void process(Object message) {

        String msg = message.toString();
        if (msg.length() > 0) {
            LinkedList<String> Msg = BreakIntoWords(msg);
            String msgStr = Msg.getFirst();//the first word in the message


            switch (msgStr) {
                case "Register": {
                    String Error = "Error registration failed";
                    String ACK = "ACK registration succeeded";

                    if (Msg.size() > 3) {

                        String userName = Msg.get(1);
                        String password = Msg.get(2);

                        boolean Good2Go = !this.connections.getUserDataBase().HasUser(userName);//if not exist returns false
                        Good2Go = Good2Go && !this.connections.isLoggedIn(connectionId);

                        String Country = Msg.get(3);//country="Country" <---need the "    "  statement only
                        Good2Go = Good2Go && Country.contains("country=");//if Register Command holds "country=" as instructed
                        if (Good2Go) {
                            Country = Country.substring(Country.indexOf("=") + 2, Country.length() - 1);
                            UserInfo newUser = new UserInfo(userName, password, Country);
                            this.connections.getUserDataBase().AddUser(newUser);
                            this.connections.send(this.connectionId, ACK);
                            break;
                        }
                    }
                    this.connections.send(this.connectionId, Error);
                    break;

                }

                case "Login": {
                    String Error = "Error login failed";
                    String ACK = "ACK login succeeded";

                    if (Msg.size() > 2) {

                        String userName = Msg.get(1);
                        String password = Msg.get(2);

                        boolean Good2Go = this.connections.getUserDataBase().HasUser(userName);//if not exist returns false
                        Good2Go = Good2Go && !this.connections.isLoggedIn(connectionId, userName);//the username or the client already logged in
                        Good2Go = Good2Go && this.connections.getUserDataBase().matchedUserPassword(userName, password);//wrong password
                        if (Good2Go) {
                            this.connections.getLoggedInClients().put(this.connectionId, userName);
                            this.connections.send(this.connectionId, ACK);
                            break;
                        }
                    }
                    this.connections.send(this.connectionId, Error);
                    break;
                }
                case "SIGNOUT": {

                    String Error = "Error Signout failed";
                    String ACK = "ACK Signout succeeded";

                    boolean Good2Go = this.connections.isLoggedIn(this.connectionId);//if not exist returns false
                    if (Good2Go) {
                        this.connections.send(this.connectionId, ACK);
                        this.connections.getLoggedInClients().remove(this.connectionId);
                        this.connections.getClientsDataBase().remove(this.connectionId);
                        break;
                    }
                    this.connections.send(this.connectionId, Error);
                    break;
                }

                case "REQUEST": {

                    boolean Good2Go = this.connections.isLoggedIn(this.connectionId);//if not exist returns false
                    if (Good2Go) {
                        Msg.removeFirst();//remove the Request word
                        normalUserRequestSystem tmp = new normalUserRequestSystem(Msg, this.connections, this.connectionId);
                        tmp.normalRequestSystem();
                        break;
                    }
                }
                default:
                    break;
            }
        }
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}