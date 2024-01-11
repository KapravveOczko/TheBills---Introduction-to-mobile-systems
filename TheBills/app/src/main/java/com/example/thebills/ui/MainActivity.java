package com.example.thebills.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thebills.R;
import com.example.thebills.room.RoomRecycleViewEvent;
import com.example.thebills.room.RoomManager;
import com.example.thebills.room.RoomManagerCreateRoom;
import com.example.thebills.room.RoomManagerJoinRoom;
import com.example.thebills.room.RoomManagerRecycleViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements RoomRecycleViewEvent {

    Button buttonLogout;
    Button buttonCreateRoom;
    Button buttonJoinRoom;
    TextView textViewEmail;
    ProgressBar progressBar;


    FirebaseAuth auth;
    FirebaseUser user;
    RoomManager roomManager;
    Context context;

    List<String> keys;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        roomManager = new RoomManager();

        progressBar = findViewById(R.id.progressBarRooms);
        progressBar.setVisibility(View.VISIBLE);

        auth = FirebaseAuth.getInstance();
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonCreateRoom = findViewById(R.id.buttonCreateRoom);
        buttonJoinRoom = findViewById(R.id.buttonJoinRoom);
        textViewEmail = findViewById(R.id.textViewEmail);
        user = auth.getCurrentUser();

//        RecyclerView recyclerView = findViewById(R.id.roomRecycleView);


        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textViewEmail.setText(user.getEmail());
        }

//        roomManager.getUserRooms(new RoomManager.GetUserRoomsCallback() {
//            @Override
//            public void onRoomsReceived(Map<String, String> roomMap) {
//                progressBar.setVisibility(View.INVISIBLE);
//                keys = new ArrayList<>(roomMap.keySet());
//                RoomManagerRecycleViewAdapter adapter = new RoomManagerRecycleViewAdapter(context, roomMap, MainActivity.this);
//
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//                recyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(String error) {
//                Log.d("MainActivity", "Błąd: " + error);
//            }
//        });

        setRecycleView();

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

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        RecyclerView recyclerView = findViewById(R.id.roomRecycleView);
        if (recyclerView != null) {
            setRecycleView();
        }
    }


    public void openDialogJoinRoom() {
        RoomManagerJoinRoom roomManagerJoinRoom = new RoomManagerJoinRoom();
        roomManagerJoinRoom.setContext(context);
        roomManagerJoinRoom.setRoomManager(roomManager);
        roomManagerJoinRoom.show(getSupportFragmentManager(), "join room dialog");
    }

    public void openDialogCreateRoom() {
        RoomManagerCreateRoom roomManagerCreateRoom = new RoomManagerCreateRoom();
        roomManagerCreateRoom.setContext(context);
        roomManagerCreateRoom.setRoomManager(roomManager);
        roomManagerCreateRoom.show(getSupportFragmentManager(), "create room dialog");
    }

    @Override
    public void onItemClick(int position) {
        String roomKey = keys.get(position);
        Toast.makeText(this, "entering room: " + roomKey, Toast.LENGTH_SHORT).show();
        roomManager.moveToRoomActivity2(context, roomKey);
    }

    public void setRecycleView() {
        roomManager.getUserRooms(new RoomManager.GetUserRoomsCallback() {

            RecyclerView recyclerView = findViewById(R.id.roomRecycleView);
            @Override
            public void onRoomsReceived(Map<String, String> roomMap) {
                progressBar.setVisibility(View.INVISIBLE);
                keys = new ArrayList<>(roomMap.keySet());
                RoomManagerRecycleViewAdapter adapter = new RoomManagerRecycleViewAdapter(context, roomMap, MainActivity.this);

                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(String error) {
                Log.d("MainActivity", "Błąd: " + error);
            }
        });
    }
}