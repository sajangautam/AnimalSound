package com.example.soundrecognitionofanimals;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class VersionActivity extends AppCompatActivity {

    TextView textViewVersionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        textViewVersionInfo = findViewById(R.id.textViewVersionInfo);

        // Here you can set the actual version info
        String versionInfo = "Model Name: Animal Sound Classifier\n" +
                "Version Number: 1.0.0\n" +
                "Description: Classifies animal sounds.\n" +
                "Accuracy: 95%\n" +
                "Release Notes: Initial release with basic functionality.";

        textViewVersionInfo.setText(versionInfo);
    }
}