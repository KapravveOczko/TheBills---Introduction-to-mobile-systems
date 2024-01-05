package com.example.thebills.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thebills.R;
import com.example.thebills.RoomManager;
import com.example.thebills.RoomManagerCreateRoom;
import com.example.thebills.RoomManagerJoinRoom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button buttonLogout;
    Button buttonCreateRoom;
    Button buttonJoinRoom;
    TextView textViewEmail;
    FirebaseUser user;

    RoomManager roomManager;
    Context context;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        roomManager = new RoomManager();

        auth = FirebaseAuth.getInstance();
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonCreateRoom = findViewById(R.id.buttonCreateRoom);
        buttonJoinRoom = findViewById(R.id.buttonJoinRoom);
        textViewEmail = findViewById(R.id.textViewEmail);
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else{
            textViewEmail.setText(user.getEmail());
        }

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


        buttonCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogCreateRoom();
            }
        });

        buttonJoinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogJoinRoom();
            }
        });
    }

    public void openDialogJoinRoom(){
        RoomManagerJoinRoom roomManagerJoinRoom = new RoomManagerJoinRoom();
        roomManagerJoinRoom.setContext(context);
        roomManagerJoinRoom.setRoomManager(roomManager);
        roomManagerJoinRoom.show(getSupportFragmentManager(), "join room dialog");
    }

    public void openDialogCreateRoom(){
        RoomManagerCreateRoom roomManagerCreateRoom = new RoomManagerCreateRoom();
        roomManagerCreateRoom.setContext(context);
        roomManagerCreateRoom.setRoomManager(roomManager);
        roomManagerCreateRoom.show(getSupportFragmentManager(), "create room dialog"); // Poprawiono literówkę
    }

}