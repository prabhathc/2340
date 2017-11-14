package cs2340.theratpack.ratapp;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import cs2340.theratpack.ratapp.model.RatModel;


/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UploadRatTest {
    private final RatModel model = RatModel.INSTANCE;

    @Test
    public void CheckIfParamIsNull() {
        assertEquals(null, model.findRatById(null));
    }

    @Test
    public void CheckIfNotInModel() {
        assertEquals(null, model.findRatById("99999999999"));
    }

    @Test
    public void CheckIfInModel() {
        assertEquals("MANHATTAN", model.findRatById("36907529").getBorough());
    }



}
