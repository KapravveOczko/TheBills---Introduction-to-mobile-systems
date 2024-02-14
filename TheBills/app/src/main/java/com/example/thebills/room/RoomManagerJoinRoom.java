package com.example.thebills.room;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.thebills.R;

public class RoomManagerJoinRoom extends AppCompatDialogFragment {

    private RoomManager roomManager;
    private EditText editTextRoomKey;
    private Context context;

    public RoomManagerJoinRoom() {}

    public void setContext(Context context) {
        this.context = context;
    }

    public void setRoomManager(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.join_room_dialog, null);

        builder.setView(view)
                .setTitle("Join Room")
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Do nothing if Cancel button is clicked
                })
                .setPositiveButton("Join", (dialog, which) -> joinRoom());

        editTextRoomKey = view.findViewById(R.id.editTextRoomKey);

        return builder.create();
    }

    // Method to join a room
    private void joinRoom() {
        String desiredRoomKey = editTextRoomKey.getText().toString().trim();

        if (!desiredRoomKey.isEmpty()) {
            roomManager.joinRoom(desiredRoomKey);
            // roomManager.moveToRoomActivity(this.context);
        }
    }
}
