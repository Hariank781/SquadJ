package com.example.squadject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class login_register extends AppCompatActivity {

    private Button signInButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        // Initialize the buttons
        signInButton = findViewById(R.id.signIn_loginRegister);
        registerButton = findViewById(R.id.register_loginRegister);

        // Set onClickListeners for the buttons
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle sign in button click
                // Add your code here
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle register button click
                // Add your code here
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Do nothing to disable the back button
    }
}
