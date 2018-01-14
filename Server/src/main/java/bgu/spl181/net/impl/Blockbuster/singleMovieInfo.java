package bgu.spl181.net.impl.Blockbuster;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a single movie in the system
 * primitive implementation
 */
public class singleMovieInfo {

    private int id;
    private String name;
    private int totalAmount;
    private int availableAmount;
    private int price;
    private List<String> bannedCountries = new ArrayList<>();

    public singleMovieInfo(int id, String name, int totalCopies, int price, List<String> bannedCountries){
        this.bannedCountries = bannedCountries;
        this.availableAmount = totalCopies;
        this.totalAmount = totalCopies;
        this.price = price;
        this.id = id;
        this.name = name;
    }

    public int getAvailableAmount() { return availableAmount; }

    public int getId() { return id; }

    public int getPrice() { return price; }

    public int getTotalAmount() { return totalAmount; }

    public String getName() { return name; }

    public List<String> getBannedCountries() { return bannedCountries; }

    public void setPrice(int price) { this.price = price; }

    public void addCopy(){ this.availableAmount++; }

    public void rentMovie(){ this.availableAmount--; }

    public boolean isBanned(String userCountry){ return bannedCountries.contains(userCountry); }

}
