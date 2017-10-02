package cs2340.theratpack.ratapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //this is where the actual button clicking occurs
        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
    }

    private void attemptRegister() {
        //check if information is entered
        EditText nameEditText = (EditText) findViewById(R.id.registration_name);
        String name = nameEditText.getText().toString();
        EditText usernameEditText = (EditText) findViewById(R.id.registration_username);
        String username = usernameEditText.getText().toString();
        EditText passwordEditText = (EditText) findViewById(R.id.registration_password);
        String password = passwordEditText.getText().toString();
        EditText repasswordEditText = (EditText) findViewById(R.id.registration_password_redo);
        String repassword = repasswordEditText.getText().toString();

        boolean cancel = false;

        if (name.matches("") && !cancel) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            cancel = true;
        }
        if (username.matches("") && !cancel) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            cancel = true;
        }
        if (password.matches("") && !cancel) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            cancel = true;
        }
        if (repassword.matches("") && !cancel) {
            Toast.makeText(this, "Please retype password", Toast.LENGTH_SHORT).show();
            cancel = true;
        }
        if(!repassword.equals(password) && !cancel) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            cancel = true;
        }

        if (!cancel) {
            register(name, username, password);
        }
    }

    private void register(String name, String username, String password) {
        //store information
        Toast.makeText(this, "works", Toast.LENGTH_SHORT).show();
    }
}
