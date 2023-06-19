package com.example.squadject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Choose extends AppCompatActivity {
    private Button formTeamButton;
    private Button joinTeamButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        formTeamButton = findViewById(R.id.formTeam_choose);
        joinTeamButton = findViewById(R.id.joinTeam_choose);

        formTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Choose.this, RegisterForm.class));
            }
        });

        joinTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Choose.this, RegisterJoin.class));
            }
        });
    }
}
