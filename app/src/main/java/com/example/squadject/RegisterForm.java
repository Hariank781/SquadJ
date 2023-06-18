package com.example.squadject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterForm extends AppCompatActivity {

    private EditText skillsText;
    private EditText collegeText;
    private EditText semesterText;
    private EditText branchText;
    private EditText repasswordText;
    private EditText passwordText;
    private EditText phoneText;
    private EditText emailText;
    private EditText nameText;
    private Button uploadButton;
    private Button registerButton;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize the Firebase Realtime Database reference
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Form_Team");

        skillsText = findViewById(R.id.skillsText_registerForm);
        collegeText = findViewById(R.id.collegeText_registerForm);
        semesterText = findViewById(R.id.semesterText_registerForm);
        branchText = findViewById(R.id.branchText_registerForm);
        repasswordText = findViewById(R.id.repasswordText_registerForm);
        passwordText = findViewById(R.id.passwordText_registerForm);
        phoneText = findViewById(R.id.phoneText_registerForm);
        emailText = findViewById(R.id.emailText_registerForm);
        nameText = findViewById(R.id.nameText_registerForm);
        uploadButton = findViewById(R.id.upload_registerForm);
        registerButton = findViewById(R.id.register_registerForm);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your logic here for the upload button click
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = nameText.getText().toString();
                String phoneNumber = phoneText.getText().toString();
                String email = emailText.getText().toString();
                String college = collegeText.getText().toString();
                String semester = semesterText.getText().toString();
                String branch = branchText.getText().toString();
                String skills = skillsText.getText().toString();
                String password = passwordText.getText().toString();
                String rePassword = repasswordText.getText().toString();

                // Check if all fields are filled
                if (fullName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || college.isEmpty()
                        || semester.isEmpty() || branch.isEmpty() || skills.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                    Toast.makeText(RegisterForm.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if password and rePassword match
                if (!password.equals(rePassword)) {
                    Toast.makeText(RegisterForm.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if semester is valid
                if (!isValidSemester(semester)) {
                    Toast.makeText(RegisterForm.this, "Invalid semester", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if branch is valid
                if (!isValidBranch(branch)) {
                    Toast.makeText(RegisterForm.this, "Invalid branch", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if email is in the correct format
                if (!isValidEmail(email)) {
                    Toast.makeText(RegisterForm.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if password is in the correct format
                if (!isValidPassword(password)) {
                    Toast.makeText(RegisterForm.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if phone number is exactly 10 digits long
                if (!isValidPhoneNumber(phoneNumber)) {
                    Toast.makeText(RegisterForm.this, "Phone number must be exactly 10 digits long", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if phone number is unique in the database
                checkPhoneNumberUniqueness(phoneNumber, fullName, email, college, semester, branch, skills, password);
            }
        });
    }

    private boolean isValidSemester(String semester) {
        return semester.matches("[1-8]");
    }

    private boolean isValidBranch(String branch) {
        return branch.matches("CSE|ISE|ECE|MECH");
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        // Add your password format validation logic here
        // For example, you can check if the password meets certain criteria (e.g., minimum length, contains uppercase, lowercase, and numbers)
        // Return true if the password is valid, otherwise return false
        return password.length() >= 8;  // Example: Password should have at least 8 characters
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.length() == 10;
    }

    private void checkPhoneNumberUniqueness(final String phoneNumber, final String fullName, final String email, final String college,
                                            final String semester, final String branch, final String skills, final String password) {
        databaseRef.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Phone number already exists in the database, show error toast
                    Toast.makeText(RegisterForm.this, "Phone number already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Phone number does not exist, proceed with registration
                    registerUser(email, password, phoneNumber, fullName, college, semester, branch, skills);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error occurred while checking phone number uniqueness
                Toast.makeText(RegisterForm.this, "Error checking phone number uniqueness", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(String email, String password, final String phoneNumber, final String fullName, final String college,
                              final String semester, final String branch, final String skills) {
        // Check if the email already exists in Firebase Authentication
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                    SignInMethodQueryResult result = task.getResult();
                    if (result != null && result.getSignInMethods() != null && result.getSignInMethods().size() > 0) {
                        // Email already exists, show error toast
                        Toast.makeText(RegisterForm.this, "Email already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        // Email does not exist, proceed with registration
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(RegisterForm.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // User registration successful
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            addUserToDatabase(user, phoneNumber, fullName, email, college, semester, branch, skills);
                                        } else {
                                            // User registration failed
                                            Toast.makeText(RegisterForm.this, "User registration failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } else {
                    // Error occurred while checking email existence
                    Toast.makeText(RegisterForm.this, "Error checking email existence", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addUserToDatabase(FirebaseUser user, String phoneNumber, String fullName, String email, String college,
                                   String semester, String branch, String skills) {
        DatabaseReference userRef = databaseRef.child(phoneNumber);

        // Set the registration data as key-value pairs under the userRef
        userRef.child("Full_Name").setValue(fullName);
        userRef.child("Phone_Number").setValue(phoneNumber);
        userRef.child("Email").setValue(email);
        userRef.child("College").setValue(college);
        userRef.child("Semester").setValue(semester);
        userRef.child("Branch").setValue(branch);
        userRef.child("Skills").setValue(skills);

        // Optional: Display a success toast message
        Toast.makeText(RegisterForm.this, "User registration successful", Toast.LENGTH_SHORT).show();
    }
}