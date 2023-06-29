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
    private ImageView profilePicImageView;

    private Uri profilePicUri;

    private static final int PICK_IMAGE_REQUEST = 1;


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
        profilePicImageView = findViewById(R.id.profilePic_registerJoin);

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
                String email = emailText.getText().toString();
                String college = collegeText.getText().toString();
                String semester = semesterText.getText().toString();
                String branch = branchText.getText().toString();
                String skills = skillsText.getText().toString();
                String password = passwordText.getText().toString();
                String rePassword = repasswordText.getText().toString();

                // Check if all fields are filled
                if (fullName.isEmpty() || email.isEmpty() || college.isEmpty()
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

                // Check if email is in the correct Format
                if (!isValidEmail(email)) {
                    Toast.makeText(RegisterJoin.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if password is in the correct Format
                if (!isValidPassword(password)) {
                    Toast.makeText(RegisterJoin.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if phone number is exactly 10 digits long
                if (!isValidPhoneNumber(phoneText.getText().toString())) {
                    Toast.makeText(RegisterJoin.this, "Phone number must be exactly 10 digits long", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Remove the check for phone number uniqueness

                // Proceed with registration
                registerUser(email, password, fullName, college, semester, branch, skills);
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
        // Add your password Format validation logic here
        return password.length() >= 8;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.length() == 10;
    }

    private void registerUser(String email, String password, final String fullName, final String college,
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
                                            addUserToDatabase(user, fullName, email, college, semester, branch, skills, profilePicUri);
                                            startActivity(new Intent(RegisterJoin.this, HomeJoin.class));
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

    private void addUserToDatabase(FirebaseUser user, String fullName, String email,
                                   String college, String semester, String branch, String skills, Uri profilePicUri) {
        DatabaseReference userRef = databaseRef.child(email.replace(".", ","));

        // Set the registration data as key-value pairs under the userRef
        userRef.child("Full name").setValue(fullName);
        userRef.child("Phone number").setValue(phoneText.getText().toString());
        userRef.child("Email").setValue(email);
        userRef.child("College").setValue(college);
        userRef.child("Semester").setValue(semester);
        userRef.child("Branch").setValue(branch);
        userRef.child("Skills").setValue(skills);

        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("profile_pictures")
                .child(email.replace(".", ",") + ".png");

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
                    Toast.makeText(RegisterJoin.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                });

        // Optional: Display a success toast message
        Toast.makeText(RegisterJoin.this, "User registration successful", Toast.LENGTH_SHORT).show();
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
            uCrop.start(RegisterJoin.this);
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