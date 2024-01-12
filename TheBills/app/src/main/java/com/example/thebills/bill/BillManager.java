package com.example.thebills.bill;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class BillManager {

    private static final String DATABASE_URL = "https://thebills-66df6-default-rtdb.europe-west1.firebasedatabase.app";
    private static final String ROOMS_REFERENCE = "rooms";

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    private DatabaseReference roomsRef;

    private Context context;
    private String currentRoom;


    private Map<String, String> dataMap = null;

    public void setContext(Context context) {
        this.context = context;
    }

    public BillManager() {
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        roomsRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(ROOMS_REFERENCE);
    }

//    ------------------------------------------------

    public String getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }

//    ------------------------------------------------

    public interface GetRoomUsersCallback {
        void onUsersReceived(Map<String, Boolean> usersMap);
        void onCancelled(String error);
    }

    public void getRoomUsers(BillManager.GetRoomUsersCallback callback) {
        roomsRef.child(getCurrentRoom()).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TheBills - room users", "query done");

                GenericTypeIndicator<Map<String, Boolean>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Boolean>>() {};
                Map<String, Boolean> dataMap = dataSnapshot.getValue(genericTypeIndicator);

                if (dataMap != null) {
//                    dataMap.remove(currentUser.getUid());
                    Log.d("Firebase", "Dane jako mapa: " + dataMap.toString());
                    callback.onUsersReceived(dataMap);
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



