package com.example.soundrecognitionofanimals;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;


import java.util.Objects;


public class Register extends AppCompatActivity {
    private EditText firstNameEditText, lastNameEditText, utaIdEditText, professionEditText, registrationEmailEditText,
            createPasswordEditText, confirmPasswordEditText, securityQuestionEditText;
    private Button createAccountButton;
    private FirebaseAuth firebaseAuth;
    TextView textView;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize EditText fields and the Create Account button
        firstNameEditText = findViewById(R.id.editTextFirstName);
        lastNameEditText = findViewById(R.id.editTextLastName);
        utaIdEditText = findViewById(R.id.editTextUTAID);
        professionEditText = findViewById(R.id.editTextProfession);
        registrationEmailEditText = findViewById(R.id.editTextRegistrationEmail);
        createPasswordEditText = findViewById(R.id.editTextCreatePassword);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        securityQuestionEditText = findViewById(R.id.editTextsecurityQuestion);
        createAccountButton = findViewById(R.id.buttonCreateAccount);
        textView = findViewById(R.id.accLogin);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        // Set an onClickListener for the Create Account button
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                final String email = registrationEmailEditText.getText().toString().trim();
                final String password = createPasswordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();
                final String firstName = firstNameEditText.getText().toString().trim();
                final String lastName = lastNameEditText.getText().toString().trim();
                final String utaId = utaIdEditText.getText().toString().trim();
                final String profession = professionEditText.getText().toString().trim();
                final String securityQuestion = securityQuestionEditText.getText().toString().trim();

                // Input validation
                if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(utaId)
                        || TextUtils.isEmpty(profession) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                        || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(securityQuestion)) {
                    Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return; // Stop the registration process if any field is blank
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return; // Stop the registration process if passwords do not match
                }

                // Check if the email already exists in the database
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                Query query = usersRef.orderByChild("email").equalTo(email);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Email already exists in the database, show a message
                            Toast.makeText(Register.this, "This user is already registered.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Email is not found, proceed with registration
                            // Store user data in Firebase Realtime Database
                            // Create a unique key using push
                            String userKey = usersRef.push().getKey();

                            // Create a User object
                            User user = new User(email, firstName, lastName, utaId, profession, password, securityQuestion);

                            // Store the user data under the user's email
                            usersRef.child(userKey).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Data stored successfully
                                        Toast.makeText(getApplicationContext(), "Created new account for " + firstName + ".", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Handle the case where data storage failed
                                        Toast.makeText(getApplicationContext(), "Account creation failed. Please check your connection.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Database error occurred", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });


    }
}
