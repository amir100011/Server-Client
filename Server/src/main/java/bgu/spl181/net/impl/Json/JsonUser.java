package bgu.spl181.net.impl.Json;


import bgu.spl181.net.impl.Users.RentedMovie;
import bgu.spl181.net.impl.Users.UserInfo;
import bgu.spl181.net.impl.Users.Users;
import com.google.gson.*;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonUser {
private Users users = new Users();//Need this to Write to Json\
private ReadWriteLock Lock = new ReentrantReadWriteLock();
private String path;
private Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class,(JsonSerializer<Integer>)(intger,type,JsonSerializationContext)->new JsonPrimitive(intger.toString())).setPrettyPrinting().create();
private JsonObject jsondoc;

public JsonUser(String path){
    this.path = path;
    updateUsers();   //Initialize JsonUsers
}

    /**
     * updates the users of the system
     */
private void updateUsers(){
    Lock.readLock().lock();
    try (Reader Reader = new FileReader(path)) {
        users = gson.fromJson(Reader,users.getClass());
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }finally {
        Lock.readLock().unlock();
    }

}

public boolean AddUser(UserInfo userInfo) {
    Lock.writeLock().lock();
    users.AddUser(userInfo);
    WriteJson();
    Lock.writeLock().unlock();
    return true;
}

public UserInfo GetUser(String Username){
    Lock.readLock().lock();
   // updateUsers();
    UserInfo ans = users.GetUser(Username);
    Lock.readLock().unlock();
    return ans;
}
public boolean HasUser(String UserName){
    Lock.readLock().lock();
    //updateUsers();
    boolean ans =  users.HasUser(UserName);
    Lock.readLock().unlock();
    return ans;
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
        if(HasUser(userName))//Already synched
            return  GetUser(userName).getPassWord().equals(password);
        return false;

    }

    public void addToBalance(String userName,int addMoney){
        Lock.writeLock().lock();
        if(HasUser(userName)){
            GetUser(userName).addToBalance(addMoney);
            WriteJson();
        }
        Lock.writeLock().unlock();
    }


    public void remMovie(String movieName,String userName){
        Lock.writeLock().lock();
      //  updateUsers();
        GetUser(userName).RemMovies(movieName);
        WriteJson();
        Lock.writeLock().unlock();
    }

    public void addMovie(String userName,RentedMovie rentedMovie, int moviePrice) {
    Lock.writeLock().lock();
    //updateUsers();
    GetUser(userName).addMovie(rentedMovie,moviePrice);
    WriteJson();
    Lock.writeLock().unlock();
    }
}
