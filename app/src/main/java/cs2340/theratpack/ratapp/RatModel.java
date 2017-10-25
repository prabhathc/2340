package cs2340.theratpack.ratapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhiannan on 10/23/2017.
 */

public class RatModel {
    private static final String TAG = "RatModel";
    public static final RatModel INSTANCE = new RatModel();

    private List<Rat> rats;

    private RatModel() {
        rats = new ArrayList<>();
    }

    public void add(Rat rat) { rats.add(rat); }

    public  void add(int x,Rat rat) {rats.add(x, rat);}

    public void deleteAll() { rats = new ArrayList<>(); }

    public List<Rat> getRats() { return rats; }

    public Rat findRatById(String key) {
        for (Rat r: rats) {
            if (r.getUniqueKey().equals(key)) return r;
        }
        Log.d(TAG, "Failed to find a rat with key: " + key);
        return null;
    }
}
