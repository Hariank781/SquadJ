package com.example.squadject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PhoneNoForm extends AppCompatActivity {

    private EditText phoneEditText;
    private Button generateOtpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_no_form);

        // Initialize EditText and Button
        phoneEditText = findViewById(R.id.phoneText_phoneForm);
        generateOtpButton = findViewById(R.id.generateOtp_phoneForm);

        // Set OnClickListener for the button
        generateOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click event here
                String phoneNumber = phoneEditText.getText().toString();
                // Do something with the phone number
            }
        });
    }
}