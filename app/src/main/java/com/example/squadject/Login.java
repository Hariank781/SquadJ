package com.example.squadject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText emailText;
    private EditText passwordText;
    private Button forgotPasswordButton;
    private Button formTeamButton;
    private Button joinTeamButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

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
                formTeam();
            }
        });

        // Set OnClickListener for join team button
        joinTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinTeam();
            }
        });
    }

    private void formTeam() {
        // Get the entered email and password
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // Validate the email and password
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(Login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sign in the user with the provided email and password
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User login successful
                            Toast.makeText(Login.this, "User login successful", Toast.LENGTH_SHORT).show();

                            // Redirect to the form team activity or perform further actions
                            // For example, you can start a new activity:
                            startActivity(new Intent(Login.this, HomeForm.class));
                        } else {
                            // User login failed
                            Toast.makeText(Login.this, "User login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void joinTeam() {
        // Get the entered email and password
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // Validate the email and password
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(Login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user with the provided email and password
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User registration successful
                            Toast.makeText(Login.this, "User login successful", Toast.LENGTH_SHORT).show();

                            // Redirect to the join team activity or perform further actions
                            // For example, you can start a new activity:
                            startActivity(new Intent(Login.this, HomeJoin.class));
                        } else {
                            // User registration failed
                            Toast.makeText(Login.this, "User login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}