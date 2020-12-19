package org.fbla.geason.ideacentrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static org.fbla.geason.ideacentrum.LoginActivity.PREFS_NAME;
import static org.fbla.geason.ideacentrum.LoginActivity.database;

public class MainActivity extends AppCompatActivity {
    RecyclerView nodeView;
    StaggeredGridLayoutManager mLayoutManager;
    CentroidAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set nav bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        Intent toNewCentroidActivity = new Intent(MainActivity.this, NewCentroidActivity.class);
                        startActivity(toNewCentroidActivity);
                        break;
                    case R.id.action_home:
                        Intent toMainActivity = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(toMainActivity);
                        break;
                    case R.id.action_profile:
                        Intent toProfileActivity = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(toProfileActivity);
                        break;
                }
                return true;
            }
        });

        // Set up recyclerview
        nodeView = findViewById(R.id.nodeView);
        mAdapter = new CentroidAdapter(this);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        nodeView.setAdapter(mAdapter);
        nodeView.setLayoutManager(mLayoutManager);

        // Reload adapter
        mAdapter.reload();
    }
}
