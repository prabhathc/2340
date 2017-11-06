package cs2340.theratpack.ratapp.activity;
import android.content.Intent;
        import android.os.AsyncTask;
        import android.support.annotation.NonNull;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
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
        import java.util.Date;

        import cs2340.theratpack.ratapp.R;
        import cs2340.theratpack.ratapp.model.Rat;
        import cs2340.theratpack.ratapp.model.RatModel;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener {
    private static final String TAG = "MapActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private RatModel ratModel = RatModel.INSTANCE;



    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
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

        Button logout = (Button) findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MapActivity.this, LoginActivity.class));
            }
        });

        Button add_rat = (Button) findViewById(R.id.add_button);
        add_rat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapActivity.this, AddRatActivity.class));
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */



    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Base map loaded");
        mMap = googleMap;
        Button testButton = (Button) findViewById(R.id.test_button);
        //for the time being its just going to stupidly reload all rats even if there is range overlap
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.0, -73.0), 7));
    }
    //    @Override
//    public void onResume() {
//        super.onResume();
//        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
//        if(dpd != null) dpd.setOnDateSetListener(this);
//    }
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

