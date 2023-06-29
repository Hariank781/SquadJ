package com.example.squadject;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeJoin extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    private RecyclerView recyclerView;

    private JoinAdapter joinAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_join);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_matches) {
                startActivity(new Intent(HomeJoin.this, MatchJoin.class));
                return true;
            } else if (item.getItemId() == R.id.menu_home) {
                return true;
            } else if (item.getItemId() == R.id.menu_logout) {
                startActivity(new Intent(HomeJoin.this, login_register.class));
                return true;
            } else {
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerJoin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load data from Firebase
        loadJoinDataFromFirebase();
    }

    private void loadJoinDataFromFirebase() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Form_Team");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<JoinItem> joinItems = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String fullName = snapshot.child("Full name").getValue(String.class);
                    String phoneNumber = snapshot.child("Phone number").getValue(String.class);
                    String email = snapshot.child("Email").getValue(String.class);
                    String college = snapshot.child("College").getValue(String.class);
                    String semester = snapshot.child("Semester").getValue(String.class);
                    String branch = snapshot.child("Branch").getValue(String.class);
                    String skills = snapshot.child("Skills needed for the project").getValue(String.class);
                    String profilePicture = snapshot.child("Profile picture").getValue(String.class);

                    JoinItem joinItem = new JoinItem(profilePicture, fullName, email, phoneNumber, college, branch, semester, skills);
                    joinItems.add(joinItem);
                }
                joinAdapter = new JoinAdapter(joinItems);
                recyclerView.setAdapter(joinAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
    }


    @Override
    public void onBackPressed() {
        // Do nothing to disable the back button
    }
}