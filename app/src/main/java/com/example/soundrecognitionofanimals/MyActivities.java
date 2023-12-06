package com.example.soundrecognitionofanimals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyActivities extends AppCompatActivity {
    private Button buttonBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myactivities);
        buttonBack = findViewById(R.id.backButton);

        fetchRecognizedAnimalsFromFirebase();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Homepage.class);
                startActivity(intent);
            }
        });
    }

    private void fetchRecognizedAnimalsFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference currentSessionRef = database.getReference("currentSession");

        String sessionId = "unique_session_id";

        currentSessionRef.child(sessionId).child("recognizedAnimalList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> recognizedAnimals = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String recognizedAnimal = snapshot.getValue(String.class);
                    recognizedAnimals.add(recognizedAnimal);
                }

                ListView listViewRecognizedAnimals = findViewById(R.id.listViewRecognizedAnimals);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MyActivities.this, android.R.layout.simple_list_item_1, recognizedAnimals);
                listViewRecognizedAnimals.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}
