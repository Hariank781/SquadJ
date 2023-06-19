package com.example.squadject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                startActivity(new Intent(login_register.this, Login.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login_register.this, Choose.class));
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
