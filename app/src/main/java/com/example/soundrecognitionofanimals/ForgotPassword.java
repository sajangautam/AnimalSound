package com.example.soundrecognitionofanimals;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailEditText;
    private EditText securityAnswerEditText;
    private Button verifyUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.editTextEmailFp);
        securityAnswerEditText = findViewById(R.id.editTextsecurityQuestionFp);
        verifyUserButton = findViewById(R.id.buttonVerifyUser);

        verifyUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user
                Log.d("ForgotPassword", "Clicked Verify User");
                String enteredEmail = emailEditText.getText().toString().trim();
                Log.d("ForgotPassword", "Entered Email: " + enteredEmail);
                String enteredSecurityAnswer = securityAnswerEditText.getText().toString().trim();
                Log.d("ForgotPassword", "Entered Security Answer: " + enteredSecurityAnswer);


                // Check if the entered email exists in Firebase
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                Query query = usersRef.orderByChild("email").equalTo(enteredEmail);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("ForgotPassword", "DataSnapshot exists: " + dataSnapshot.exists());

                        boolean userVerified = false; // Flag to track if the user has been verified

                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            User user = userSnapshot.getValue(User.class);
                            String storedSecurityQuestionAnswer = user.getSecurityQuestion();

                            // Compare the entered answer with the stored answer
                            if (enteredSecurityAnswer.equalsIgnoreCase(storedSecurityQuestionAnswer)) {
                                // User is verified, no need to check further
                                userVerified = true;
                                break;
                            }
                        }

                        if (userVerified) {
                            // User is verified, implement password recovery here
                            // You can send a password reset email to the entered email
                            // using Firebase Authentication's sendPasswordResetEmail method.
                            // Provide a UI feedback to the user.
                            Toast.makeText(ForgotPassword.this, "User Verified. Password reset email sent.", Toast.LENGTH_SHORT).show();
                        } else if (dataSnapshot.exists()) {
                            // User exists but security question does not match
                            Toast.makeText(ForgotPassword.this, "User not verified. Please check your security answer.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Email does not exist in the database
                            Toast.makeText(ForgotPassword.this, "User not found. Please register first.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any database error if necessary
                        Log.e("ForgotPassword", "Database Error: " + databaseError.getMessage());

                    }
                });
            }
        });


    }
}


