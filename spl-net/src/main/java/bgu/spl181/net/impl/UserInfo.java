package bgu.spl181.net.impl;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


//Signle User

public class UserInfo {

    private AtomicBoolean isAdmine = new AtomicBoolean(false);
    private String country;
    private AtomicInteger balance = new AtomicInteger(0);
    private ConcurrentSkipListSet<String> movies = new ConcurrentSkipListSet<>();

    public UserInfo(Boolean isAdmine, String countery, Integer balance, List<String> movies){
        this.isAdmine.set(isAdmine);
        this.balance.set(balance);
        this.country = countery;
        for (String movieName: movies) {
            this.movies.add(movieName);
        }
    }

    public AtomicBoolean getIsAdmine() {
        return isAdmine;
    }

    public AtomicInteger getBalance() {
        return balance;
    }

    public ConcurrentSkipListSet<String> getMovies() {
        return movies;
    }

    public String getCountry() {
        return country;
    }

    public void setBalance(int balance) {
            this.balance.set(balance);
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setIsAdmine(AtomicBoolean isAdmine) {
        this.isAdmine = isAdmine;
    }

    public void setMovies(String movies) {
        if(this.movies.contains(movies))
            this.movies.remove(movies);
        else
            this.movies.add(movies);
    }
}
