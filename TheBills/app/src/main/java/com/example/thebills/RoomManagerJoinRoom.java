package com.example.thebills;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;


public class RoomManagerJoinRoom extends AppCompatDialogFragment {

    private RoomManager roomManager;
    private EditText editTextRoomKey;
    private Context context;


    public RoomManagerJoinRoom() {

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setRoomManager(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    public RoomManagerJoinRoom(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.join_room_dialog, null);

        builder.setView(view)
                .setTitle("Join Room")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing on cancel
                    }
                })
                .setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        joinRoom();
                    }
                });

        editTextRoomKey = view.findViewById(R.id.editTextRoomKey);

        return builder.create();
    }

    private void joinRoom() {
        String desiredRoomKey = editTextRoomKey.getText().toString().trim();

        if (!desiredRoomKey.isEmpty()) {
            roomManager.joinRoom(desiredRoomKey);
            roomManager.moveToRoomActivity(this.context);
    }}
}
