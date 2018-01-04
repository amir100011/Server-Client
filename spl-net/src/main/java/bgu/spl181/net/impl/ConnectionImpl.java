package bgu.spl181.net.impl;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.BlockingConnectionHandler;
import bgu.spl181.net.srv.bidi.ConnectionHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ConnectionImpl<T> implements Connections {

    private ConcurrentHashMap<Integer, ConnectionHandler> connectionsDataBase = new ConcurrentHashMap<>();//All clients
    private ConcurrentHashMap<Integer, Boolean> ConncectedIdConnections = new ConcurrentHashMap<>();//logged in connectionId
    private ConcurrentSkipListSet<String> loggedInClients = new ConcurrentSkipListSet<>();//loggedIn Clients by userName And PassWord
    private ConcurrentHashMap<String, String> UserNameAndPassword = new ConcurrentHashMap<>();//All existing users and their passwords

    @Override
    public boolean send(int connectionId, Object msg) {

        if (!this.connectionsDataBase.containsKey(connectionId))
            return false;
        else {//exists
            this.connectionsDataBase.get(connectionId).send(msg);
            return true;
        }//end of else
    }

    @Override
    public void broadcast(Object msg) {

        Collection<ConnectionHandler> ActiveClients = this.connectionsDataBase.values();
        for (ConnectionHandler activeClient : ActiveClients) {
            activeClient.send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {

        boolean exists = this.connectionsDataBase.contains(connectionId);
        boolean connected = this.ConncectedIdConnections.contains(connectionId);

        if (exists && connected) {
            ConnectionHandler disconnect = this.connectionsDataBase.get(connectionId);
            try {
                disconnect.close();
                ConncectedIdConnections.remove(connectionId);
                connectionsDataBase.remove(connectionId);
            } catch (IOException ignored) {
                ignored.printStackTrace();//TODO delete before submitting
            }

        }//end of if
    }

    public boolean AddConnection(int connectionId, ConnectionHandler handler) {
        if (!this.connectionsDataBase.contains(connectionId)) {
            this.connectionsDataBase.put(connectionId, handler);
            this.ConncectedIdConnections.put(connectionId, false);
        }
        return this.connectionsDataBase.contains(connectionId);//true - added/exists false not exists
    }

    public ConcurrentHashMap getconnectionsDataBase() {
        return this.connectionsDataBase;
    }

    public ConcurrentHashMap getConncectedIdConnections() {
        return this.ConncectedIdConnections;
    }

    public ConcurrentSkipListSet<String> getLoggedInClients() {
        return this.loggedInClients;
    }

    public ConcurrentHashMap getUserNameAndPassword() {
        return this.UserNameAndPassword;
    }

    public boolean CheckifLoggedIn(int Connectionid) {
        return this.ConncectedIdConnections.get(Connectionid);
    }

    public boolean RegisterCondCheck(LinkedList<String> Msg, Integer connectionId) {

        if (Msg.size() < 3)//Missing Info(3)
            return false;

        else {
            String UserName = Msg.get(1);
            String Password = Msg.get(2);

            if (this.CheckifLoggedIn(connectionId))
                return false;
            if (this.UserNameAndPassword.containsKey(UserName))//UserName Taken(2)
                return false;

            else {//passed all tests
                this.UserNameAndPassword.put(UserName, Password);//new userName and Password
                this.ConncectedIdConnections.put(connectionId, false);
                return true;
            }//end of else
        }//end of else
    }//end of function

    public boolean LoginCondCheck(LinkedList<String> Msg, Integer connectionId) {

        String UserName = Msg.get(1);
        String Password = Msg.get(2);


        if (this.CheckifLoggedIn(connectionId))//if this client is already logged in
            return false;
        if (!this.UserNameAndPassword.containsKey(UserName))
            return false;//no UserName with that name exists
        if (!this.UserNameAndPassword.get(UserName).equals(Password))//UserName and Password do not match(3)
            return false;
        if (this.loggedInClients.contains(UserName))//UserName already logged in(2)
            return false;
        else {//passed all tests
            this.loggedInClients.add(UserName);//added the user to logged in users
            this.ConncectedIdConnections.replace(connectionId, true);
            return true;
        }//end of else
    }//end of function


    public boolean SignoutCondCheck(LinkedList<String> Msg, Integer connectionId) {

        String UserName = Msg.get(1);
        String Password = Msg.get(2);


        if(this.ConncectedIdConnections.contains(connectionId))//Client not logged in (1)
            return false;
        else{
            return true;
        }
    }//end of function
}
