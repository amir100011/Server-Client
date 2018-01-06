package bgu.spl181.net.impl.Blockbuster;

/**
 * This class represents a single movie in the system
 * primitive implementation
 */
public class singleMovieInfo {

    private int Id;
    private String movieName;
    private int totalCopies;
    private int copiesLeft;
    private int price;
    private String[] bannedCountries;

    public singleMovieInfo(int id, String name, int totalCopies, int price, String[] bannedCountries){
        this.bannedCountries = bannedCountries;
        this.copiesLeft = totalCopies;
        this.totalCopies = totalCopies;
        this.price = price;
        this.Id = id;
        this.movieName = name;
    }

    public int getCopiesLeft() { return copiesLeft; }

    public int getId() { return Id; }

    public int getPrice() { return price; }

    public int getTotalCopies() { return totalCopies; }

    public String getMovieName() { return movieName; }

    public String[] getBannedCountries() { return bannedCountries; }

    public void setPrice(int price) { this.price = price; }

    public void addCopie(){ this.copiesLeft++; }

    public void rentMocie(){ this.copiesLeft--; }

    public boolean isBanned(String userCountry){

        boolean banned = false;
        for(int index = 0; index < this.bannedCountries.length; index++) {
            if (bannedCountries[index].equals(userCountry))
                banned = true;
        }

        return banned;
    }
}
