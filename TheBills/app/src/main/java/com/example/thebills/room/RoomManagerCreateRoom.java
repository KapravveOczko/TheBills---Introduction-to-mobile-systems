package com.example.thebills.room;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.thebills.R;
import com.example.thebills.ui.MainActivity;
import com.google.firebase.database.DatabaseReference;


public class RoomManagerCreateRoom extends AppCompatDialogFragment {

    private RoomManager roomManager;
    private EditText editTextRoomName;

    private Context context;

    public RoomManagerCreateRoom() {

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setRoomManager(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    public RoomManagerCreateRoom(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.create_room_dialog, null);

        builder.setView(view)
                .setTitle("Create New Room")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createNewRoom();
                    }
                });

        editTextRoomName = view.findViewById(R.id.editTextRoomName);

        return builder.create();
    }

    private void createNewRoom() {
        String roomName = editTextRoomName.getText().toString().trim();
        Log.d("TheBills - createNewRoom", "process of room creating started");

        if (!roomName.isEmpty()) {
            roomManager.createNewRoom(roomName);
//            roomManager.moveToRoomActivity(this.context);
        }
    }


}
