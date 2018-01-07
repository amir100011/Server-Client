package bgu.spl181.net.impl.Json;

import bgu.spl181.net.impl.Users.Users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonMovie {

    private Movies users = new Users();//Need this to Write to Json
    private ReadWriteLock Lock = new ReentrantReadWriteLock();
    private String path;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private JsonObject jsondoc;


}
