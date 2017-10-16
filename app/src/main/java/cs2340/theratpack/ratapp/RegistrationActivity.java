package cs2340.theratpack.ratapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class RegistrationActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private static final String TAG = "RegistrationActivity";

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

<<<<<<< Updated upstream
=======
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:registered:" +user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

>>>>>>> Stashed changes
        setContentView(R.layout.activity_registration);

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


<<<<<<< Updated upstream
    private void createUser(String email, String username,  String password, UserType userType) {
        User user = new User(username, email, password, userType);
        user.register(RegistrationActivity.this, new Intent(RegistrationActivity.this, MainActivity.class));
=======
    private void createUser(String email, final String username,  String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String uid = mAuth.getCurrentUser().getUid();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                mDatabase.child("users").child(uid).child("username").setValue(username);
                mDatabase.child("users").child(uid).child("admin").setValue(isAdmin);
                mDatabase.child("users").child(uid).child("last-attempt").setValue(timestamp.toString());
                Log.d(TAG, "createdUserWithEmail:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                }
            }
        });
>>>>>>> Stashed changes
    }
}
