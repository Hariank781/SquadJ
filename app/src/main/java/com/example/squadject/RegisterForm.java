package com.example.squadject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterForm extends AppCompatActivity {
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
        setContentView(R.layout.activity_register_form);

        // Initialize EditTexts
        nameEditText = findViewById(R.id.nameText_registerForm);
        emailEditText = findViewById(R.id.emailText_registerForm);
        passwordEditText = findViewById(R.id.passwordText_registerForm);
        repasswordEditText = findViewById(R.id.repasswordText_registerForm);
        collegeEditText = findViewById(R.id.collegeText_registerForm);
        semesterEditText = findViewById(R.id.semesterText_registerForm);
        branchEditText = findViewById(R.id.branchText_registerForm);

        // Initialize Next button
        nextButton = findViewById(R.id.next_registerForm);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on button click
                // Add your code logic here
            }
        });
    }
}