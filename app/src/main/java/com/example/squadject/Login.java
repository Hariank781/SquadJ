package com.example.squadject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    private EditText emailText;
    private EditText passwordText;
    private Button forgotPasswordButton;
    private Button formTeamButton;
    private Button joinTeamButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        emailText = findViewById(R.id.emailText_login);
        passwordText = findViewById(R.id.passwordText_login);
        forgotPasswordButton = findViewById(R.id.forgotPassword_login);
        formTeamButton = findViewById(R.id.formTeam_login);
        joinTeamButton = findViewById(R.id.joinTeam_login);

        // Set OnClickListener for forgot password button
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle forgot password button click here
            }
        });

        // Set OnClickListener for form team button
        formTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle form team button click here
            }
        });

        // Set OnClickListener for join team button
        joinTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle join team button click here
            }
        });
    }
}
