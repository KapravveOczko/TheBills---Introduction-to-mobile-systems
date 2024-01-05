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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RoomManagerCreateRoom extends AppCompatDialogFragment {

    private static final String DATABASE_URL = "https://thebills-66df6-default-rtdb.europe-west1.firebasedatabase.app";
    private static final String ROOMS_REFERENCE = "rooms";
    private static final String USERS_REFERENCE = "users";

    private EditText editTextRoomName;
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
        View view = inflater.inflate(R.layout.create_room_dialog, null);

        builder.setView(view)
                .setTitle("create new room")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createNewRoom();
                    }
                });

        editTextRoomName = view.findViewById(R.id.editTextRoomName);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        return builder.create();
    }

    public void createNewRoom() {
        String roomName = editTextRoomName.getText().toString().trim();
        Log.d("TheBills - createNewRoom", "process of room creating started");

        if (!roomName.isEmpty() && currentUser != null) {
            String roomKey = roomsRef.push().getKey();
            RoomTuple newRoom = new RoomTuple(roomKey, roomName, currentUser.getUid());

            roomsRef.child(roomKey).setValue(newRoom).addOnSuccessListener(unused -> {

                DatabaseReference usersRef = roomsRef.child(roomKey).child("users");
                usersRef.setValue(newRoom.getUsers());

                addRoomToUser(roomKey,roomName);

                Log.d("TheBills - createNewRoom", "created room: " + newRoom.toString());
                moveToRoomActivity();
            });

            Log.d("TheBills - createNewRoom", "created room not worked");
        }
    }

    private void moveToRoomActivity() {
        if (context != null && context instanceof AppCompatActivity) {
            Intent intent = new Intent(context, Room.class);
            ((AppCompatActivity) context).startActivity(intent);
        }
    }

    private void addRoomToUser(String roomKey, String roomName){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put(roomKey, roomName);
        appUsersRef.child(currentUser.getUid()).child("rooms").updateChildren(updateMap);
    }

    private void addUserToRoom(){

    }

}



//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        boolean isCreateRoomDialog = true;
//
//        if (isCreateRoomDialog) {
//            return createCreateRoomDialog().create();
//        } else {
//            return createNewDialog().create();
//        }
//    }
//
//    private AlertDialog.Builder createCreateRoomDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.create_room_dialog, null);
//
//        builder.setView(view)
//                .setTitle("create new room")
//                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Obsługa anulowania
//                    }
//                })
//                .setPositiveButton("create", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        createNewRoom();
//                    }
//                });
//
//        return builder;
//    }
//
//    private AlertDialog.Builder createNewDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        // konf
//        builder.setTitle("New Dialog")
//                .setMessage("This is a new dialog.")
//                .setPositiveButton("Join", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//
//        return builder;
//    }