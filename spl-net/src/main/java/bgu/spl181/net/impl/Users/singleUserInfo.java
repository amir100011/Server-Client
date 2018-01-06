package bgu.spl181.net.impl.Users;

import java.util.LinkedList;

/**
 * This Class represents a single user in the system
 * primitive implementation
 */
public class singleUserInfo {

    private String userName;
    private boolean isAdmine;
    private String password;
    private String country;
    private LinkedList<String> rentedMovies = new LinkedList<>();
    private int balance = 0;

    public singleUserInfo(String userName, boolean isAdmine, String password, String country){
        this.userName = userName;
        this.isAdmine = isAdmine;
        this.password = password;
        this.country = country;
    }


    public String getCountry() { return country; }

    public boolean isAdmine() { return isAdmine; }

    public String getPassword() { return password; }

    public int getBalance() { return balance; }

    public String getUserName() { return userName; }

    public LinkedList<String> getRentedMovies() { return rentedMovies; }

    public void balanceAdd(int balance) { this.balance += balance; }//addition

    public void balanceDec(int balance) { this.balance -= balance; }//deduction

    public boolean isRented(String movieName){ return this.rentedMovies.contains(movieName); }

    public void addRemoveMovie(String movieName) {
        if(this.rentedMovies.contains(movieName))
            rentedMovies.remove(movieName);
        else
            rentedMovies.add(movieName);
    }
}
