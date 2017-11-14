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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cs2340.theratpack.ratapp.R;

/**
 * This is the activity for the graph view of the app
 */
public class GraphActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener{

    private static final String TAG = "GraphActivity";
    private DatabaseReference mDatabase;
    private BarChart chart;
    private List<BarEntry> entries;
    private BarData barData = null;
    private List<String> labels;


    /**
     * Create Method sets up firebase authentication and button/action bar listeners
     * @param savedInstanceState saved state, if there is one
     */
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

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
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


    /**
     * What happens when you press back while the drawer is open
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * List of things that can be done in the drawer
     * @param item the item selected in the drawer
     * @return returns true if drawer is closed
     */
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

    /**
     * List of things that can be done in the drawer
     * @param item the item selected in the drawer
     * @return returns true if drawer is closed
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

    /**
     * Applies changes to graph view based on date selected
     * @param view The view of the date picker
     * @param year start year of the range
     * @param monthOfYear start month of the range
     * @param dayOfMonth start day of the range
     * @param yearEnd end year of the range
     * @param monthOfYearEnd end month of the range
     * @param dayOfMonthEnd end day of the range
     */
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth,int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        //call rats in range on a per month+year basis
        //make sure to account for irregularly selected number of days on the tail months, if less that 1 or 2 months
        entries = new ArrayList<>();
        labels = new ArrayList<>();
        if (year == yearEnd && monthOfYear == monthOfYearEnd) {
            Calendar startDate = Calendar.getInstance();
            startDate.set(year, monthOfYear, dayOfMonth);
            Calendar endDate = Calendar.getInstance();
            endDate.set(yearEnd, monthOfYearEnd, dayOfMonthEnd);
            ratsInRange(startDate.getTimeInMillis(),endDate.getTimeInMillis(), 0, true);
            labels.add(String.valueOf(monthOfYear) + "-" + String.valueOf(year));
        } else {
            int position = 0;
            for (int yr = year; yr <= yearEnd; yr++) {
                for (int month = monthOfYear; month <= monthOfYearEnd; month++) {
                    boolean last = false;
                    Calendar startDate = Calendar.getInstance();
                    startDate.set(Calendar.YEAR, yr);
                    startDate.set(Calendar.MONTH, month);
                    if (month == monthOfYear && yr == year) {
                        startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    } else {
                        startDate.set(Calendar.DAY_OF_MONTH, 1);

                    }

                    Calendar endDate = Calendar.getInstance();
                    endDate.set(Calendar.YEAR, yr);
                    endDate.set(Calendar.MONTH, month);
                    if (month == monthOfYearEnd && yr == yearEnd) {
                        endDate.set(Calendar.DAY_OF_MONTH, dayOfMonthEnd);
                        last = true;
                    } else {
                        endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));

                    }
                    Log.d(TAG, String.valueOf(position) + ": " + String.valueOf(month) + "-"
                            + String.valueOf(yr) + " " + startDate.getTime() + " " + endDate.getTime());
                    labels.add(String.valueOf(month) + "-" + String.valueOf(yr));
                    ratsInRange(startDate.getTimeInMillis(),endDate.getTimeInMillis(), position, last);
                    position++;


                    //account for first and last months
                    //if yr==year and month==month of year check day
                    //if yr==year end and month==month of year rend check day
                    //if the above are true and year==year end and month==month end, only look at start/end days
                }
            }
        }
    }

    /**
     * Actually setting the bar chart stuff. A utility method used by on data set
     * Gets the actual rats in in the date/time range
     * @param startTime start time in unix time (times 1000)
     * @param endTime end time in unix time (times 1000)
     * @param monthYear label of the month and year for the bar
     * @param last is this the last
     */
    private void ratsInRange(long startTime, long endTime, final int monthYear, final boolean last) {
        mDatabase.child("rat sightings").orderByChild("Created Date").startAt(String.valueOf(startTime))
                .endAt(String.valueOf(endTime)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();

                entries.add(new BarEntry(monthYear, count));

                Log.d(TAG, String.valueOf(count) + " rats loaded");
                if (last) {
                    IAxisValueFormatter formatter = new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            return labels.get((int)value);
                        }
                    };
                    XAxis xAxis = chart.getXAxis();
                    xAxis.setGranularity(1f);
                    xAxis.setLabelCount(labels.size());



                    xAxis.setValueFormatter(formatter);
                    BarDataSet dataSet = new BarDataSet(entries, "Rat Count");
                    if (barData != null) {
                        barData.removeDataSet(0);
                        barData.addDataSet(dataSet);
                        chart.notifyDataSetChanged();
                    } else {
                        BarData barData = new BarData(dataSet);
                        chart.setPinchZoom(true);
                        chart.setDrawGridBackground(false);
                        chart.setFitBars(true);
                        chart.setData(barData);
                    }
                    chart.invalidate();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
