package cs2340.theratpack.ratapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import cs2340.theratpack.ratapp.R;
import cs2340.theratpack.ratapp.model.Rat;
import cs2340.theratpack.ratapp.model.RatModel;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback, DatePickerDialog.OnDateSetListener {
    private static final String TAG = "Activity2";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private RatModel ratModel = RatModel.INSTANCE;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(TAG, "AuthState changed");

            }
        };
        mAuth.addAuthStateListener(mAuthListener);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Button dateButton = (Button) findViewById(R.id.test_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        MapActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logOut) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MapActivity.this, LoginActivity.class));
        } else if (id == R.id.addrat) {
            startActivity(new Intent(MapActivity.this, AddRatActivity.class));
        } else if (id == R.id.graph_view) {
            startActivity(new Intent(MapActivity.this, GraphActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addrat) {
            startActivity(new Intent(MapActivity.this, AddRatActivity.class));

        } else if (id == R.id.logOut) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MapActivity.this, LoginActivity.class));
        } else if (id == R.id.graph_view) {
            startActivity(new Intent(MapActivity.this, GraphActivity.class));

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Base map loaded");
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.0, -73.0), 7));
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth,int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        Calendar startDate = Calendar.getInstance();
        startDate.set(year, monthOfYear, dayOfMonth);
        Calendar endDate = Calendar.getInstance();
        endDate.set(yearEnd, monthOfYearEnd, dayOfMonthEnd);
        ratModel.deleteAll();
        if (startDate != null && endDate != null) {
            ratsInRange(startDate.getTimeInMillis(),endDate.getTimeInMillis());
        }
        Log.d(TAG, "" +startDate.getTimeInMillis());

    }
    private void ratsInRange(long startTime, long endTime) {
        mDatabase.child("rat sightings").orderByChild("Created Date").startAt(String.valueOf(startTime))
                .endAt(String.valueOf(endTime)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> ratSnaps = dataSnapshot.getChildren();
                //I see two options:
                //load pins into the map in the for-loop (idk if thats possible due to async processes
                //load all the rats in as they currently are, then execute a separate "pin adding"
                //idk tho im dumb
                for (DataSnapshot ratSnap : ratSnaps) {
                    if ((String)ratSnap.child("Longitude").getValue() != null && ((String)ratSnap.child("Longitude").getValue()).length() > 0) {
                        Rat newRat = new Rat(ratSnap);
                        ratModel.add(newRat);
                        LatLng loc = new LatLng(newRat.getLatitude(), newRat.getLongitude());
                        String title = ("ID: " + newRat.getUniqueKey());
                        mMap.addMarker(new MarkerOptions().position(loc).title(title));
                    }
                }
                Log.d(TAG, String.valueOf(dataSnapshot.getChildrenCount()) + " rats loaded");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
