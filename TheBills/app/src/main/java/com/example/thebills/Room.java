package com.example.thebills;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Room extends AppCompatDialogFragment {

    private EditText editTextRoomName;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

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

    /* Metoda do tworzenia nowego pokoju i dodawania go do bazy

    1. Uzyskanie dostępu do referencji bazy danych
    2. Utworzenie unikalnego klucza
    3. Utworzenie obiektu reprezentującego nowy pokój
    4. Dodanie nowego pokoju do bazy danych

     */
    public void createNewRoom() {
        String roomName = editTextRoomName.getText().toString().trim();
        Log.d("TheBills - createNewRoom","process of room crating started");

        if (!roomName.isEmpty() && currentUser != null) {

            DatabaseReference roomsRef = FirebaseDatabase.getInstance().getReference("rooms");
            String roomKey = roomsRef.push().getKey();
            RoomTuple newRoom = new RoomTuple(roomKey, roomName, currentUser.getUid());
            roomsRef.child(roomKey).setValue(newRoom);

            Log.d("TheBills - createNewRoom", "created room: " + newRoom.toString());
        }
    }
}
