package com.example.thebills.room;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thebills.ui.Room;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class RoomManager {

    // Constants for Firebase database
    private static final String DATABASE_URL = "https://thebills-66df6-default-rtdb.europe-west1.firebasedatabase.app";
    private static final String ROOMS_REFERENCE = "rooms";
    private static final String USERS_REFERENCE = "users";

    private final FirebaseAuth auth;
    private FirebaseUser currentUser;

    // Database references
    private final DatabaseReference roomsRef;
    private final DatabaseReference appUsersRef;

    // Context variable for Toast messages
    private Context context;

    // Method to set the context
    public void setContext(Context context) {
        this.context = context;
    }

    // Constructor initializes Firebase instances and database references
    public RoomManager() {
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        roomsRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(ROOMS_REFERENCE);
        appUsersRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(USERS_REFERENCE);
    }

    // Method to move to the Room activity
    public void moveToRoomActivity(Context context, String roomId) {
        if (context instanceof AppCompatActivity) {
            Intent intent = new Intent(context, Room.class);
            intent.putExtra("roomId",roomId);
            context.startActivity(intent);
        }
    }

    // Method to add a room to the list of rooms for the current user
    public void addRoomToUser(String roomKey, String roomName) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put(roomKey, roomName);
        appUsersRef.child(currentUser.getUid()).child("rooms").updateChildren(updateMap);
    }

    // Method to add the current user to the specified room
    public void addUserToRoom(String roomId) {
        DatabaseReference usersRef = roomsRef.child(roomId).child("users");
        usersRef.child(currentUser.getUid()).setValue(true);
    }

    // Method to create a new room
    public void createNewRoom(String roomName) {
        String roomKey = roomsRef.push().getKey();
        Map<String, Object> newRoomData = new HashMap<>();
        newRoomData.put("roomId", roomKey);
        newRoomData.put("roomName", roomName);
        newRoomData.put("ownerId", currentUser.getUid());
        newRoomData.put("createDate", new Timestamp(System.currentTimeMillis()));

        Map<String, Boolean> users = new HashMap<>();
        users.put(currentUser.getUid(), true);

        newRoomData.put("users", users);

        roomsRef.child(roomKey).setValue(newRoomData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                addUserToRoom(roomKey);
                addRoomToUser(roomKey, roomName);
                Log.d("TheBills: RoomManager", "New room created successfully");
            } else {
                Log.d("TheBills: RoomManager", "Failed to create new room");
            }
        });
    }

    // Method to join a room
    public void joinRoom(String desiredRoomKey) {
        roomsRef.child(desiredRoomKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TheBills: RoomManager", "joinRoom: query done");
                if (dataSnapshot.exists()) {
                    String roomId = dataSnapshot.getKey();
                    String roomName = dataSnapshot.child("roomName").getValue(String.class);

                    if (roomId != null && roomName != null) {
                        addRoomToUser(roomId, roomName);
                        addUserToRoom(roomId);
                    }
                } else {
                    Log.d("TheBills: RoomManager", "joinRoom: Room not found");
                    Toast.makeText(context, "Room not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TheBills: RoomManager", "error " + databaseError);
            }
        });
    }

    // Interface for receiving user rooms
    public interface GetUserRoomsCallback {
        void onRoomsReceived(Map<String, String> roomMap);
        void onNullReceived();
        void onCancelled(String error);
    }

    // Method to get user rooms
    public void getUserRooms(GetUserRoomsCallback callback) {
        appUsersRef.child(currentUser.getUid()).child("rooms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TheBills: RoomManager", "query done: " + dataSnapshot);

                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                Map<String, String> dataMap = dataSnapshot.getValue(genericTypeIndicator);

                if (dataMap != null) {
                    callback.onRoomsReceived(dataMap);
                }
                else{
                    callback.onNullReceived();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TheBills: RoomManager", "error " + databaseError);
                callback.onCancelled(databaseError.getMessage());
            }
        });
    }

    // Interface for receiving room names
    public interface GetRoomsNameCallback {
        void onRoomsNameReceived(String name);
        void onCancelled(String error);
    }

    // Method to get room name
    public void getRoomsName(String roomId, GetRoomsNameCallback callback) {
        roomsRef.child(roomId).child("roomName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TheBills: RoomManager", "getRoomsName: query done");

                String roomName = dataSnapshot.getValue(String.class);

                if (roomName != null) {
                    Log.d("Firebase", "Room name: " + roomName);
                    callback.onRoomsNameReceived(roomName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TheBills - get room name", "database error");
                callback.onCancelled(databaseError.getMessage());
            }
        });
    }
}
