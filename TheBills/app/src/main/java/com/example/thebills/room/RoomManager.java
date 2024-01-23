package com.example.thebills.room;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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

import java.util.HashMap;
import java.util.Map;

public class RoomManager {

    private static final String DATABASE_URL = "https://thebills-66df6-default-rtdb.europe-west1.firebasedatabase.app";
    private static final String ROOMS_REFERENCE = "rooms";
    private static final String USERS_REFERENCE = "users";

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    private DatabaseReference roomsRef;
    private DatabaseReference appUsersRef;

    private Context context;


    private Map<String, String> dataMap = null;

    public void setContext(Context context) {
        this.context = context;
    }


    public Map<String, String> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, String> dataMap) {
        this.dataMap = dataMap;
    }

    public RoomManager() {
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        roomsRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(ROOMS_REFERENCE);
        appUsersRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(USERS_REFERENCE);
    }

//    public void moveToRoomActivity(Context context) {
//        if (context != null && context instanceof AppCompatActivity) {
//            Intent intent = new Intent(context, Room.class);
//            ((AppCompatActivity) context).startActivity(intent);
//        }
//    }

    public void moveToRoomActivity(final Context context) {
        if (context != null && context instanceof AppCompatActivity) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(context, Room.class);
                    ((AppCompatActivity) context).startActivity(intent);
                }
            }, 500);
        }
    }
    public void moveToRoomActivity2(Context context, String roomId) {
        if (context != null && context instanceof AppCompatActivity) {
            Intent intent = new Intent(context, Room.class);
            intent.putExtra("roomId",roomId);
            ((AppCompatActivity) context).startActivity(intent);
        }
    }

    public void addRoomToUser(String roomKey, String roomName) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put(roomKey, roomName);
        appUsersRef.child(currentUser.getUid()).child("rooms").updateChildren(updateMap);
    }

    public void addUserToRoom(String roomId) {
        DatabaseReference usersRef = roomsRef.child(roomId).child("users");
        usersRef.child(currentUser.getUid()).setValue(true);
    }

    public void createNewRoom(String roomName) {
        String roomKey = roomsRef.push().getKey();
        RoomTuple newRoom = new RoomTuple(roomKey, roomName, currentUser.getUid());

        roomsRef.child(roomKey).setValue(newRoom).addOnSuccessListener(unused -> {
            addUserToRoom(roomKey);
            addRoomToUser(roomKey, roomName);
        });
    }

    public void joinRoom(String desiredRoomKey) {
        roomsRef.child(desiredRoomKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TheBills - joinRoom", "query done");
                if (dataSnapshot.exists()) {
                    String roomId = dataSnapshot.getKey();
                    String roomName = dataSnapshot.child("roomName").getValue(String.class);

                    if (roomId != null && roomName != null) {
                        addRoomToUser(roomId, roomName);
                        addUserToRoom(roomId);
                    }
                } else {
                    Log.d("TheBills - joinRoom", "Room not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TheBills - joinRoom", "database error");
            }
        });
    }

    public interface GetUserRoomsCallback {
        void onRoomsReceived(Map<String, String> roomMap);
        void onCancelled(String error);
    }


    public void getUserRooms(GetUserRoomsCallback callback) {
        appUsersRef.child(currentUser.getUid()).child("rooms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TheBills - joinRoom", "query done");

                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                Map<String, String> dataMap = dataSnapshot.getValue(genericTypeIndicator);

                if (dataMap != null) {
                    Log.d("Firebase", "Dane jako mapa: " + dataMap.toString());
                    callback.onRoomsReceived(dataMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TheBills - joinRoom", "database error");
                callback.onCancelled(databaseError.getMessage());
            }
        });
    }


}
