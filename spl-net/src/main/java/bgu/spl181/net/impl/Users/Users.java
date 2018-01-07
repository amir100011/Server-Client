package bgu.spl181.net.impl.Users;

import bgu.spl181.net.impl.Users.UserInfo;
import java.util.List;

public class Users {
    private List<UserInfo> users;


    public void AddUser(UserInfo userInfo) {
        users.add(userInfo);
    }

    public UserInfo GetUser(String username) {
        for (UserInfo userInfo : users) {
            if(userInfo.getUserName().equals(username))
                return userInfo;
        }
        return null;//not found
    }

    public boolean UserExist(UserInfo userInfo) {
        for (UserInfo info : this.users) {
            if(info.getUserName().equals(userInfo.getUserName()))
                return true;
        }
        return false;
    }

    public boolean HasUser(String userName) {
        for (UserInfo info : this.users) {
            if(info.getUserName().equals(userName))
                return true;
        }
        return false;
    }
}

