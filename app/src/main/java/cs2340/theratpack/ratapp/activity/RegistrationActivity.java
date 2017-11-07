package cs2340.theratpack.ratapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cs2340.theratpack.ratapp.model.User;
import cs2340.theratpack.ratapp.model.UserType;
import cs2340.theratpack.ratapp.R;


public class RegistrationActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private static final String TAG = "RegistrationActivity";
    private DatabaseReference mDatabase;
    private Iterable<DataSnapshot> usernames;
    private EditText mEmailView;
    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mPasswordView2;
    private ToggleButton mAdminToggle;
    private View mRegistrationFormView;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("usernames").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        usernames = dataSnapshot.getChildren();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
        //this is where the actual button clicking occurs
        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        mRegistrationFormView = findViewById(R.id.registration_form);
        mEmailView = (EditText) findViewById(R.id.registration_email);
        mUsernameView = (EditText) findViewById(R.id.registration_username);

        mPasswordView = (EditText) findViewById(R.id.registration_password);
        mPasswordView2 = (EditText) findViewById(R.id.registration_password_redo);
        mAdminToggle = (ToggleButton) findViewById(R.id.registration_admin_toggle);

    }

    private void attemptRegister() {

        //Reset errors
        mEmailView.setError(null);
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mPasswordView2.setError(null);

        //check if information is entered
        String email = mEmailView.getText().toString();
        String username = mUsernameView.getText().toString();
        UserType userType = mAdminToggle.isChecked() ? UserType.ADMIN : UserType.USER;
        String password = mPasswordView.getText().toString();
        String repassword = mPasswordView2.getText().toString();

        boolean usernameExists = false;

        for(DataSnapshot fbUsername : usernames) {
            if (username.equals((String)fbUsername.getKey())) {
                usernameExists = true;
            }
        }

        View focusView = null;
        boolean cancel = false;

        // Checks that things were typed
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (usernameExists) {
            mUsernameView.setError(getString(R.string.error_username_exists));
            focusView = mUsernameView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(repassword)) {
            mPasswordView2.setError(getString(R.string.error_field_required));
            focusView = mPasswordView2;
            cancel = true;
        } else if(!repassword.equals(password)) {
            mPasswordView2.setError("Password not matching");
            focusView = mPasswordView2;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            createUser(email, username, password, userType);
        }
    }


//<<<<<<< Updated upstream
    private void createUser(String email, String username,  String password, UserType userType) {
        User user = new User(username, email, password, userType);
        user.register(RegistrationActivity.this, new Intent(RegistrationActivity.this, MapActivity.class));
    }
}
