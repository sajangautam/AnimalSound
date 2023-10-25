package com.example.soundrecognitionofanimals;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    TextView textView, textViewFp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize EditText fields and the Login button
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogIn);
        textView = findViewById(R.id.textViewCreateAccount);
        textViewFp = findViewById(R.id.textViewForgotPassword);
        progressBar = findViewById(R.id.progressBar);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });
        textViewFp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });

        // Set an onClickListener for the Login button
        // ... Previous code ...

// Set an onClickListener for the Login button
        // ... Previous code ...

// Set an onClickListener for the Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                // Get user input
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return; // Stop the login process if any field is blank
                } else {
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                    Query query = usersRef.orderByChild("email").equalTo(email);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            if (dataSnapshot.exists()) {
                                // Email exists in the database, now check the password
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    String storedPassword = userSnapshot.child("password").getValue(String.class);
                                    if (password.equals(storedPassword)) {
                                        // Password matches, login is successful
                                        Toast.makeText(Login.this, "Login successful.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Homepage.class);
                                        intent.putExtra("userEmail", email);
                                        startActivity(intent);
                                        return; // Exit the login process
                                    }
                                }
                                // Password doesn't match any user, show an error message
                                Toast.makeText(Login.this, "Incorrect password. Please try again.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Login.this, "Login failed. Please check your credentials or Create Account.",  Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Database error occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}
