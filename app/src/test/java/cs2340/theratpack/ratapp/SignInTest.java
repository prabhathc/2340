package cs2340.theratpack.ratapp;

import com.google.firebase.auth.FirebaseAuth;
import org.junit.Test;
import cs2340.theratpack.ratapp.activity.LoginActivity;
import cs2340.theratpack.ratapp.model.User;

import static junit.framework.Assert.assertNull;

/**
 * Tests if the signIn function found in LoginActivity.java will create a user with an invalid
 * email address.
 * Created by isabela on 11/13/17.
 */

public class SignInTest {
    private FirebaseAuth mAuth;
    private LoginActivity logAct;

    /**
     * Assigns a LoginActivity to SignInTest.
     * @param logAct
     */
    public SignInTest(LoginActivity logAct) {
        this.logAct = logAct;
    }

    /**
     * Checks if a user with an invalid email address will be created.
     */
    @Test
    public void testExistingUser() {
        mAuth = FirebaseAuth.getInstance();
        logAct.signIn("temp@temp.edu", "tempPassword");
        assertNull("Test passed.", mAuth.getCurrentUser());
    }
}
