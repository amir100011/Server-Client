package bgu.spl181.net.impl.Users;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {
    private final String username;
    private final String password;
    private String type = "normal";
    private String country;
    private int balance = 0;
    private List<RentedMovie> movies = new ArrayList<>();

    public UserInfo(String UserName, String passWord, String country) {
        this.username = UserName;
        this.password = passWord;
        this.country = country;
    }

    public String getUserName() {
        return username;
    }

    public String getPassWord() {
        return password;
    }

    public boolean getIsAdmin() {
        return type.equals("admin");
    }

    public int getBalance() {
        return balance;
    }

    public List<RentedMovie> getMovies() {
        return movies;
    }

    public String getCountry() {
        return country;
    }

    public void addToBalance(int balance) {
        this.balance += balance;
    }

    public void decFromBalance(int balance) {
        if(this.balance > balance)
            this.balance -= balance;
    }

    public boolean AddMovies(RentedMovie movie) {
       if(movies.contains(movie))
           return false;
       else {
           movies.add(movie);
           return true;
       }

    }

    public void RemMovies(String movieName) {
        for (RentedMovie movie : movies) {
            if(movie.getMovieName().equals(movieName)){
                movies.remove(movie);
                break;
            }
        }
    }

    public boolean alreadyRented(String movieName){
        for (RentedMovie movie: this.movies) {
            if(movie.getMovieName().equals(movieName))
                return true;
            }
            return false;
    }

    public void addMovie(RentedMovie rentedMovie, int price) {
        this.movies.add(rentedMovie);
        decFromBalance(price);


    }
}
