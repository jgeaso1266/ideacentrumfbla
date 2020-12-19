package org.fbla.geason.ideacentrum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static org.fbla.geason.ideacentrum.LoginActivity.PREFS_NAME;
import static org.fbla.geason.ideacentrum.LoginActivity.database;

public class NewCentroidActivity extends AppCompatActivity {
    EditText newCentroidTitle;
    TextView chooseImageText;
    ImageView newCentroidImage;
    EditText newCentroidProblem;
    EditText newCentroidIdea;
    EditText newCentroidNeed;
    FloatingActionButton saveButton;
    public static final int PICK_IMAGE = 1;
    Uri newImage;
    int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_centroid);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        id = settings.getInt("id", 0);

        // Set nav bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        Intent toCentroidActivity = new Intent(NewCentroidActivity.this, NewCentroidActivity.class);
                        startActivity(toCentroidActivity);
                        break;
                    case R.id.action_home:
                        Intent toMainActivity = new Intent(NewCentroidActivity.this, MainActivity.class);
                        startActivity(toMainActivity);
                        break;
                    case R.id.action_profile:
                        Intent toProfileActivity = new Intent(NewCentroidActivity.this, ProfileActivity.class);
                        startActivity(toProfileActivity);
                        break;
                }
                return true;
            }
        });

        // Set all variables
        newCentroidTitle = findViewById(R.id.new_centroid_title);
        chooseImageText = findViewById(R.id.choose_image_text);
        newCentroidImage = findViewById(R.id.new_centroid_image);
        newCentroidProblem = findViewById(R.id.new_centroid_problem);
        newCentroidIdea = findViewById(R.id.new_centroid_idea);
        newCentroidNeed = findViewById(R.id.new_centroid_need);
        saveButton = findViewById(R.id.save_button);

        newCentroidImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Choose an image
                chooseImage();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the new Centroid
                save();
            }
        });
    }

    private void chooseImage() {

        // Pick an image
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void save() {

        // Get all input elements, create a Centroid object, and add to database
        String saveTitle = newCentroidTitle.getText().toString();
        String saveProblem = newCentroidProblem.getText().toString();
        String saveIdea = newCentroidIdea.getText().toString();
        String saveNeed = newCentroidNeed.getText().toString();

        // Check if image is null
        String saveImage = null;
        if (newImage != null) {
            saveImage = newImage.toString();
        }

        Centroid saveCentroid = new Centroid(id, saveTitle, saveImage, saveProblem, saveIdea, saveNeed);

        database.centroidDao().insertCentroid(saveCentroid);

        Intent toMainActivity = new Intent(NewCentroidActivity.this, MainActivity.class);
        startActivity(toMainActivity);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && requestCode == PICK_IMAGE) {

            // Get URI from image and put it in the ImageView
            newImage = data.getData();

            getContentResolver().takePersistableUriPermission(newImage, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            newCentroidImage.setImageURI(newImage);
            chooseImageText.setVisibility(View.INVISIBLE);
        }
    }
}
