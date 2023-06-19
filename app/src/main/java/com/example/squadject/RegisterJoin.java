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

public class RegisterJoin extends AppCompatActivity {

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
        setContentView(R.layout.activity_register_join);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize the Firebase Realtime Database reference
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Join_Team");

        skillsText = findViewById(R.id.skillsText_registerJoin);
        collegeText = findViewById(R.id.collegeText_registerJoin);
        semesterText = findViewById(R.id.semesterText_registerJoin);
        branchText = findViewById(R.id.branchText_registerJoin);
        repasswordText = findViewById(R.id.repasswordText_registerJoin);
        passwordText = findViewById(R.id.passwordText_registerJoin);
        phoneText = findViewById(R.id.phoneText_registerJoin);
        emailText = findViewById(R.id.emailText_registerJoin);
        nameText = findViewById(R.id.nameText_registerJoin);
        uploadButton = findViewById(R.id.upload_registerJoin);
        registerButton = findViewById(R.id.register_registerJoin);

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
                    Toast.makeText(RegisterJoin.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if password and rePassword match
                if (!password.equals(rePassword)) {
                    Toast.makeText(RegisterJoin.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if semester is valid
                if (!isValidSemester(semester)) {
                    Toast.makeText(RegisterJoin.this, "Invalid semester", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if branch is valid
                if (!isValidBranch(branch)) {
                    Toast.makeText(RegisterJoin.this, "Invalid branch", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if email is in the correct format
                if (!isValidEmail(email)) {
                    Toast.makeText(RegisterJoin.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if password is in the correct format
                if (!isValidPassword(password)) {
                    Toast.makeText(RegisterJoin.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if phone number is exactly 10 digits long
                if (!isValidPhoneNumber(phoneNumber)) {
                    Toast.makeText(RegisterJoin.this, "Phone number must be exactly 10 digits long", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RegisterJoin.this, "Phone number already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Phone number does not exist, proceed with registration
                    registerUser(email, password, phoneNumber, fullName, college, semester, branch, skills);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error occurred while checking phone number uniqueness
                Toast.makeText(RegisterJoin.this, "Error checking phone number uniqueness", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RegisterJoin.this, "Email already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        // Email does not exist, proceed with registration
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(RegisterJoin.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // User registration successful
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            addUserToDatabase(user, phoneNumber, fullName, email, college, semester, branch, skills);
                                        } else {
                                            // User registration failed
                                            Toast.makeText(RegisterJoin.this, "User registration failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } else {
                    // Error occurred while checking email existence
                    Toast.makeText(RegisterJoin.this, "Error checking email existence", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addUserToDatabase(FirebaseUser user, String phoneNumber, String fullName, String email, String college,
                                   String semester, String branch, String skills) {
        DatabaseReference userRef = databaseRef.child(phoneNumber);

        // Set the registration data as key-value pairs under the userRef
        userRef.child("Full name").setValue(fullName);
        userRef.child("Phone number").setValue(phoneNumber);
        userRef.child("Email").setValue(email);
        userRef.child("College").setValue(college);
        userRef.child("Semester").setValue(semester);
        userRef.child("Branch").setValue(branch);
        userRef.child("Skills").setValue(skills);

        // Optional: Display a success toast message
        Toast.makeText(RegisterJoin.this, "User registration successful", Toast.LENGTH_SHORT).show();
    }
}