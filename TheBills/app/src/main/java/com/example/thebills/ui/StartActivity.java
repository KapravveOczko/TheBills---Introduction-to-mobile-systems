package com.example.thebills.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.thebills.R;
import com.example.thebills.ui.Login;

// displays entry screen (logo)
public class StartActivity extends AppCompatActivity {

    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        start = findViewById(R.id.buttonStart);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }


    public void start() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}