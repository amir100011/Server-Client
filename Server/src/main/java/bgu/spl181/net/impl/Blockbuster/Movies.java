package bgu.spl181.net.impl.Blockbuster;


import bgu.spl181.net.impl.Users.RentedMovie;

import java.util.List;

/**
 * This class contains data for json writing
 */
public class Movies {

    private List<singleMovieInfo> movies;

    public void addMovie(singleMovieInfo newMovie){ movies.add((newMovie)); }

    public boolean HasMovie(String movieName){

        for (int index = 0; index < movies.size(); index++){
            String curMovieName = movies.get(index).getName();
            if(curMovieName.equals(movieName))
                return true;
        }

        return false;
    }

    /**
     * @param movieName
     * @return movieInfo if movieName exists, else null
     */
    public singleMovieInfo getSpecificMovie(String movieName){
        for (singleMovieInfo movieToReturn: this.movies) {
            if(movieToReturn.getName().equals(movieName))
                return movieToReturn;
        }
        return null;
    }

    public List<singleMovieInfo> getMovies() {
        return movies;
    }

    public RentedMovie RentMovie(String movieName) {
        singleMovieInfo movie = getSpecificMovie(movieName);
        movie.rentMovie();//decrement available copies
        return new RentedMovie(movie.getId(), movie.getName());
    }


    public int getNewMovieId() {
        int highestId = 0;
        for (singleMovieInfo movie: this.movies) {
            if(highestId < movie.getId())
                highestId = movie.getId();
        }
        return highestId + 1;
    }

    public void remMovie(singleMovieInfo movieToRemmove) {
        for (singleMovieInfo movie : this.movies) {
            if(movie.getName().equals(movieToRemmove.getName())){
                this.movies.remove(movie);
                break;
            }
        }
        return;
    }

    public void changePrice(Integer price, String movieName) {
        for (singleMovieInfo movie : this.movies) {
            if(movie.getName().equals(movieName)) {//Found the movie
                movie.setPrice(price);
                return;
            }
        }
    }
}
