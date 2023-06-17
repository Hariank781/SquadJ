package com.example.squadject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Password_Change extends AppCompatActivity {

    private EditText passwordEditText;
    private EditText reenterPasswordEditText;
    private Button generateOtpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        // Initialize views
        passwordEditText = findViewById(R.id.passwordText_change);
        reenterPasswordEditText = findViewById(R.id.repasswordText_change);
        generateOtpButton = findViewById(R.id.generateOtp_change);

        // Set OnClickListener for the generateOtpButton
        generateOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action when the button is clicked
                // Replace this with your desired action
                String password = passwordEditText.getText().toString();
                String reenterPassword = reenterPasswordEditText.getText().toString();

                // Add your code logic here
            }
        });
    }
}
