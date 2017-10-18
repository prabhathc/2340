package cs2340.theratpack.ratapp;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jamal Paden on 10/12/2017.
 */

public class Model {
    public static final Model INSTANCE = new Model();

    private List<Rat> rats;

    private Model() {
        rats = new ArrayList<>();
    }

    public void addRat(Rat rat) { rats.add(rat); }

    public List<Rat> getRats() { return rats; }

    public Rat findRatById(int key) {
        for (Rat r: rats) {
            if (r.getUniqueKey() == key) return r;
        }
        Log.d("MYAPP", "Failed to find a rat with key: " + key);
        return null;
    }


}
