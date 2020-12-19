package org.fbla.geason.ideacentrum;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

import static org.fbla.geason.ideacentrum.LoginActivity.PREFS_NAME;
import static org.fbla.geason.ideacentrum.LoginActivity.database;

public class CentroidActivity extends AppCompatActivity {
    TextView centroidTitle;
    ImageView centroidImage;
    TextView centroidProblem;
    TextView centroidIdea;
    TextView centroidNeed;
    LinearLayout profile;
    CircleImageView centroidProfileImage;
    TextView profileUsername;
    Button centroidShareButton;
    Button centroidDeleteButton;
    Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centroid);

        // Get id from intent and get centroid from id
        int id = getIntent().getIntExtra("centroid id", 0);

        final Centroid thisCentroid = database.centroidDao().getCentroid(id);
        final Profile thisProfile = database.profileDao().getProfile(thisCentroid.profileId);

        // Set nav bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        Intent toCentroidActivity = new Intent(CentroidActivity.this, NewCentroidActivity.class);
                        startActivity(toCentroidActivity);
                        break;
                    case R.id.action_home:
                        Intent toMainActivity = new Intent(CentroidActivity.this, MainActivity.class);
                        startActivity(toMainActivity);
                        break;
                    case R.id.action_profile:
                        Intent toProfileActivity = new Intent(CentroidActivity.this, ProfileActivity.class);
                        startActivity(toProfileActivity);
                        break;
                }
                return true;
            }
        });

        // Set all elements
        centroidTitle = findViewById(R.id.centroid_title); centroidTitle.setText(thisCentroid.title);
        centroidImage = findViewById(R.id.centroid_image);
        centroidProblem = findViewById(R.id.centroid_problem); centroidProblem.setText(thisCentroid.problem);
        centroidIdea = findViewById(R.id.centroid_idea); centroidIdea.setText(thisCentroid.idea);
        centroidNeed = findViewById(R.id.centroid_need); centroidNeed.setText(thisCentroid.need);

        // Determine whether to show image
        if (thisCentroid.fileUri == null) {
            centroidImage.setVisibility(View.INVISIBLE);
            centroidImage.getLayoutParams().height = 0;
        } else {
            imageUri = Uri.parse(thisCentroid.fileUri);
            centroidImage.setImageURI(imageUri);
        }

        profile = findViewById(R.id.centroid_profile);
        centroidProfileImage = findViewById(R.id.centroid_profile_image);
        profileUsername = findViewById(R.id.centroid_profile_username);
        centroidShareButton = findViewById(R.id.centroid_share_button);
        centroidDeleteButton = findViewById(R.id.centroid_delete_button);

        // Check if delete button should be shown
        Profile userProfile = database.profileDao().getProfile(getSharedPreferences(PREFS_NAME, 0).getInt("id", 0));
        if ( userProfile.profileId == thisProfile.profileId) {
            centroidDeleteButton.setVisibility(View.VISIBLE);
        }

        centroidShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Go to sharing method
                centroidShare();
            }
        });

        centroidDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the Centroid from the database and go back to Main Activity
                database.centroidDao().deleteCentroid(thisCentroid);

                Intent toMainActivity = new Intent(CentroidActivity.this, MainActivity.class);
                startActivity(toMainActivity);
            }
        });

        if (thisProfile.imageUri != null) {
            centroidProfileImage.setImageURI(Uri.parse(thisProfile.imageUri));
        }

        String profileName = String.format(getResources().getString(R.string.profile_name), thisProfile.firstName, thisProfile.lastName);
        profileUsername.setText(profileName);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCentroidProfileActivity = new Intent(CentroidActivity.this, CentroidProfileActivity.class);
                toCentroidProfileActivity.putExtra("profile id", thisProfile.profileId);
                startActivity(toCentroidProfileActivity);
            }
        });
    }

    private void centroidShare() {

        // Create intent to send title, idea, and photo to any app
        Intent tweet = new Intent(Intent.ACTION_SEND);
        tweet.setType("*/*");

        if (imageUri != null) {
            tweet.putExtra(Intent.EXTRA_STREAM, imageUri);
        }
        tweet.putExtra(Intent.EXTRA_TITLE, centroidTitle.getText().toString());
        tweet.putExtra(Intent.EXTRA_TEXT, String.format(getResources().getString(R.string.centroid_share), centroidProblem.getText().toString()));
        startActivity(Intent.createChooser(tweet, "Share this via"));
    }
}
