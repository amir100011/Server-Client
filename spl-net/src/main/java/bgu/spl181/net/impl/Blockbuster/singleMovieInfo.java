package bgu.spl181.net.impl.Blockbuster;

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
    private String[] bannedCountries;

    public singleMovieInfo(int id, String name, int totalCopies, int price, String[] bannedCountries){
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

    public String[] getBannedCountries() { return bannedCountries; }

    public void setPrice(int price) { this.price = price; }

    public void addCopie(){ this.availableAmount++; }

    public void rentMocie(){ this.availableAmount--; }

    public boolean isBanned(String userCountry){

        boolean banned = false;
        for(int index = 0; index < this.bannedCountries.length; index++) {
            if (bannedCountries[index].equals(userCountry))
                banned = true;
        }

        return banned;
    }
}
