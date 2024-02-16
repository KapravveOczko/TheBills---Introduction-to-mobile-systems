package com.example.thebills.user;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class UserManager {

    // Firebase components
    private static final String DATABASE_URL = "https://thebills-66df6-default-rtdb.europe-west1.firebasedatabase.app";
    private static final String USERS_REFERENCE = "users";
    private final DatabaseReference appUsersRef;

    // Constructor
    public UserManager() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        appUsersRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(USERS_REFERENCE);
    }

    // Add username to user's data and username list
    public void addUsername(String userId, String username) {
        addUsernameToUser(userId, username);
        addUsernameToList(username);
    }

    // Add username to user's data
    public void addUsernameToUser(String userId, String username) {
        DatabaseReference usersRef = appUsersRef.child(userId).child("username");
        usersRef.setValue(username);
    }

    // Add username to the username list
    public void addUsernameToList(String username){
        DatabaseReference usernameListRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference("usernameList");
        usernameListRef.child(username).setValue(true);
    }

    // Interface for retrieving a list of usernames
    public interface GetUsernameListCallback {
        void onUsernameListReceived(Map<String, Boolean> usernameList);
        void onCancelled(String error);
    }

        // Retrieve the list of usernames
    //    public void getUsernameList(String userId, UserManager.GetUsernameListCallback callback) {
    //        appUsersRef.child(userId).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
    //            @Override
    //            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    //                Map<String, Boolean> usernameList = dataSnapshot.getValue(Map.class);
    //
    //                if (usernameList != null) {
    //                    callback.onUsernameListReceived(usernameList);
    //                }
    //            }
    //
    //            @Override
    //            public void onCancelled(@NonNull DatabaseError databaseError) {
    //                callback.onCancelled(databaseError.getMessage());
    //            }
    //        });
    //    }

    // Check the availability of a username
    public void checkAvailability(UserManager.GetUsernameListCallback callback){
        DatabaseReference usernameListRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference("usernameList");
        usernameListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Boolean> usernameList = (Map<String, Boolean>) dataSnapshot.getValue();
                if (usernameList != null) {
                    callback.onUsernameListReceived(usernameList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onCancelled(databaseError.getMessage());
            }
        });
    }

    // Interface for retrieving a username
    public interface GetUsernameCallback {
        void onUsernameReceived(String name);
        void onCancelled(String error);
    }

    // Get a specific user's username
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
