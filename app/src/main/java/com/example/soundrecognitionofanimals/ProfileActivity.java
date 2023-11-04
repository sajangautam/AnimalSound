package com.example.soundrecognitionofanimals;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import com.example.soundrecognitionofanimals.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICKER = 1;
    private ImageView imageViewProfile; // This should be the view where the profile image is displayed
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextUtaId, editTextProfession;
    private Button buttonSaveProfile, buttonSelectImage; // Renamed for clarity

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Here we initialize imageViewProfile and selectImageButton
        imageViewProfile = findViewById(R.id.imageViewProfile); // Make sure this ID matches your ImageView in XML
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextUtaId = findViewById(R.id.editTextUtaId);
        editTextProfession = findViewById(R.id.editTextProfession);

        buttonSaveProfile = findViewById(R.id.buttonSaveProfile);
        buttonSelectImage = findViewById(R.id.buttonSelectImage); // Make sure this ID matches your button in XML

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        Button saveProfileButton = findViewById(R.id.buttonSaveProfile);
        buttonSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity, which will take the user back to the previous Activity on the stack
                finish();
            }
        });

        loadUserData();
    }

    private void loadUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Check if user data exists
                    if (dataSnapshot.exists()) {
                        // Get user information
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            // Set user info to EditText fields
                            editTextFirstName.setText(user.getFirstName());
                            editTextLastName.setText(user.getLastName());
                            editTextEmail.setText(user.getEmail());
                            editTextUtaId.setText(user.getUtaId());
                            editTextProfession.setText(user.getProfession());
                            // If you also need to set the security question, do it here
                            // editTextSecurityQuestion.setText(user.getSecurityQuestion());
                        } else {
                            Toast.makeText(ProfileActivity.this, "User data is null.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "Can't find user data.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // No user is signed in
            Toast.makeText(ProfileActivity.this, "No signed in user.", Toast.LENGTH_LONG).show();
        }
    }


    private void saveUserProfile() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            User updatedUser = new User(
                    editTextEmail.getText().toString().trim(),
                    editTextFirstName.getText().toString().trim(),
                    editTextLastName.getText().toString().trim(),
                    editTextUtaId.getText().toString().trim(),
                    editTextProfession.getText().toString().trim(),
                    null, // Assuming password and securityQuestion are handled elsewhere
                    null
            );

            databaseReference.child(userId).setValue(updatedUser).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            imageViewProfile.setImageURI(selectedImageUri);
        }
    }

}
