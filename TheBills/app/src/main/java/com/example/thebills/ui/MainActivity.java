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
import com.example.thebills.UserManager;
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

    // UI elements
    Button buttonLogout;
    Button buttonCreateRoom;
    Button buttonJoinRoom;
    TextView textViewUsername;
    ProgressBar progressBar;
    Button refresh;

    // Firebase
    FirebaseAuth auth;
    FirebaseUser user;

    // Managers
    RoomManager roomManager;
    UserManager userManager;

    // Context
    Context context;

    // Room keys
    List<String> keys;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize context, room manager, and user manager
        context = this;
        roomManager = new RoomManager();
        userManager = new UserManager();

        // Initialize Firebase Authentication instance and get the current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Find views by their IDs
        progressBar = findViewById(R.id.progressBarRooms);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonCreateRoom = findViewById(R.id.buttonCreateRoom);
        buttonJoinRoom = findViewById(R.id.buttonJoinRoom);
        textViewUsername = findViewById(R.id.textViewUsername);

        // Check if the user is logged in, if not, redirect to the Login activity
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        // Set up RecyclerView
        setRecycleView();

        // Set onClickListener for logout button
        buttonLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });

        // Set onClickListener for refresh button to recreate activity
        refresh = findViewById(R.id.buttonRefresh);
        refresh.setOnClickListener(v -> recreate());

        // Set onClickListeners for creating and joining room buttons
        buttonCreateRoom.setOnClickListener(v -> openDialogCreateRoom());
        buttonJoinRoom.setOnClickListener(v -> openDialogJoinRoom());

        // Set the username
        setUsername(new UserManager.GetUsernameCallback() {
            @Override
            public void onUsernameReceived(String name) {
                textViewUsername.setText(name);
            }

            @Override
            public void onCancelled(String error) {
                Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Refresh data when the activity resumes
    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    // Refresh RecyclerView data
    private void refreshData() {
        RecyclerView recyclerView = findViewById(R.id.roomRecycleView);
        if (recyclerView != null) {
            setRecycleView();
        }
    }

    // Open join room dialog
    public void openDialogJoinRoom() {
        RoomManagerJoinRoom roomManagerJoinRoom = new RoomManagerJoinRoom();
        roomManagerJoinRoom.setContext(context);
        roomManagerJoinRoom.setRoomManager(roomManager);
        roomManagerJoinRoom.show(getSupportFragmentManager(), "join room dialog");
    }

    // Open create room dialog
    public void openDialogCreateRoom() {
        RoomManagerCreateRoom roomManagerCreateRoom = new RoomManagerCreateRoom();
        roomManagerCreateRoom.setContext(context);
        roomManagerCreateRoom.setRoomManager(roomManager);
        roomManagerCreateRoom.show(getSupportFragmentManager(), "create room dialog");
    }

    // Handle RecyclerView item click event
    @Override
    public void onItemClick(int position) {
        String roomKey = keys.get(position);
        Toast.makeText(this, "entering room: " + roomKey, Toast.LENGTH_SHORT).show();
        roomManager.moveToRoomActivity(context, roomKey);
    }

    // Set up RecyclerView
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
                Log.d("TheBills: MainActivity activity", "error: " + error);
            }
        });
    }

    // Set the username
    public void setUsername(UserManager.GetUsernameCallback callback) {
        userManager.getUsername(user.getUid(), new UserManager.GetUsernameCallback() {
            @Override
            public void onUsernameReceived(String name) {
                callback.onUsernameReceived(name);
            }

            @Override
            public void onCancelled(String error) {
                callback.onCancelled(error);
            }
        });
    }
}
