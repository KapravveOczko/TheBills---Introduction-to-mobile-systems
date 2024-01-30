package com.example.thebills;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.thebills.room.RoomManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserManager {

    private static final String DATABASE_URL = "https://thebills-66df6-default-rtdb.europe-west1.firebasedatabase.app";
    private static final String USERS_REFERENCE = "users";
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private DatabaseReference appUsersRef;

    public UserManager() {
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        appUsersRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(USERS_REFERENCE);
    }

//do sprawdzenia czy zadziała
    public void addUsername(String userId, String username) {
        DatabaseReference usersRef = appUsersRef.child(userId);
        usersRef.setValue("username", username);
    }


    public interface GetUsernameCallback {
        void onUsernameReceived(String name);
        void onCancelled(String error);
    }


    public void getUsername(String userId, UserManager.GetUsernameCallback callback) {
        appUsersRef.child(userId).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);

                if (username != null) {
                    callback.onUsernameReceived(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onCancelled(databaseError.getMessage());
            }
        });
    }
}
