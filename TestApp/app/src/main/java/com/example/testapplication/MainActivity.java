package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.testapplication.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String string, name, surname;

    FirebaseDatabase db;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                string = binding.textPlace.getText().toString();
                name = "kura";
                surname = "jajo";

                if(!string.isEmpty()){
                    Sender sender = new Sender(string, name, surname);
                    db = FirebaseDatabase.getInstance("https://testapplication-158d8-default-rtdb.europe-west1.firebasedatabase.app");
                    reference = db.getReference("path");
                    reference.child(string).setValue(sender).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(MainActivity.this, "siadlo", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}