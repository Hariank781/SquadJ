package com.example.squadject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MatchJoin extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_join);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_matches);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_matches) {
                return true;
            } else if (item.getItemId() == R.id.menu_home) {
                startActivity(new Intent(MatchJoin.this, HomeJoin.class));
                return true;
            } else if (item.getItemId() == R.id.menu_logout) {
                startActivity(new Intent(MatchJoin.this, login_register.class));
                return true;
            } else {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Do nothing to disable the back button
    }
}