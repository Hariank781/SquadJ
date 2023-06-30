package com.example.squadject;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeForm extends AppCompatActivity implements FormAdapter.OnInviteClickListener {
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private FormAdapter formAdapter;
    private String emailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_form);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("emailID")) {
            emailID = intent.getStringExtra("emailID");
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_matches) {
                Intent intentForm = new Intent(HomeForm.this, MatchForm.class);
                intentForm.putExtra("formKey", emailID);
                startActivity(intentForm);
                return true;
            } else if (item.getItemId() == R.id.menu_home) {
                return true;
            } else if (item.getItemId() == R.id.menu_logout) {
                startActivity(new Intent(HomeForm.this, login_register.class));
                return true;
            } else {
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerForm);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load data from Firebase
        loadFormDataFromFirebase();
    }

    private void loadFormDataFromFirebase() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Join_Team");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<FormItem> formItems = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String fullName = snapshot.child("Full name").getValue(String.class);
                    String phoneNumber = snapshot.child("Phone number").getValue(String.class);
                    String email = snapshot.child("Email").getValue(String.class);
                    String college = snapshot.child("College").getValue(String.class);
                    String semester = snapshot.child("Semester").getValue(String.class);
                    String branch = snapshot.child("Branch").getValue(String.class);
                    String skills = snapshot.child("Skills").getValue(String.class);
                    String profilePicture = snapshot.child("Profile picture").getValue(String.class);

                    FormItem formItem = new FormItem(profilePicture, fullName, email, phoneNumber, college, branch, semester, skills);
                    formItems.add(formItem);
                }
                formAdapter = new FormAdapter(formItems, HomeForm.this);
                recyclerView.setAdapter(formAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
    }

    @Override
    public void onInviteClick(String email) {
        storeEmailInFirebase(email);
    }

    private void storeEmailInFirebase(String email) {
        DatabaseReference teamRef = FirebaseDatabase.getInstance().getReference().child("Form_Team").child(emailID);
        teamRef.child("Teammate email").setValue(email)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(HomeForm.this, "Email stored in Firebase", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeForm.this, "Failed to store email", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onBackPressed() {
        // Do nothing to disable the back button
    }
}