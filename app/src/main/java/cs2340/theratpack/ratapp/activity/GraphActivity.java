package cs2340.theratpack.ratapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.github.mikephil.charting.charts.BarChart;
import com.google.android.gms.maps.SupportMapFragment;
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

public class GraphActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener{

    private static final String TAG = "Activity2";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private RatModel ratModel = RatModel.INSTANCE;
    private BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_graph);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(TAG, "AuthState changed");

            }
        };
        mAuth.addAuthStateListener(mAuthListener);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        chart = (BarChart) findViewById(R.id.chart);

        Button dateButton = (Button) findViewById(R.id.test_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        GraphActivity.this,
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
            startActivity(new Intent(GraphActivity.this, LoginActivity.class));
        } else if (id == R.id.addrat) {
            startActivity(new Intent(GraphActivity.this, AddRatActivity.class));
        } else if (id == R.id.map_view) {
            startActivity(new Intent(GraphActivity.this, MapActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addrat) {
            startActivity(new Intent(GraphActivity.this, AddRatActivity.class));

        } else if (id == R.id.logOut) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(GraphActivity.this, LoginActivity.class));
        } else if (id == R.id.map_view) {
            startActivity(new Intent(GraphActivity.this, MapActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth,int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        //call rats in range on a per month+year basis
        //make sure to account for irregularly selected number of days on the tail months, if less that 1 or 2 months
        for (int yr = year; yr <= yearEnd; yr++) {
            for (int month = monthOfYear; month <= monthOfYearEnd; month++) {
                //account for first and last months
                //if yr==year and month==monthofyear check day
                //if yr==yearend and month==monthofyrend check day
                //if the above are true and year==year end and month==monthend, only look at start/end days
            }
        }
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

                for (DataSnapshot ratSnap : ratSnaps) {
                    if ((String)ratSnap.child("Longitude").getValue() != null && ((String)ratSnap.child("Longitude").getValue()).length() > 0) {
                        Rat newRat = new Rat(ratSnap);
                        ratModel.add(newRat);
                        String title = ("ID: " + newRat.getUniqueKey());
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
