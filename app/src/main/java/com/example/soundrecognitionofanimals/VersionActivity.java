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
                "Accuracy: 95%\n" +
                "Release Notes: Initial release with basic functionality.";

        textViewVersionInfo.setText(versionInfo);

        backButton = findViewById(R.id.backButton);
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