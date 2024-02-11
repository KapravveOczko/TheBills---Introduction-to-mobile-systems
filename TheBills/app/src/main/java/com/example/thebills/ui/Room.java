package com.example.thebills.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thebills.R;
import com.example.thebills.remover.Remover;
import com.example.thebills.results.ResultsManager;
import com.example.thebills.room.RoomManager;
import com.google.firebase.auth.FirebaseAuth;


public class Room extends AppCompatActivity {

    TextView roomName;
    String roomKey;
    Button moveToBills;
    Button delete;
    TextView textViewResult;
    String user;
    Remover remover;
    ResultsManager resultsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        // Retrieve roomKey from intent
        try {
            Intent intent = getIntent();
            roomKey = intent.getStringExtra("roomId");
            Log.d("TheBills: Room activity", "entered room: " + roomKey);
        } catch (NullPointerException e){
            // If roomKey is not passed, redirect to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        remover = new Remover(this); // Initialize Remover
        textViewResult = findViewById(R.id.textViewResult);
        resultsManager = new ResultsManager(textViewResult);
        moveToBills = findViewById(R.id.buttonShowBills);
        delete = findViewById(R.id.buttonLeaveRoom);
        resultsManager.getBillsForRoom(roomKey);
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        roomName = findViewById(R.id.RoomName);

        // Click listener for leave room button
        delete.setOnClickListener(v -> {
            remover.leaveRoom(roomKey, user);
            moveAfterLeaving();
        });

        // Click listener for moving to bills view button
        moveToBills.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), BillsView.class);
            intent.putExtra("roomId", roomKey);
            startActivity(intent);
        });

        RoomManager roomManager = new RoomManager();
        // Retrieve room name from database
        roomManager.getRoomsName(roomKey, new RoomManager.GetRoomsNameCallback() {
            @Override
            public void onRoomsNameReceived(String name) {
                Log.d("TheBills: Room activity", "Room name received: " + name);
                roomName.setText(name);
            }

            @Override
            public void onCancelled(String error) {
                Log.d("TheBills: Room activity", "Error getting room name: " + error);
            }
        });

    }

    // Method to redirect to MainActivity after leaving the room
    private void moveAfterLeaving(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
