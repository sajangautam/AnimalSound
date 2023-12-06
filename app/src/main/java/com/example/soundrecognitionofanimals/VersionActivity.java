package com.example.soundrecognitionofanimals;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.view.View;
import android.view.MenuItem;

public class VersionActivity extends AppCompatActivity {

    TextView textViewVersionInfo;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textViewVersionInfo = findViewById(R.id.textViewVersionInfo);


        String versionInfo = "Model Name: Animal Sound Classifier\n" +
                "Version Number: 1.0.0\n" +
                "Description: Classifies animal sounds.\n" +
                "Google's speech to text API.\n" +
                "Firebase Authentication.\n" +
                "JDK 21.\n" +
                "Android Studio 4.2.\n" +
                "Accuracy: 95%\n" +
                "Release Notes: Final Version.";

        textViewVersionInfo.setText(versionInfo);

        backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Finish the activity and go back
                finish();
            }
        });
    }
}