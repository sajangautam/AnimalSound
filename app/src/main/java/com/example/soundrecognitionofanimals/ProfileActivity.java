package com.example.soundrecognitionofanimals;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICKER = 1;
    private ImageView imageViewProfile; // This should be the view where the profile image is displayed
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextUtaId, editTextProfession;
    private Button buttonSaveProfile, buttonSelectImage; // Renamed for clarity

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private List<Uri> selectedImages = new ArrayList<>();


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
        String userEmail = getIntent().getStringExtra("userEmail");
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

        loadUserData(userEmail);
    }

    private void loadUserData(String userEmail) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Query the database to find the user with the specified email
        Query query = usersRef.orderByChild("email").equalTo(userEmail);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the user information
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user != null) {
                            // Set user info to EditText fields
                            editTextFirstName.setText(user.getFirstName()); // Set hint with the user's first name
                            editTextLastName.setText(user.getLastName()); // You can set text directly if needed
                            editTextEmail.setText(user.getEmail());
                            editTextUtaId.setText(user.getUtaId());
                            editTextProfession.setText(user.getProfession());
                            // If you also need to set the security question, do it here
                            // editTextSecurityQuestion.setText(user.getSecurityQuestion());
                            return; // Exit the loop once the user is found
                        }
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
    }

    private void saveUserProfile() {
        String userEmail = getIntent().getStringExtra("userEmail");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        Query query = usersRef.orderByChild("email").equalTo(userEmail);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot userSnapshot = null; // Declare userSnapshot outside the loop
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    userSnapshot = snapshot;
                }
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userSnapshot.getKey());
                userRef.child("firstName").setValue(editTextFirstName.getText().toString().trim());
                userRef.child("lastName").setValue(editTextLastName.getText().toString().trim());
                userRef.child("profession").setValue(editTextProfession.getText().toString().trim());
                userRef.child("utaId").setValue(editTextUtaId.getText().toString().trim());

                // Save the selected image URIs to the user's profile
                for (int i = 0; i < selectedImages.size(); i++) {
                    String uriString = selectedImages.get(i).toString();
                    // Create a unique key for each image or use a specific naming convention
                    String imageKey = "image" + i;
                    userRef.child("profileImages").child(imageKey).setValue(uriString);
                }

                Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), Homepage.class);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); // Limit selection to images only
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Allow multiple image selection
        startActivityForResult(intent, REQUEST_IMAGE_PICKER);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK) {
            if (data != null) {
                // Handle single or multiple image selection
                if (data.getData() != null) {
                    // Single image selected
                    Uri selectedImageUri = data.getData();
                    selectedImages.add(selectedImageUri);

                    // Load the selected image into the profile picture ImageView
                    imageViewProfile.setImageURI(selectedImageUri);
                } else {
                    // Multiple images selected
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            Uri selectedImageUri = clipData.getItemAt(i).getUri();
                            selectedImages.add(selectedImageUri);
                        }
                    }
                }
            }
        }
    }




}
