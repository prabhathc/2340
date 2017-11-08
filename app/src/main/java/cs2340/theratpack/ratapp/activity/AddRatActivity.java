package cs2340.theratpack.ratapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import cs2340.theratpack.ratapp.R;
import cs2340.theratpack.ratapp.model.Rat;
import cs2340.theratpack.ratapp.model.RatModel;


public class AddRatActivity extends AppCompatActivity {


    private String uniqueKey = "" + (int) Math.floor(Math.random()*100);
    private double latitude = 40.730610;
    private double longitude = 	-73.935242;
    private long createdDate = (new Date()).getTime();
    private static RatModel ratModel = RatModel.INSTANCE;



    private EditText mLocationType;
    private EditText mIncidentZip;
    private EditText mIncidentAddress;
    private EditText mCity;
    private EditText mBorough;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rat);


        Button upload = (Button)findViewById(R.id.upload_button);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptUploadRat();
            }
        });

        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddRatActivity.this, MapActivity.class));
            }
        });

        mLocationType = (EditText) findViewById(R.id.location_type);
        mIncidentZip = (EditText) findViewById(R.id.zip);
        mIncidentAddress = (EditText) findViewById(R.id.address);
        mCity = (EditText) findViewById(R.id.city);
        mBorough = (EditText) findViewById(R.id.borough);
    }
    /**
     * Tag for logging purposes
     */
    private static final String TAG = "AddRatActivity";

    private void attemptUploadRat () {
        // Reset errors.
        mLocationType.setError(null);
        mIncidentZip.setError(null);
        mIncidentAddress.setError(null);
        mCity.setError(null);
        mBorough.setError(null);

        // Store values at the time of the login attempt.
        String locationType = mLocationType.getText().toString();
        String incidentZip = mIncidentZip.getText().toString();
        String incidentAddress = mIncidentAddress.getText().toString();
        String city = mCity.getText().toString();
        String borough = mBorough.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Checks that a locationType was entered
        if (TextUtils.isEmpty(locationType)) {
            mLocationType.setError(getString(R.string.error_field_required));
            focusView = mLocationType;
            cancel = true;
        }
        // Checks that a zip was entered
        if (TextUtils.isEmpty(incidentZip)) {
            mIncidentZip.setError(getString(R.string.error_field_required));
            focusView = mIncidentZip;
            cancel = true;
        }
        // Checks that a address was entered
        if (TextUtils.isEmpty(incidentAddress)) {
            mIncidentAddress.setError(getString(R.string.error_field_required));
            focusView = mIncidentAddress;
            cancel = true;
        }
        // Checks that a city was entered
        if (TextUtils.isEmpty(city)) {
            mCity.setError(getString(R.string.error_field_required));
            focusView = mCity;
            cancel = true;
        }
        // Checks that a borough was entered
        if (TextUtils.isEmpty(borough)) {
            mBorough.setError(getString(R.string.error_field_required));
            focusView = mBorough;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the rat data attempt.

            createRat(locationType,incidentZip,incidentAddress,city, borough);

        }

    }

    private void createRat(String locationType, String incidentZip, String incidentAddress, String city, String borough) {
        Rat rat = new Rat(uniqueKey,createdDate,locationType,incidentZip,incidentAddress,city,borough,longitude,latitude);

        ratModel.add(rat);
        startActivity(new Intent(AddRatActivity.this, MapActivity.class));
    }

    public RatModel getModel() {
        return ratModel;
    }

}
