package bgu.spl181.net.impl.Blockbuster;

import java.util.List;

/**
 * This class represents all the movies in our system
 */
public class movieCollection {

    private List<singleMovieInfo> movies;

    public void addRemoveMovie(singleMovieInfo movie){

        if(hasMovie(movie.getMovieName()))
            this.movies.remove(movie);
        else
            this.movies.add(movie);
    }

    public boolean hasMovie(String movie){

        for (singleMovieInfo movieToCompare: this.movies) {
            if (movieToCompare.getMovieName().equals(movie))
                return true;
        }
        return false;
    }


    /**
     * @param movieName
     * @return the movie as singleMovieInfo if exists. Otherwise return null
     */
    public singleMovieInfo getSpecificMovie(String movieName) {

        if (hasMovie(movieName)) {
            for (singleMovieInfo movieToCompare : this.movies) {
                if (movieToCompare.getMovieName().equals(movieName))
                    return movieToCompare;
            }//end of for
        }//end of if

        return null;
    }

    /**
     * @return current movie list as a singleMovieInfo array
     */
    public singleMovieInfo[] toArray(){

        singleMovieInfo[] moviesAsArray = new singleMovieInfo[this.movies.size()];

        for (singleMovieInfo movieToCompare : this.movies)
            moviesAsArray[this.movies.indexOf(movieToCompare)] = movieToCompare;

        return moviesAsArray;

    }
}
