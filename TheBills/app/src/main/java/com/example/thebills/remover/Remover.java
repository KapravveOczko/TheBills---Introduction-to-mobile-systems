package com.example.thebills.remover;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

// Class responsible for removing data from Firebase Realtime Database
public class Remover {

    private static final String DATABASE_URL = "https://thebills-66df6-default-rtdb.europe-west1.firebasedatabase.app";

    private static final String ROOMS_REFERENCE = "rooms";
    private static final String USERS_REFERENCE = "users";
    private static final String BILLS_REFERENCE = "bills";

    private final FirebaseAuth auth;
    private FirebaseUser currentUser;
    private final Context context;

    private final DatabaseReference roomsRef;
    private final DatabaseReference appUsersRef;
    private final DatabaseReference billsRef;

    // Constructor to initialize Remover with Firebase instances
    public Remover(Context context) {
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        roomsRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(ROOMS_REFERENCE);
        appUsersRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(USERS_REFERENCE);
        billsRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(BILLS_REFERENCE);
        this.context = context;
    }

    // Method to remove a bill from a room
    private void removeBillFromRoom(String roomId, String billId) {
        roomsRef.child(roomId).child("bills").child(billId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("TheBills: Remover", "bill removed from room");
            }
        });
    }

    // Method to remove a bill from a user
    private void removeBillFromUser(String userId, String billId) {
        appUsersRef.child(userId).child("bills").child(billId).removeValue();
    }

    // Method to remove a bill from the database
    private void removeBill(String billId) {
        billsRef.child(billId).removeValue();
    }

    // Method to remove a bill by its ID along with its association with the room and users
    public void removeBillById(String billId, String roomId, ArrayList<String> users) {
        removeBillFromRoom(roomId, billId);
        removeBill(billId);

        for (String userId : users) {
            removeBillFromUser(userId, billId);
        }
    }

    // Method to remove room data from the database
    private void removeRoomData(String roomId) {
        roomsRef.child(roomId).removeValue();
    }

    // Method to remove a room and its associated bills and users
    public void removeRoom(String roomId, ArrayList<String> users, ArrayList<String> bills) {
        // Remove room data
        removeRoomData(roomId);

        // Remove bills associated with the room
        for (String billId : bills) {
            removeBillFromRoom(roomId, billId);
            removeBill(billId);

            // Remove bills from users
            for (String userId : users) {
                removeBillFromUser(userId, billId);
            }
        }
    }

    // Method to remove a user from a room
    public void leaveRoom(String roomId, String userId) {
        appUsersRef.child(userId).child("rooms").child(roomId).removeValue();
        roomsRef.child(roomId).child("users").child(userId).removeValue();
    }
}
