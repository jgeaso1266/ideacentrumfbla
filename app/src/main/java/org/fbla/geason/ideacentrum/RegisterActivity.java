package org.fbla.geason.ideacentrum;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import static org.fbla.geason.ideacentrum.LoginActivity.database;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText phone;
    private EditText password;
    private EditText passwordConfirm;
    private Button registerButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Give views to elements
        firstname = findViewById(R.id.register_firstname);
        lastname = findViewById(R.id.register_lastname);
        email = findViewById(R.id.register_email);
        phone = findViewById(R.id.register_phone);
        password = findViewById(R.id.profile_password);
        passwordConfirm = findViewById(R.id.register_password_confirm);
        registerButton = findViewById(R.id.register_register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get text from EditTexts
                String firstText = firstname.getText().toString();
                String lastText = lastname.getText().toString();
                String emailText = email.getText().toString();
                String phoneText = phone.getText().toString();
                String passwordText = password.getText().toString();
                String passwordConfirmText = passwordConfirm.getText().toString();

                // Register
                register(firstText, lastText, emailText, phoneText, passwordText, passwordConfirmText);
            }
        });
    }

    void register(String first, String last, String email, String phone, String password, String passwordConfirmation) {

        // Check if email is available
        List<Profile> profileList = database.profileDao().getAllProfiles();
        for (int i = 0; i < profileList.size(); i++) {
            if (email.equals(profileList.get(i).email)) {
                Toast.makeText(this, "Username is not available. Try again.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Check if passwords match
        if (!password.equals(passwordConfirmation)) {
            Toast.makeText(this, "Passwords do not match. Try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate all the fields. If anyone is empty, send a message
        if(!validate(new String[]{first, last, email, phone, password, passwordConfirmation})) {
            Toast.makeText(this, "Fill in all boxes!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make all the info into a Profile and add to database
        Profile registerProfile = new Profile(null, first, last, email, phone, password, "", "", "");
        int profileId = (int) database.profileDao().newProfile(registerProfile);

        // Go to home screen
        Intent toMainActivity = new Intent(this, MainActivity.class);
        startActivity(toMainActivity);
    }

    boolean validate(String[] fields) {

        // Check if any fields are empty
        for(int j = 0; j < fields.length; j++) {
            if (fields[j].length() == 0) {
                return false;
            }
        }

        return true;
    }
}
