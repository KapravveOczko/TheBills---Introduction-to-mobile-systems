package com.example.thebills.room;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.thebills.R;

public class RoomManagerCreateRoom extends AppCompatDialogFragment {

    private RoomManager roomManager;
    private EditText editTextRoomName;
    private Context context;

    public RoomManagerCreateRoom() {}

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
        View view = inflater.inflate(R.layout.create_room_dialog, null);

        builder.setView(view)
                .setTitle("Create New Room")
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Do nothing if Cancel button is clicked
                })
                .setPositiveButton("Create", (dialog, which) -> createNewRoom());

        editTextRoomName = view.findViewById(R.id.editTextRoomName);

        return builder.create();
    }

    // Method to create a new room
    private void createNewRoom() {
        String roomName = editTextRoomName.getText().toString().trim();
        Log.d("TheBills: RoomManagerCreateRoom", "process of room creating started");

        if (!roomName.isEmpty()) {
            roomManager.createNewRoom(roomName);
            // roomManager.moveToRoomActivity(this.context);
        }
    }
}
