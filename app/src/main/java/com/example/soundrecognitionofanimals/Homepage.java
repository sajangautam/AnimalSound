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
    Button buttonLogOut, buttonProfile, buttonVersion, buttonRecognizeSound, buttonActivities;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        buttonLogOut = findViewById(R.id.logOutButton);
        buttonProfile = findViewById(R.id.profileButton);
        buttonVersion = findViewById(R.id.VersionButton);
        buttonRecognizeSound = findViewById(R.id.RecognizeSound);
        buttonActivities = findViewById(R.id.MyActivities);
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout logic
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout logic
                Intent intent = new Intent(getApplicationContext(), MyActivities.class);
                startActivity(intent);
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Profile logic
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("userEmail", getIntent().getStringExtra("userEmail"));
                startActivity(intent);
            }
        });

        buttonVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to VersionActivity
                Intent intent = new Intent(getApplicationContext(), VersionActivity.class);
                startActivity(intent);
            }
        });

        buttonRecognizeSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecognizeSound.class);
                startActivity(intent);
            }
        });
    }
}