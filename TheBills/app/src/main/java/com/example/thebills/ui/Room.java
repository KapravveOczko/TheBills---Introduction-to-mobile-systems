package com.example.thebills.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.thebills.R;

public class Room extends AppCompatActivity {

    String roomKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        roomKey = intent.getStringExtra("roomId");
        Log.d("TheBills: Room activity", "entered room: " + roomKey);

    }
}