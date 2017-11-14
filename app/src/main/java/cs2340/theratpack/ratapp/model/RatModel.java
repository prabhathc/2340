
package cs2340.theratpack.ratapp.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RatModel {
    private static final String TAG = "RatModel";
    public static final RatModel INSTANCE = new RatModel();

    private List<Rat> rats;

    private RatModel() {
        rats = new ArrayList<>();
    }

    public void add(Rat rat) { rats.add(rat); }

    public void deleteAll() { rats = new ArrayList<>(); }

    // --Commented out by Inspection (11/10/2017 12:03 AM):public List<Rat> getRats() { return rats; }

    /**
     * Method to find a particular Rat in the ArrayList backed model
     * @param key The key for the particular sighting that you are trying to find
     * @return The rat sighting or null if it is not found
     */
    public Rat findRatById(String key) {
        if (key == null) {
            return null;
        }
        for (Rat r: rats) {
            if (r.getUniqueKey().equals(key)) return r;
        }
        Log.d(TAG, "Failed to find a rat with key: " + key);
        return null;
    }
}
