package bgu.spl181.net.impl;

import java.lang.reflect.Array;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class BlockbusterInfo {//TODO use Movie Class to hold info on each movie and this class will manage the blockbuster

    private ConcurrentHashMap<String,Integer> copiesLeft;
    private ConcurrentHashMap<String,Integer> totalCopies;
    private ConcurrentHashMap<String,Integer> prices;
    private ConcurrentHashMap<String,ConcurrentSkipListSet<String>> BannedCountries;

    public BlockbusterInfo(String[] movieName, int[] price, int[] copiesLeft, int[] totalCopies, List<String>[] bannedCountries){
        ConcurrentSkipListSet<String> tmp = new ConcurrentSkipListSet<>();
        for (int index = 0; index < movieName.length; index++) {
            this.copiesLeft.put(movieName[index],copiesLeft[index]);
            this.totalCopies.put(movieName[index],totalCopies[index]);
            this.prices.put(movieName[index],price[index]);
            for (String tmp1: bannedCountries[index]) {
                tmp.add(tmp1);
            }
            this.BannedCountries.put(movieName[index],tmp);
            tmp.clear();
        }
    }


    public ConcurrentHashMap<String, ConcurrentSkipListSet<String>> getBannedCountries() {
        return BannedCountries;
    }

    public ConcurrentHashMap<String, Integer> getCopiesLeft() {
        return copiesLeft;
    }

    public ConcurrentHashMap<String, Integer> getPrices() {
        return prices;
    }

    public ConcurrentHashMap<String, Integer> getTotalCopies() {
        return totalCopies;
    }

    public void setCopiesLeft(ConcurrentHashMap<String, Integer> copiesLeft) {
        this.copiesLeft = copiesLeft;
    }
}
