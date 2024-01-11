package com.example.thebills.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.thebills.R;

public class Room extends AppCompatActivity {

    String roomKey;
    Button moveToBills;
    Button moveToUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        roomKey = intent.getStringExtra("roomId");
        Log.d("TheBills: Room activity", "entered room: " + roomKey);

        moveToBills = findViewById(R.id.buttonShowBills);
        moveToUsers = findViewById(R.id.buttonShowUsers);

        moveToBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BillsView.class);
                startActivity(intent);
                finish();
            }
        });

    }
}