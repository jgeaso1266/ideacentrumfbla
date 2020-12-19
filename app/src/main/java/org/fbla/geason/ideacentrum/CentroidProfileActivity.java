package org.fbla.geason.ideacentrum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static org.fbla.geason.ideacentrum.LoginActivity.PREFS_NAME;
import static org.fbla.geason.ideacentrum.LoginActivity.database;

public class CentroidProfileActivity extends AppCompatActivity {
    ImageView profileImage;
    Button profileImageChooser;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText phone;
    EditText about;
    EditText education;
    EditText experience;
    Uri profileImageUri;
    FloatingActionButton profileSave;
    Profile thisProfile;
    int id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centroid_profile);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        id = settings.getInt("id", 0);

        thisProfile = database.profileDao().getProfile(id);

        // Set nav bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        Intent toCentroidActivity = new Intent(CentroidProfileActivity.this, NewCentroidActivity.class);
                        startActivity(toCentroidActivity);
                        break;
                    case R.id.action_home:
                        Intent toMainActivity = new Intent(CentroidProfileActivity.this, MainActivity.class);
                        startActivity(toMainActivity);
                        break;
                    case R.id.action_profile:
                        Intent toProfileActivity = new Intent(CentroidProfileActivity.this, ProfileActivity.class);
                        startActivity(toProfileActivity);
                        break;
                }
                return true;
            }
        });

        // Set all elements
        profileImage = findViewById(R.id.profile_image);

        if (thisProfile.imageUri != null) {
            profileImageUri = Uri.parse(thisProfile.imageUri);
            profileImage.setImageURI(profileImageUri);
        }

        profileImageChooser = findViewById(R.id.profile_image_chooser);
        firstName = findViewById(R.id.profile_firstname);
        firstName.setText(thisProfile.firstName);
        lastName = findViewById(R.id.profile_lastname);
        lastName.setText(thisProfile.lastName);
        email = findViewById(R.id.profile_email);
        email.setText(thisProfile.email);
        phone = findViewById(R.id.profile_phone);
        phone.setText(thisProfile.phone);
        about = findViewById(R.id.about_input);
        about.setText(thisProfile.about);
        education = findViewById(R.id.education_input);
        education.setText(thisProfile.education);
        experience = findViewById(R.id.experience_input);
        experience.setText(thisProfile.experience);
        profileSave = findViewById(R.id.profile_save);
    }
}
