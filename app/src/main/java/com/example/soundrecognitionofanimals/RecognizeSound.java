package com.example.soundrecognitionofanimals;
import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.Toast;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.widget.ArrayAdapter;
import android.widget.Spinner;



import java.io.File;
import java.io.IOException;


public class RecognizeSound extends AppCompatActivity {
    private Button buttonRecord, buttonPlay, buttonBack;
    private Spinner spinnerRecordings;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private String fileName = null;
    private boolean isRecording = false, isPlaying = false;
    private List<String> recordedFiles = new ArrayList<>();
    private ArrayAdapter<String> recordingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognizesound);

        buttonRecord = findViewById(R.id.buttonRecord);
        buttonPlay = findViewById(R.id.buttonPlay);
        buttonBack = findViewById(R.id.buttonBack);
        spinnerRecordings = findViewById(R.id.spinnerRecordings);

        recordingsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        recordingsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecordings.setAdapter(recordingsAdapter);

        // Check if the RecordedSound folder exists, if not, create it
        File folder = new File(getExternalFilesDir(null), "RecordedSound");
        if (!folder.exists()) {
            folder.mkdirs(); // This will create the folder if it does not exist
        }

        fileName = new File(folder, "audiorecordtest.3gp").getAbsolutePath();

        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    stopRecording();
                    buttonRecord.setText("Record");
                    isRecording = false;
                } else {
                    if (checkPermissions()) {
                        startRecording();
                        buttonRecord.setText("Stop");
                        isRecording = true;
                    }
                }
            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    playSelectedRecording();
                    buttonPlay.setText("Stop");
                    isPlaying = true;
                } else {
                    stopPlaying();
                    buttonPlay.setText("Play");
                    isPlaying = false;
                }
            }
        });


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes the current activity and returns to the previous one
            }
        });
    }

    private void startRecording() {
        // Use a timestamp to create a unique file name for each recording
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        fileName = new File(getExternalFilesDir(null) + "/RecordedSound/", "AUD_" + timestamp + ".3gp").getAbsolutePath();
        recordedFiles.add(fileName); // Add file name to the list

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            Log.e("RecognizeSound", "Recording start failed", e);
            Toast.makeText(this, "Failed to start recording", Toast.LENGTH_SHORT).show();
        }
        updateRecordingsList();
    }
    private void playSelectedRecording() {
        int selectedPosition = spinnerRecordings.getSelectedItemPosition();
        // Ensure the selection is valid (excluding the "Select a recording" placeholder)
        if (selectedPosition > 0 && selectedPosition <= recordedFiles.size()) {
            String selectedFile = recordedFiles.get(selectedPosition - 1); // Adjust for the placeholder
            Log.d("RecognizeSound", "Selected file for playback: " + selectedFile);
            startPlaying(selectedFile); // Start playing the selected file
        } else {
            Toast.makeText(this, "Please select a recording", Toast.LENGTH_SHORT).show();
        }
    }
    private void playRecording(String filePath) {
        if (player == null) {
            player = new MediaPlayer();
        } else {
            player.reset();
        }

        try {
            player.setDataSource(filePath);
            player.prepare();
            player.start();

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // Audio has finished playing
                    stopPlaying();
                }
            });
        } catch (IOException e) {
            Log.e("RecognizeSound", "Could not start playback", e);
            Toast.makeText(this, "Playback failed", Toast.LENGTH_SHORT).show();
        }
    }
    private void stopRecording() {
        try {
            recorder.stop();
            recorder.release();
            recorder = null;
            Toast.makeText(this, "Recording saved successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Log the error for debugging purposes
            Log.e("RecognizeSound", "Failed to save the recording", e);
            // Show a user-friendly error message
            Toast.makeText(this, "Failed to save the recording", Toast.LENGTH_SHORT).show();
        }
    }
    private void startPlaying(String filePath) {
        if (player != null) {
            player.release();
        }
        player = new MediaPlayer();

        try {
            player.setDataSource(filePath);
            player.prepare();
            player.start();

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });
        } catch (IOException e) {
            Log.e("RecognizeSound", "Could not start playback", e);
            Toast.makeText(this, "Playback failed", Toast.LENGTH_SHORT).show();
        }
    }
    private void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;
        }
        isPlaying = false;
        buttonPlay.setText("Play");
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            }
        }
    }
    private void updateRecordingsList() {
        recordingsAdapter.clear();
        recordingsAdapter.add("Select a recording"); // Placeholder item

        for (String filePath : recordedFiles) {
            File file = new File(filePath);
            recordingsAdapter.add(file.getName());
        }
        recordingsAdapter.notifyDataSetChanged();
    }


}