package com.example.squadject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MatchForm extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private String formKey;
    private DatabaseReference teamRef;
    private String keyJoin;
    private DatabaseReference joinTeamRef;
    private String keyForm;
    private String phoneNumber;
    private Button redirectForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_form);
        redirectForm=findViewById(R.id.redirectForm);

        Intent intentForm = getIntent();
        if (intentForm != null && intentForm.hasExtra("formKey")) {
            formKey = intentForm.getStringExtra("formKey");
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_matches);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_matches) {
                return true;
            } else if (item.getItemId() == R.id.menu_home) {
                startActivity(new Intent(MatchForm.this, HomeForm.class));
                return true;
            } else if (item.getItemId() == R.id.menu_logout) {
                startActivity(new Intent(MatchForm.this, login_register.class));
                return true;
            } else {
                return false;
            }
        });

        teamRef = FirebaseDatabase.getInstance().getReference().child("Form_Team").child(formKey);
        teamRef.child("Teammate email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String teammateEmail = dataSnapshot.getValue(String.class);
                    if (teammateEmail != null) {
                        keyJoin = teammateEmail.replace('.', ',');

                        // Retrieve leader email from Firebase
                        joinTeamRef = FirebaseDatabase.getInstance().getReference().child("Join_Team").child(keyJoin);
                        joinTeamRef.child("Leader email").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String leaderEmail = dataSnapshot.getValue(String.class);
                                    if (leaderEmail != null) {
                                        keyForm = leaderEmail.replace('.', ',');

                                        // Check if formKey is equal to keyForm
                                        if (formKey.equals(keyForm)) {
                                            // Retrieve the Phone number from Join_Team
                                            joinTeamRef.child("Phone number").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        phoneNumber = dataSnapshot.getValue(String.class);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    // Handle database error if needed
                                                }
                                            });
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Handle database error if needed
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
        redirectForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add your code here to handle the button click
                if (phoneNumber != null) {
                    // Redirect to WhatsApp chat
                    String url = "https://api.whatsapp.com/send?phone=+91" + phoneNumber;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        // Do nothing to disable the back button
    }
}