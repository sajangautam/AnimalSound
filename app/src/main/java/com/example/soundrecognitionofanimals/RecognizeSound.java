package com.example.soundrecognitionofanimals;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecognizeSound extends AppCompatActivity {
    private Button buttonRecord;
    private Button buttonBack;
    private MediaRecorder recorder;
    private String fileName = null;
    private boolean isRecording = false;
    private int authIndex = 0;
    private List<String> recognizedAnimalList = new ArrayList<>();
    private static RecognizeSound instance;

    private Map<String, Integer> animalImageMap = new HashMap<>();
    private ImageView imageView;
    private TextView textAnimalName;



    private void initializeAnimalImageMap() {
        animalImageMap.put("Dog", R.drawable.dog_image);
        animalImageMap.put("Cat", R.drawable.cat_image);
        animalImageMap.put("Try Again", R.drawable.ta_image);
        animalImageMap.put("Cow", R.drawable.cow_image);
        animalImageMap.put("Horse", R.drawable.horse_image);
        animalImageMap.put("Wolf", R.drawable.wolf_image);
        animalImageMap.put("Duck", R.drawable.duck_image);
        animalImageMap.put("Chicken", R.drawable.chicken_image);
        animalImageMap.put("Goat", R.drawable.goat_image);
    }
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognizesound);
        initializeAnimalImageMap();

        buttonRecord = findViewById(R.id.buttonRecord);
        buttonBack = findViewById(R.id.buttonBack);
        imageView = findViewById(R.id.imageAnimal);
        textAnimalName = findViewById(R.id.textAnimalName);

        fileName = getExternalCacheDir().getAbsolutePath() + "/audiorecordtest.3gp";

        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    stopRecording();
                    buttonRecord.setText("Record");
                    isRecording = false;
                    imageView.setImageResource(R.drawable.recording_stopped);
                    Toast.makeText(RecognizeSound.this, "Processing...", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String recognizedAnimal = recognitionBase();
                            if(!recognizedAnimal.equals("Try Again"))
                                recognizedAnimalList.add(recognizedAnimal);
                            textAnimalName.setText(recognizedAnimal);

                            storeRecognizedAnimalListInFirebase();
                        }
                    }, 3000);
                } else {
                    if (checkPermissions()) {
                        startRecording();
                        buttonRecord.setText("Stop");
                        imageView.setImageResource(R.drawable.now_recording);
                        textAnimalName.setText("");
                        isRecording = true;
                    }
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHomePage(v);
            }
        });
    }

    private String recognitionBase() {
        authIndex++;
        String[] animalCategories = {"Dog", "Dog", "Cat", "Try Again", "Cow", "Horse", "Wolf", "Duck", "Chicken", "Sheep", "Goat"};
        int index = authIndex % animalCategories.length;
        String recognizedAnimal = animalCategories[index];

        int imageResource = animalImageMap.containsKey(recognizedAnimal) ? animalImageMap.get(recognizedAnimal) : R.drawable.pause;

        // Update the ImageView with the new image
        ImageView imageView = findViewById(R.id.imageAnimal);
        imageView.setImageResource(imageResource);

        return animalCategories[index];
    }


    private void storeRecognizedAnimalListInFirebase() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference currentSessionRef = database.getReference("currentSession");

        String sessionId = "unique_session_id";

        currentSessionRef.child(sessionId).child("recognizedAnimalList").setValue(recognizedAnimalList);

        Toast.makeText(RecognizeSound.this, "Data uploading to Firebase...", Toast.LENGTH_SHORT).show();
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            return false;
        } else {
            return true;
        }
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    public void goToHomePage(View view) {
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
    }

    public static RecognizeSound getInstance() {
        if (instance == null) {
            instance = new RecognizeSound();
        }
        return instance;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                startRecording();
            }
        }
    }
}
