package com.example.thebills;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.thebills.ui.Room;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RoomManagerJoinRoom extends AppCompatDialogFragment {

    private static final String DATABASE_URL = "https://thebills-66df6-default-rtdb.europe-west1.firebasedatabase.app";
    private static final String ROOMS_REFERENCE = "rooms";
    private static final String USERS_REFERENCE = "users";

    private EditText editTextRoomKey;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    private Context context;
    private DatabaseReference roomsRef;
    private DatabaseReference appUsersRef;

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomsRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(ROOMS_REFERENCE);
        appUsersRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(USERS_REFERENCE);
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.join_room_dialog, null);

        builder.setView(view)
                .setTitle("join room")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("join", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        joinRoom();
                    }
                });

        editTextRoomKey = view.findViewById(R.id.editTextRoomKey);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        return builder.create();
    }

    public void joinRoom() {
        String desiredRoomKey = editTextRoomKey.getText().toString().trim();

        roomsRef.child(desiredRoomKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TheBills - joinRoom", "query done");
                if (dataSnapshot.exists()) {
                    String roomId = dataSnapshot.getKey();
                    String roomName = dataSnapshot.child("roomName").getValue(String.class);

                    if (roomId != null && roomName != null) {
                        addRoomToUser(roomId, roomName);

                        DatabaseReference usersRef = roomsRef
                                .child(roomId)
                                .child("users");

                        usersRef.child(currentUser.getUid()).setValue(true);

                        moveToRoomActivity();
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


    private void moveToRoomActivity() {
        if (context != null && context instanceof AppCompatActivity) {
            Intent intent = new Intent(context, Room.class);
            ((AppCompatActivity) context).startActivity(intent);
        }
    }

    private void addRoomToUser(String roomKey,String roomName){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put(roomKey, roomName);
        appUsersRef.child(currentUser.getUid()).child("rooms").updateChildren(updateMap);
    }

}
