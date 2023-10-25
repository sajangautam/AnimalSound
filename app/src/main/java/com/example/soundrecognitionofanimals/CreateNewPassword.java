package com.example.soundrecognitionofanimals;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CreateNewPassword extends AppCompatActivity {
    private EditText newPasswordEditText, confirmPasswordEditText;
    private Button setPasswordButton;
    private TextView textViewBack;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_password);

        newPasswordEditText = findViewById(R.id.editTextCreateNewPassword);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmNewPassword);
        setPasswordButton = findViewById(R.id.SetPassword);
        String currentUserEmail = getIntent().getStringExtra("userEmail");
        textViewBack = findViewById(R.id.back);
        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });
        setPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String newPassword = newPasswordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (newPassword.equals(confirmPassword)) {
                    // Passwords match, update the user's password in the database
                    // Check if the entered email exists in Firebase
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                    Query query = usersRef.orderByChild("email").equalTo(currentUserEmail);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DataSnapshot userSnapshot = null; // Declare userSnapshot outside the loop
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                userSnapshot = snapshot;
                            }
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userSnapshot.getKey());
                            userRef.child("password").setValue(newPassword);
                            Toast.makeText(CreateNewPassword.this, "Password changed successfully.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle any database error if necessary
                            Log.e("ForgotPassword", "Database Error: " + databaseError.getMessage());

                        }
                    });

                } else {
                    Toast.makeText(CreateNewPassword.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
