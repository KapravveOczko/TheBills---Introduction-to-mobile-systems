package com.example.thebills.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thebills.R;
import com.example.thebills.remover.Remover;
import com.example.thebills.results.ResultsManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Room extends AppCompatActivity {

    String roomKey;
    Button moveToBills;
    Button delete;
    TextView textViewResult;
    String user;
    Remover remover;
    private FirebaseAuth auth;

    ResultsManager resultsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        try {
            Intent intent = getIntent();
            roomKey = intent.getStringExtra("roomId");
            Log.d("TheBills: Room activity", "entered room: " + roomKey);
        } catch (NullPointerException e){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        remover = new Remover(this);

        textViewResult = findViewById(R.id.textViewResult);
        resultsManager = new ResultsManager(textViewResult);

        moveToBills = findViewById(R.id.buttonShowBills);
        delete = findViewById(R.id.buttonLeaveRoom);

        resultsManager.getBillsForRoom(roomKey);
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remover.leaveRoom(roomKey, user);
                moveAfterLeaving();
            }
        });

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

    private void moveAfterLeaving(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
