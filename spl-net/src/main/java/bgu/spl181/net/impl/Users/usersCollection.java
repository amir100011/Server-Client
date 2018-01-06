package bgu.spl181.net.impl.Users;

import java.util.LinkedList;

public class usersCollection {

    private LinkedList<singleUserInfo> users = new LinkedList<>();

    public void addRemoveuser(singleUserInfo user){

        if(hasUser(user.getUserName()))
            this.users.remove(user);
        else
            this.users.add(user);
    }

    public boolean hasUser(String user){

        for (singleUserInfo userToCompare: this.users) {
            if (userToCompare.getUserName().equals(user))
                return true;
        }
        return false;
    }


    /**
     * @param userName
     * @return the user as singleUserInfo if exists. Otherwise return null
     */
    public singleUserInfo getSpecificuser(String userName) {

        if (hasUser(userName)) {
            for (singleUserInfo userToCompare : this.users) {
                if (userToCompare.getUserName().equals(userName))
                    return userToCompare;
            }//end of for
        }//end of if

        return null;
    }

    /**
     * @return current user list as a singleUserInfo array
     */
    public singleUserInfo[] toArray(){

        singleUserInfo[] usersAsArray = new singleUserInfo[this.users.size()];

        for (singleUserInfo userToCompare : this.users)
            usersAsArray[this.users.indexOf(userToCompare)] = userToCompare;

        return usersAsArray;

    }
}
