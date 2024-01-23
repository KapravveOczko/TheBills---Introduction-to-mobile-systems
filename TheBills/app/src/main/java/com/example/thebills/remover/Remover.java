package com.example.thebills.remover;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Remover {

    private static final String DATABASE_URL = "https://thebills-66df6-default-rtdb.europe-west1.firebasedatabase.app";

    private static final String ROOMS_REFERENCE = "rooms";
    private static final String USERS_REFERENCE = "users";
    private static final String BILLS_REFERENCE = "bills";

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    private DatabaseReference roomsRef;
    private DatabaseReference appUsersRef;
    private DatabaseReference billsRef;

    private Context context;


    public Remover(Context context) {
        this.context = context;
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        roomsRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(ROOMS_REFERENCE);
        appUsersRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(USERS_REFERENCE);
        billsRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(BILLS_REFERENCE);
    }

    ///////////////////////////////////////////////////
    // removing bill

    private void removeBillFromRoom(String roomId, String billId) {
        roomsRef.child(roomId).child("bills").child(billId).removeValue();
    }

    private void removeBillFromUser(String userId, String billId) {
        appUsersRef.child(userId).child("bills").child(billId).removeValue();
    }

    private void removeBill(String billId) {
        billsRef.child(billId).removeValue();
    }

    public void removeBillById(String billId, String roomId, ArrayList<String> users) {
        removeBillFromRoom(roomId, billId);
        removeBill(billId);

        for (int i = 0; i!= users.size(); i++) {
            removeBillFromUser(users.get(i), billId);
        }
    }


    //////////////////////////////////////////////////
    //removing room


    private void removeRoomData(String roomId) {
        roomsRef.child(roomId).removeValue();
    }

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

    /////////////////////////////////////////////////
    //leaving room

    public void leaveRoom(String roomId, String userId) {
        appUsersRef.child(userId).child("rooms").child(roomId).removeValue();
        roomsRef.child(roomId).child("users").child(userId).removeValue();
    }


}
