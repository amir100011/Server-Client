package bgu.spl181.net.impl.Json;


import bgu.spl181.net.impl.Users.UserInfo;
import bgu.spl181.net.impl.Users.Users;
import com.google.gson.*;

import java.io.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonUser {
private Users users = new Users();//Need this to Write to Json\
private ReadWriteLock Lock = new ReentrantReadWriteLock();
private String path;
private Gson gson = new GsonBuilder().setPrettyPrinting().create();
private JsonObject jsondoc;

public JsonUser(String path){
    this.path = path;
    updateUsers();   //Initialize JsonUsers
}

    /**
     * updates the users of the system
     */
private void updateUsers(){
    try (Reader Reader = new FileReader(path)) {
        users = gson.fromJson(Reader,users.getClass());
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

}

public boolean AddUser(UserInfo userInfo) {
   if(users.UserExist(userInfo))
       return false;
    users.AddUser(userInfo);
    WriteJson();
    return true;
}

public UserInfo GetUser(String Username){
    updateUsers();
    return users.GetUser(Username);
}
public boolean HasUser(String UserName){
    updateUsers();
    return users.HasUser(UserName);
}

private boolean WriteJson(){
    Lock.writeLock().lock();
    try (FileWriter UpDater = new FileWriter(path)) {
        gson.toJson(users ,Users.class, UpDater);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        Lock.writeLock().unlock();
        return true;
    }
}


    public boolean matchedUserPassword(String userName, String password) {
        if(HasUser(userName))
            return  GetUser(userName).getPassWord().equals(password);
        return false;

    }
}
