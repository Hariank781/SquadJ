package com.example.squadject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yalantis.ucrop.UCrop;

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
    private ImageView profilePicImageView;

    private Uri profilePicUri;

    private static final int PICK_IMAGE_REQUEST = 1;


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
        profilePicImageView = findViewById(R.id.profilePic_registerForm);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
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
        return password.length() >= 8;
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
                                            addUserToDatabase(user, phoneNumber, fullName, email, college, semester, branch, skills, profilePicUri);
                                            startActivity(new Intent(RegisterForm.this, HomeForm.class));
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
                                   String semester, String branch, String skills, Uri profilePicUri) {
        DatabaseReference userRef = databaseRef.child(phoneNumber);

        // Set the registration data as key-value pairs under the userRef
        userRef.child("Full name").setValue(fullName);
        userRef.child("Phone number").setValue(phoneNumber);
        userRef.child("Email").setValue(email);
        userRef.child("College").setValue(college);
        userRef.child("Semester").setValue(semester);
        userRef.child("Branch").setValue(branch);
        userRef.child("Skills needed for the project").setValue(skills);

        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("profile_pictures")
                .child(user.getUid() + ".png");

        profilePicRef.putFile(profilePicUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the profile picture download URL
                    profilePicRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String profilePicUrl = uri.toString();
                        userRef.child("Profile picture").setValue(profilePicUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Profile picture upload failed
                    Toast.makeText(RegisterForm.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                });
        // Optional: Display a success toast message
        Toast.makeText(RegisterForm.this, "User registration successful", Toast.LENGTH_SHORT).show();
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            profilePicUri = selectedImageUri;

            // Start the image cropping activity
            UCrop.Options options = new UCrop.Options();
            options.setToolbarColor(ContextCompat.getColor(this, R.color.black));
            options.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
            options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.white));
            options.setCompressionFormat(Bitmap.CompressFormat.PNG);
            options.setCompressionQuality(90);
            options.setCircleDimmedLayer(true);
            options.setShowCropGrid(true);

            UCrop uCrop = UCrop.of(selectedImageUri, Uri.fromFile(new File(getCacheDir(), "cropped_image")));
            uCrop.withOptions(options);
            uCrop.start(RegisterForm.this);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK && data != null) {
            Uri croppedImageUri = UCrop.getOutput(data);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), croppedImageUri);
                profilePicImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == UCrop.RESULT_ERROR && data != null) {
            Throwable cropError = UCrop.getError(data);
            Toast.makeText(this, "Image cropping failed: " + cropError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}