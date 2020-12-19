package org.fbla.geason.ideacentrum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button loginButton;
    private Button registerButton;
    public static final String PREFS_NAME = "LoginConfirmation";
    public static AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = AppDatabase.getInstance(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        // Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        // If they have already logged in, go straight to home screen
        if(hasLoggedIn)
        {
            Intent toMainActivity = new Intent(this, MainActivity.class);
            toMainActivity.putExtra("id", settings.getInt("id", 0));
            startActivity(toMainActivity);
        }

        // Give views to elements
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_login_button);
        registerButton = findViewById(R.id.login_register_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get text from EditTexts
                String usernameInput = email.getText().toString();
                String passwordInput = password.getText().toString();

                // Login
                login(usernameInput, passwordInput);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Register
                register();
            }
        });
    }

    private void login(String email, String password) {

        // Get complete list of usernames and passwords
        List<Profile> profileList = database.profileDao().getAllProfiles();

        // Create login boolean and chosen profile object
        boolean loginSuccess = false;
        Profile correctProfile = null;

        // Check if email is in system
        for (int i = 0; i < profileList.size(); i++) {
            String thisEmail = profileList.get(i).email;
            if (email.equalsIgnoreCase(thisEmail)) {
                correctProfile = profileList.get(i);
                break;
            }
        }

        // Check if username was found
        if (correctProfile != null && password.equals(correctProfile.password)) {
            loginSuccess = true;
        }

        // If the boolean is still false, then username or password is incorrect
        if (!loginSuccess) {
            Toast.makeText(this, "Email or password is incorrect. Try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // User has successfully logged in, save information
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); // 0 - for private mode
        SharedPreferences.Editor editor = settings.edit();

        // Set "hasLoggedIn" to true
        editor.putBoolean("hasLoggedIn", true);
        editor.putInt("id", correctProfile.profileId);

        // Commit the edits
        editor.apply();

        // Go to home screen
        Intent toMainActivity = new Intent(this, MainActivity.class);
        toMainActivity.putExtra("id", correctProfile.profileId);
        startActivity(toMainActivity);
    }

    private void register() {
        Intent toRegisterActivity = new Intent(this, RegisterActivity.class);
        startActivity(toRegisterActivity);
    }
}
