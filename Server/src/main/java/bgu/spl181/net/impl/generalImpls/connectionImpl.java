package bgu.spl181.net.impl.generalImpls;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.impl.Json.JsonMovie;
import bgu.spl181.net.impl.Json.JsonUser;
import bgu.spl181.net.srv.bidi.ConnectionHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class connectionImpl<T> implements Connections {

    private ConcurrentHashMap<Integer, ConnectionHandler> clientsDataBase = new ConcurrentHashMap<>();//All clients
    private ConcurrentHashMap<Integer, String> loggedInClients = new ConcurrentHashMap<>();//All clients
    private JsonUser userDataBase;
    private JsonMovie movieDataBase;

    @Override
    public boolean send(int connectionId, Object msg) {
        if (!this.clientsDataBase.containsKey(connectionId))
            return false;
        else {//exists
            this.clientsDataBase.get(connectionId).send(msg);
            return true;
        }//end of else
    }

    /**
     * @param msg send a message to every client
     *            in TPC--> each client has a thread which deals with incoming messages and return answers to the client
     *                      while preforming broadcast a single thread issues an answer to every client this could cause a segement of the code
     *                      to be unsafe that is why we need to add synchronization in the BlockingConnectionHandler
     *            in Reactor--> Clients sends commands to the Server, the Server has one selector thread which :
     *                              *receives encoded messages from the socket, decodes them,and hands the responsibility of processing them to the ATP threads
     *                              *receives decoded messages from the ATP threads (containing the answers to the clients) encodes them and sends them to server
     *                          the WriteQueue used in NonBlockingConnectionHandler is a concurrentLinkedList while using broadcast there's a possibily
     *                          that some threads  are writing to it in the sametime, hopefully the fact that the list is concurrent will protect the code
     *                          so we dont need to add sync to it
     *
     *
     *
     */
    @Override
    public void broadcast(Object msg) {
        Collection<Integer> ActiveClients = this.loggedInClients.keySet();
        ConnectionHandler connectedClient;
        for (Integer activeClient : ActiveClients) {///what if some user managed to sign out between the given set of key and this line
            connectedClient = this.getClientsDataBase().get(activeClient);
            if(connectedClient == null)
                continue;
            connectedClient.send("BROADCAST " + msg);
        }

    }

    @Override
    public void disconnect(int connectionId) {

        boolean exists = this.clientsDataBase.containsKey(connectionId);

        if (exists) {
            this.loggedInClients.remove(connectionId);
            this.clientsDataBase.remove(connectionId);
        }//end of if


    }

    @Override
    public boolean AddConnection(int connectionId, ConnectionHandler handler) {
        if (!this.clientsDataBase.contains(connectionId)) {
            this.clientsDataBase.put(connectionId, handler);
        }
        return this.clientsDataBase.contains(connectionId);//true - added/exists false not exists
    }

    public boolean isLoggedIn(int conId, String UserName) {
        return this.loggedInClients.containsKey(conId) || this.loggedInClients.containsValue(UserName);
    }

    public boolean isLoggedIn(int conId) {
        return this.loggedInClients.containsKey(conId);
    }
    public void SetJson(JsonUser userDataBase, JsonMovie movieDataBase){
        this.userDataBase = userDataBase;
        this.movieDataBase = movieDataBase;
    }

    public JsonUser getUserDataBase() {
        return userDataBase;
    }

    public JsonMovie getMovieDataBase() {
        return movieDataBase;
    }

    public ConcurrentHashMap<Integer, String> getLoggedInClients() {
        return loggedInClients;
    }

    public ConcurrentHashMap<Integer, ConnectionHandler> getClientsDataBase() {
        return clientsDataBase;
    }

    public void addLoggedInUser(int connectionId, String userName) {
        this.loggedInClients.put(connectionId,userName);
    }
}
