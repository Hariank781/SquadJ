package com.example.squadject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class RegisterJoin2 extends AppCompatActivity {

    private ImageView profilePic;
    private Button uploadButton;
    private EditText skillsText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_join2);

        profilePic = findViewById(R.id.profilePic_registerJoin2);
        uploadButton = findViewById(R.id.upload_registerJoin2);
        skillsText = findViewById(R.id.skillsText_registerJoin2);
        registerButton = findViewById(R.id.register_registerJoin2);

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
                String needs = skillsText.getText().toString();}
        });
    }
}
