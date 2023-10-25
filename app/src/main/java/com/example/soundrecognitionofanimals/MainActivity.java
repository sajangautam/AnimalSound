package com.example.soundrecognitionofanimals;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.buttonLogout);
        textView = findViewById(R.id.user_details);
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        Intent intent = getIntent();
        String currentUserEmail = intent.getStringExtra("userEmail");

        if (currentUserEmail != null) {
            textView.setText("User email: " + currentUserEmail);
        } else {
            // No user email provided, handle the situation as needed (e.g., redirect to login)
            Intent loginIntent = new Intent(this, Login.class);
            startActivity(loginIntent);
            finish();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the logout logic or update your custom database if necessary
                // Example: Update your database to mark the user as logged out
                // Redirect the user to the login screen
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

}