package bgu.spl181.net.impl.generalImpls;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.bidi.ConnectionHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class connectionImpl implements Connections {

    private ConcurrentHashMap<Integer, ConnectionHandler> clientsDataBase = new ConcurrentHashMap<>();//All clients


    @Override
    public boolean send(int connectionId, Object msg) {
        if (!this.clientsDataBase.containsKey(connectionId))
            return false;
        else {//exists
            this.clientsDataBase.get(connectionId).send(msg);
            return true;
        }//end of else
    }

    @Override
    public void broadcast(Object msg) {

        Collection<ConnectionHandler> ActiveClients = this.clientsDataBase.values();
        for (ConnectionHandler activeClient : ActiveClients) {
            activeClient.send(msg);
        }

    }

    @Override
    public void disconnect(int connectionId, String UserName) {

        boolean exists = this.clientsDataBase.containsKey(connectionId);

        if (exists) {
            ConnectionHandler disconnect = this.clientsDataBase.get(connectionId);
            try {
                disconnect.close();
                clientsDataBase.remove(connectionId,UserName);
            } catch (IOException ignored) {
                ignored.printStackTrace();//TODO delete before submitting
            }

        }//end of if

    }

    @Override
    public boolean AddConnection(int connectionId, ConnectionHandler handler) {
        if (!this.clientsDataBase.contains(connectionId)) {
            this.clientsDataBase.put(connectionId, handler);
        }
        return this.clientsDataBase.contains(connectionId);//true - added/exists false not exists
    }
}
