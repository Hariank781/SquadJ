package com.example.squadject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class RegisterForm2 extends AppCompatActivity {

    private ImageView profilePic;
    private Button uploadButton;
    private EditText needsText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form2);

        profilePic = findViewById(R.id.profilePic_registerForm2);
        uploadButton = findViewById(R.id.upload_registerForm2);
        needsText = findViewById(R.id.needsText_registerForm2);
        registerButton = findViewById(R.id.register_registerForm2);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement your logic for the upload button click
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement your logic for the register button click
                String needs = needsText.getText().toString();}
        });
    }
}
