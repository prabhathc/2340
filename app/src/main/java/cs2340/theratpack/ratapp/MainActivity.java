package cs2340.theratpack.ratapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private ArrayList<Rat> rats = new ArrayList<>();

    private int pageNumber = 0;
    private int ratsPerPage = 10; //this number can be adjusting for cacheing/speed purposes
    private String anchorKey; //see loadNextPage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button logout = (Button) findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(TAG, "AuthState changed");

            }
        };
        mAuth.addAuthStateListener(mAuthListener);
        //this can also be used just as an async task that loads as you scroll through a list
        loadNextPage();
    }

    private void loadNextPage() {
        //if its the first page, you want to order by key, but you dont have to start at a specific key
        if (pageNumber == 0) {
            mDatabase.child("rat sightings").orderByKey().limitToFirst(ratsPerPage)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> ratSnaps = dataSnapshot.getChildren();

                            for (DataSnapshot ratSnap : ratSnaps) {
                                rats.add(new Rat(ratSnap));
                                anchorKey = ratSnap.getKey();
                            }

                            Log.d(TAG, String.valueOf(dataSnapshot.getChildrenCount()) + " rats loaded");
                            pageNumber++;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } else {
            //if youre on any later query, you want to start at the last key of the last query
            //make the limit+1, then skip the first key (you already have it in your prev query)
            mDatabase.child("rat sightings").orderByKey().startAt(anchorKey).limitToFirst(ratsPerPage+1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> ratSnaps = dataSnapshot.getChildren();
                            for (DataSnapshot ratSnap : ratSnaps) {
                                if (ratSnap.getKey() != anchorKey) {
                                    rats.add(new Rat(ratSnap));
                                    anchorKey = dataSnapshot.getKey();
                                }
                            }

                            Log.d(TAG, String.valueOf(dataSnapshot.getChildrenCount()) + " rats loaded");
                            pageNumber++;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

}
