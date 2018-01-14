package bgu.spl181.net.impl.Json;

import bgu.spl181.net.impl.Blockbuster.Movies;
import bgu.spl181.net.impl.Blockbuster.singleMovieInfo;
import bgu.spl181.net.impl.Users.RentedMovie;
import com.google.gson.*;

import java.io.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonMovie {

    private Movies movies = new Movies();//Need this to Write to Json
    private ReadWriteLock Lock = new ReentrantReadWriteLock();
    private String path;
    private Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class, (JsonSerializer<Integer>) (intger, type, JsonSerializationContext) -> new JsonPrimitive(intger.toString())).setPrettyPrinting().create();
    private JsonObject jsondoc;

    public JsonMovie(String path) {
        this.path = path;
        updateMovies();   //Initialize JsonMovies
    }


    /**
     * this methos writes new information to the json file, has to be thread-safe
     *
     * @return true if suceeded in writing
     */
    private boolean WriteJson() {
        Lock.writeLock().lock();
        try (FileWriter UpDater = new FileWriter(path)) {
            gson.toJson(movies, Movies.class, UpDater);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Lock.writeLock().unlock();
            return true;
        }
    }

    /**
     * updates the movies of the system
     */
    private void updateMovies() {
        this.Lock.readLock().lock();
        try (Reader Reader = new FileReader(path)) {
            movies = gson.fromJson(Reader, movies.getClass());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.Lock.readLock().unlock();
        }
    }

    /**note that this function is always called from Block Movies task runnable meaning the json is locked for other users
     * @param movieToAdd
     * @return true if movie was added successfully elsewise returns false
     */
    public boolean addMovie(singleMovieInfo movieToAdd) {
       // updateMovies();
        if (movies.HasMovie(movieToAdd.getName())) {
            return false;
        }
        movies.addMovie(movieToAdd);
        WriteJson();
        return true;
    }

    public singleMovieInfo getSpecificMovie(String movieName) {
        Lock.readLock().lock();
     //   updateMovies();
        singleMovieInfo ans = movies.getSpecificMovie(movieName);
        Lock.readLock().unlock();
        return ans;
    }

    public boolean hasMovie(String movieName) {
        Lock.readLock().lock();
        //updateMovies();
        boolean ans = movies.HasMovie(movieName);
        Lock.readLock().unlock();
        return ans;
    }

    /*
    public boolean isBanned(String movieName, String usersCountry) {
        updateMovies();
        if (hasMovie(movieName)) {
            getSpecificMovie(movieName).getBannedCountries().contains(usersCountry);
            return true;
        }
        return false;
    }*/

    /**note that this function is always called from Block Movies task runnable meaning the json is locked for other users
     * @param movieName
     * @return true if movie has available copies else false
     */
    public boolean hasAviliableCopiesLeft(String movieName) {
        //updateMovies();
        if (hasMovie(movieName))
            return (getSpecificMovie(movieName).getAvailableAmount() > 0);
        return false;
    }

    /** note that this function is always called from Block Movies task runnable meaning the json is locked for other users
     * @return all the movies in they system, the readlock is to ensure that no thread write to json and change the DB while reading from the DB
     */
    public String getAllMoviesNames() {
        Lock.readLock().lock();
        String tmp = "";
        if(this.movies.getMovies().size() > 0) {
            tmp = "\"" + movies.getMovies().get(0).getName() + "\"";
            for (int index = 1; index < movies.getMovies().size(); index++) {
                tmp = tmp.concat(" \"" + movies.getMovies().get(index).getName() + "\"");
            }
        }
        Lock.readLock().unlock();
        return tmp;
    }

    public void BlockMovies(Runnable task) {
        this.Lock.writeLock().lock();
        task.run();
        this.Lock.writeLock().unlock();
    }


    /** note that this function is always called from Block Movies task runnable meaning the json is locked for other users
     * @param movieName
     * @return RentedMovie for user
     */
    public RentedMovie RentMovie(String movieName) {
        Lock.writeLock().lock();
        //updateMovies();
        RentedMovie ans = this.movies.RentMovie(movieName);
        WriteJson();
        Lock.writeLock().unlock();
        return ans;
    }

    /** note that this function is always called from Block Movies task runnable meaning the json is locked for other users
     * @param movieName adds a copy to Movie DB
     */
    public void addCopy(String movieName){
        //updateMovies();
        this.movies.getSpecificMovie(movieName).addCopy();//Preforming a change to the DB this is why we cannot allow other threads to preform reads from DB
        WriteJson();
    }


    /**note that this function is always called from Block Movies task runnable meaning the json is locked for other users
     * @return new id for a new movie
     */
    public int getNewMovieId() {
        //updateMovies();
        int ans = this.movies.getNewMovieId();//Enlisting a new Movie Id for the action add,not that this action is always called from BlockMovies
        return ans;
    }

    /**note that this function is always called from Block Movies task runnable meaning the json is locked for other users
     * @param movieToRemmove remove a movie from Movie DB
     */
    public void remMovie(singleMovieInfo movieToRemmove) {
        //updateMovies();
        this.movies.remMovie(movieToRemmove);
        WriteJson();
    }

    /**note that this function is always called from Block Movies task runnable meaning the json is locked for other users
     * @param price
     * @param movieName
     * changes a price of a movie
     */
    public void changePrice(Integer price, String movieName) {
        //updateMovies();
        this.movies.changePrice(price,movieName);
        WriteJson();
    }
}



