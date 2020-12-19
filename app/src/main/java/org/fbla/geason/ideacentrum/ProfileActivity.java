package org.fbla.geason.ideacentrum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static org.fbla.geason.ideacentrum.LoginActivity.PREFS_NAME;
import static org.fbla.geason.ideacentrum.LoginActivity.database;
import static org.fbla.geason.ideacentrum.NewCentroidActivity.PICK_IMAGE;

public class ProfileActivity extends AppCompatActivity {
    ImageButton appSettings;
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
    public static List<String> bugList = new ArrayList<>();
    RecyclerView myCentroids;
    MyCentroidsAdapter adapter;
    StaggeredGridLayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
                        Intent toCentroidActivity = new Intent(ProfileActivity.this, NewCentroidActivity.class);
                        startActivity(toCentroidActivity);
                        break;
                    case R.id.action_home:
                        Intent toMainActivity = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(toMainActivity);
                        break;
                    case R.id.action_profile:
                        Intent toProfileActivity = new Intent(ProfileActivity.this, ProfileActivity.class);
                        startActivity(toProfileActivity);
                        break;
                }
                return true;
            }
        });

        // Set all elements
        appSettings = findViewById(R.id.app_settings);
        profileImage = findViewById(R.id.profile_image);

        if (thisProfile.imageUri != null) {
            profileImageUri = Uri.parse(thisProfile.imageUri);
            profileImage.setImageURI(profileImageUri);
        }

        profileImageChooser = findViewById(R.id.profile_image_chooser);
        firstName = findViewById(R.id.profile_firstname); firstName.setText(thisProfile.firstName);
        lastName = findViewById(R.id.profile_lastname); lastName.setText(thisProfile.lastName);
        email = findViewById(R.id.profile_email); email.setText(thisProfile.email);
        phone = findViewById(R.id.profile_phone); phone.setText(thisProfile.phone);
        about = findViewById(R.id.about_input); about.setText(thisProfile.about);
        education = findViewById(R.id.education_input); education.setText(thisProfile.education);
        experience = findViewById(R.id.experience_input); experience.setText(thisProfile.experience);
        profileSave = findViewById(R.id.profile_save);

        profileImageChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileChooseImage();
            }
        });

        profileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileSave();
            }
        });

        appSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMenu(v);
            }
        });

        // Set up recyclerview
        myCentroids = findViewById(R.id.my_centroids_view);
        adapter = new MyCentroidsAdapter(this);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        myCentroids.setAdapter(adapter);
        myCentroids.setLayoutManager(layoutManager);

        // Reload adapter
        adapter.reload(id);
    }

    private void profileChooseImage() {

        // Pick an image
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void profileSave() {

        // Get text from elements and update database
        String saveFirstname = firstName.getText().toString();
        String saveLastname = lastName.getText().toString();
        String saveEmail = email.getText().toString();
        String savePhone = phone.getText().toString();
        String saveAbout = about.getText().toString();
        String saveEducation = education.getText().toString();
        String saveExperience = experience.getText().toString();

        // Check if image is null
        String saveProfileImage = null;
        if (profileImageUri != null) {
            saveProfileImage = profileImageUri.toString();
        }

        database.profileDao().updateProfile(saveProfileImage, saveFirstname, saveLastname, saveEmail, savePhone,
                saveAbout, saveEducation, saveExperience, id);

        Intent toMainActivity = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(toMainActivity);
    }

    private void createMenu(View v) {
        final PopupMenu popup = new PopupMenu(ProfileActivity.this, v);
        //inflating menu from xml resource
        popup.inflate(R.menu.settings);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu1:
                        final View bugView = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.report_bug_alert, null);
                        final android.app.AlertDialog bugAlert = new android.app.AlertDialog.Builder(ProfileActivity.this).create();

                        bugAlert.setTitle("Report a bug");
                        bugAlert.setMessage("Please type more information about the bug");
                        bugAlert.setCancelable(true);
                        bugAlert.setView(bugView);

                        final EditText bug = bugView.findViewById(R.id.bug_report);

                        bugAlert.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "Rename", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String bugText = bug.getText().toString();
                                bugList.add(bugText);
                            }
                        });

                        bugAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bugAlert.dismiss();
                            }
                        });

                        bugAlert.show();

                        return true;
                }
                return false;
            }
        });

        popup.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && requestCode == PICK_IMAGE) {

            // Get URI from image and put it in the ImageView
            profileImageUri = data.getData();

            getContentResolver().takePersistableUriPermission(profileImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            profileImage.setImageURI(profileImageUri);
        }
    }
}
