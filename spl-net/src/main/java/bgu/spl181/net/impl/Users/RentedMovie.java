package bgu.spl181.net.impl.Users;

public class RentedMovie {
    private  int id;
    private String name;

    public RentedMovie(int id,String Moviename){
        this.id = id;
        this.name = Moviename;
    }

    public String getMovieName() {
        return name;
    }

    public int getId() {
        return id;
    }

}
