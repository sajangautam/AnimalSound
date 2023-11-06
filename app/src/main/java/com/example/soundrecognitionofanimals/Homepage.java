package com.example.soundrecognitionofanimals;
import android.annotation.SuppressLint;
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




public class Homepage extends AppCompatActivity {
    Button buttonLogOut;
    Button buttonProfile;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        buttonLogOut = findViewById(R.id.logOutButton);
        buttonProfile = findViewById(R.id.profileButton);

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
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
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event for the Profile button
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                startActivity(intent);
            }
        });
    }
}