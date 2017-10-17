package cs2340.theratpack.ratapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.Iterator;

/** Model for the user class
 * Created by Jamal Paden on 9/28/2017.
 */

public class User {
    private static final String TAG = "User";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseUser fUser;

    private String uid;
    private String username;
    private String password;
    private String email;
    private int loginAttempts;
    private Timestamp lastLoginAttempt;
    private UserType type;

    //possible additional constructor for user-user interaction where we dont have email/pass, only
    //uid or username or something like that

    /**
     * Constructor for login. If explicitly using this one, always call login next
     * @param email
     * @param password
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(TAG, "AuthState changed");

            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Constructor used for when registering a user. Always call register after this to get uid
     * @param username intended username
     * @param email user email address
     * @param password user password
     * @param type admin or standard user
     */
    public User(String username, String email, String password, UserType type) {
        this(email, password);
        this.username = username;
        this.type = type;
        this.loginAttempts = 0;
    }

    /**
     * Attempts to register the user via firebase, then moves on to the next screen
     * @param context the current screen
     * @param intent the intended screen
     */
    public void register(final Context context, final Intent intent) {
        lastLoginAttempt = new Timestamp(System.currentTimeMillis());
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //fUser = mAuth.getCurrentUser();
                            lastLoginAttempt = new Timestamp(System.currentTimeMillis());
                            uid = mAuth.getCurrentUser().getUid();
                            setupReadListeners();
                            mDatabase.child("users").child(uid).child("admin").setValue(isAdmin());
                            mDatabase.child("users").child(uid).child("last-attempt").setValue(lastLoginAttempt.toString());
                            mDatabase.child("users").child(uid).child("username").setValue(username);
                            mDatabase.child("usernames").child(username).setValue(uid);
                            Log.w(TAG, "registration: success. UID: " + uid);
                            context.startActivity(intent);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure" + email, task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    /**
     * Attempts login, and goes on to next screen if successful
     * @param context current screen
     * @param intent next intended screen
     */
    public void login(final Context context, final Intent intent) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    lastLoginAttempt = new Timestamp(System.currentTimeMillis());
                    uid = mAuth.getCurrentUser().getUid();
                    setupReadListeners();
                    mDatabase.child("users").child(uid).child("last-attempt").setValue(lastLoginAttempt.toString());
                    Log.w(TAG, "login: success");
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //this is still a work in progress lol
    private void setupReadListeners() {
        DatabaseReference user = mDatabase.child("users").child(uid);
        user.child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Read failed");
            }
        });
        user.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                type = ((boolean)dataSnapshot.getValue() ? UserType.ADMIN : UserType.USER);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Read failed");
            }
        });

    }
    /**
     * Method to increment user logins. Used for security
     */
    public void incrementLogins() {
        this.loginAttempts++;
    }


    /**
     * Method to return whether or not a given user is an admin
     * @return whether or not a particular instance is an admin
     */
    public boolean isAdmin() {
        return type == UserType.ADMIN;
    }



}
