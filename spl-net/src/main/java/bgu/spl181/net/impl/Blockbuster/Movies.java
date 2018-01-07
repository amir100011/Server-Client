package bgu.spl181.net.impl.Blockbuster;


import java.util.List;

/**
 * This class contains data for json writing
 */
public class Movies {

    private List<singleMovieInfo> movies;

    public void addMovie(singleMovieInfo newMovie){ movies.add((newMovie)); }

    public boolean HasMovie(String movieName){ return movies.contains(movieName); }

    /**
     * @param movieName
     * @return movieInfo if movieName exists, else null
     */
    public singleMovieInfo getSpecificMovie(String movieName){
        for (singleMovieInfo movieToReturn: this.movies) {
            if(movieToReturn.getMovieName().equals(movieName));
            return movieToReturn;
        }
        return null;
    }


}
