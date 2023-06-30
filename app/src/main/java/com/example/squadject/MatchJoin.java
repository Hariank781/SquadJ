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

public class MatchJoin extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private String joinKey;
    private DatabaseReference teamRef;
    private String keyForm;
    private DatabaseReference formTeamRef;
    private String keyJoin;
    private String phoneNumber;
    private Button redirectJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_join);
        redirectJoin=findViewById(R.id.redirectJoin);

        Intent intentJoin = getIntent();
        if (intentJoin != null && intentJoin.hasExtra("joinKey")) {
            joinKey = intentJoin.getStringExtra("joinKey");
        }

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

        teamRef = FirebaseDatabase.getInstance().getReference().child("Join_Team").child(joinKey);
        teamRef.child("Leader email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String teammateEmail = dataSnapshot.getValue(String.class);
                    if (teammateEmail != null) {
                        keyForm = teammateEmail.replace('.', ',');

                        // Retrieve leader email from Firebase
                        formTeamRef = FirebaseDatabase.getInstance().getReference().child("Form_Team").child(keyForm);
                        formTeamRef.child("Teammate email").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String leaderEmail = dataSnapshot.getValue(String.class);
                                    if (leaderEmail != null) {
                                        keyJoin = leaderEmail.replace('.', ',');

                                        // Check if joinKey is equal to keyJoin
                                        if (joinKey.equals(keyJoin)) {
                                            // Retrieve the Phone number from Form_Team
                                            formTeamRef.child("Phone number").addListenerForSingleValueEvent(new ValueEventListener() {
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
        redirectJoin.setOnClickListener(new View.OnClickListener() {
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