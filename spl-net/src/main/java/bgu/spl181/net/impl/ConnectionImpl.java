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
    private ConcurrentHashMap<String, UserInfo> UserInfo = new ConcurrentHashMap<>();//logged in connectionId
    private ConcurrentHashMap<Integer, String> loggedInClients = new ConcurrentHashMap<>();//loggedIn Clients by userName And PassWord
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
    public void disconnect(int connectionId,String UserName) {

        boolean exists = this.connectionsDataBase.containsKey(connectionId);

        if (exists) {
            ConnectionHandler disconnect = this.connectionsDataBase.get(connectionId);
            try {
                disconnect.close();
                connectionsDataBase.remove(connectionId);
                loggedInClients.remove(connectionId,UserName);
            } catch (IOException ignored) {
                ignored.printStackTrace();//TODO delete before submitting
            }

        }//end of if
    }

    public boolean AddConnection(int connectionId, ConnectionHandler handler) {
        if (!this.connectionsDataBase.contains(connectionId)) {
            this.connectionsDataBase.put(connectionId, handler);
        }
        return this.connectionsDataBase.contains(connectionId);//true - added/exists false not exists
    }

    public ConcurrentHashMap getconnectionsDataBase() {
        return this.connectionsDataBase;
    }

    public ConcurrentHashMap getUserInfo() {
        return this.UserInfo;
    }

    public ConcurrentHashMap getLoggedInClients() {
        return this.loggedInClients;
    }

    public ConcurrentHashMap getUserNameAndPassword() {
        return this.UserNameAndPassword;
    }


    public boolean RegisterCondCheck(LinkedList<String> Msg, Integer connectionId) {

        if (Msg.size() < 3)//Missing Info(3)
            return false;

        else {
            String UserName = Msg.get(1);
            String Password = Msg.get(2);

            if (this.loggedInClients.containsKey(connectionId))
                return false;
            if (this.UserNameAndPassword.containsKey(UserName))//UserName Taken(2)
                return false;

            else {//passed all tests

                if(Msg.size() > 2) { //Adding user info if he has //TODO check if it can have less parameters than needed
                    List<String> moviesList = new LinkedList<>();
                    for (int index = 6; index < Msg.size(); index++)
                        moviesList.add(Msg.get(index));
                    boolean type = Msg.get(3).equals("Admin");
                    String countery = Msg.get(4);
                    Integer balance = Integer.parseInt(Msg.get(5));
                    UserInfo user =  new UserInfo(type, countery, balance, moviesList);
                    this.UserInfo.put(UserName,user);
                }//end of Addition


                this.UserNameAndPassword.put(UserName, Password);//new userName and Password
                return true;
            }//end of else
        }//end of else
    }//end of function

    public boolean LoginCondCheck(LinkedList<String> Msg, Integer connectionId) {

        String UserName = Msg.get(1);
        String Password = Msg.get(2);


        if (this.loggedInClients.containsKey(connectionId))//if this client is already logged in
            return false;
        if (!this.UserNameAndPassword.containsKey(UserName))
            return false;//no UserName with that name exists
        if (!this.UserNameAndPassword.get(UserName).equals(Password))//UserName and Password do not match(3)
            return false;
        if (this.loggedInClients.containsValue(UserName))//UserName already logged in(2)
            return false;
        else {//passed all tests
            this.loggedInClients.put(connectionId,UserName);//added the user to logged in users
            return true;
        }//end of else
    }//end of function


    public boolean SignoutCondCheck(Integer connectionId) {

            return this.loggedInClients.get(connectionId) != null;

    }//end of function
}
