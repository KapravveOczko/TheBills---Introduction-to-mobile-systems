package com.example.thebills.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thebills.R;
import com.example.thebills.results.ResultsManager;


public class Room extends AppCompatActivity {

    String roomKey;
    Button moveToBills;
    Button moveToUsers;
    TextView textViewResult;

    ResultsManager resultsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        roomKey = intent.getStringExtra("roomId");
        Log.d("TheBills: Room activity", "entered room: " + roomKey);


        textViewResult = findViewById(R.id.textViewResult);
        resultsManager = new ResultsManager(textViewResult);

        moveToBills = findViewById(R.id.buttonShowBills);
        moveToUsers = findViewById(R.id.buttonDeleteRoom);

        resultsManager.getBillsForRoom(roomKey);
//        resultsManager.getBills();
//        resultsManager.getUsers(roomKey);



        moveToBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BillsView.class);
                intent.putExtra("roomId", roomKey);
                startActivity(intent);
                finish();
            }
        });

    }
}
