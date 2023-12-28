package com.example.thebills;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

/*

to jest podstawowa komurka w probgramie,ma ona:

pamiętać swoich użytkowników
pamiętac właściciela
generować kod dostępu
pokazywać rachunki
tworzyć/usuwać rachunki
prowadzić rozliczenia

*/
public class Room extends AppCompatDialogFragment {

    private EditText editTextRoomName;

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_room_dialog, null);

        builder.setView(view)
                .setTitle("createRoom")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        editTextRoomName = view.findViewById(R.id.editTextRoomName);

        return  builder.create();
    }

    //dialog
    public void createNewRoom(){



    }

}
