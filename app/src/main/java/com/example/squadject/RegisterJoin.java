package com.example.squadject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterJoin extends AppCompatActivity {
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repasswordEditText;
    private EditText collegeEditText;
    private EditText semesterEditText;
    private EditText branchEditText;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_join);

        // Initialize EditTexts
        nameEditText = findViewById(R.id.nameText_registerJoin);
        emailEditText = findViewById(R.id.emailText_registerJoin);
        passwordEditText = findViewById(R.id.passwordText_registerJoin);
        repasswordEditText = findViewById(R.id.repasswordText_registerJoin);
        collegeEditText = findViewById(R.id.collegeText_registerJoin);
        semesterEditText = findViewById(R.id.semesterText_registerJoin);
        branchEditText = findViewById(R.id.branchText_registerJoin);

        // Initialize Next button
        nextButton = findViewById(R.id.next_registerJoin);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on button click
                // Add your code logic here
            }
        });
    }
}