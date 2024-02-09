package com.example.thebills;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class UserManager {

    private static final String DATABASE_URL = "https://thebills-66df6-default-rtdb.europe-west1.firebasedatabase.app";
    private static final String USERS_REFERENCE = "users";
    private FirebaseAuth auth;
    private DatabaseReference appUsersRef;

    public UserManager() {
        auth = FirebaseAuth.getInstance();
        appUsersRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(USERS_REFERENCE);
    }

    public void addUsername(String userId, String username) {
        addUsernameToUser(userId, username);
        addUsernameToList(username);
    }

    public void addUsernameToUser(String userId, String username) {
        DatabaseReference usersRef = appUsersRef.child(userId).child("username");
        usersRef.setValue(username);
    }

    public void addUsernameToList(String username){
        DatabaseReference usernameListRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference("usernameList");
        usernameListRef.child(username).setValue(true);
    }


    //

    public interface GetUsernameListCallback {
        void onUsernameListReceived(Map<String, Boolean> usernameList);
        void onCancelled(String error);
    }

    public void getUsernameList(String userId, UserManager.GetUsernameListCallback callback) {
        appUsersRef.child(userId).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map <String, Boolean> usernameList = dataSnapshot.getValue(Map.class);

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

    public boolean checkAvailability(String username, UserManager.GetUsernameListCallback callback){
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
        return false;
    }


    //

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
