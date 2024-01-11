package com.example.thebills.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.thebills.R;

public class BillsView extends AppCompatActivity {

    RecyclerView billsViewRecycleView;
    ProgressBar billsViewProgressBar;
    Button addBillButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_view);

        billsViewProgressBar = findViewById(R.id.progressBarBillsView);
        billsViewProgressBar.setVisibility(View.VISIBLE);
        addBillButton = findViewById(R.id.buttonAddBill);

        addBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateBill.class);
                startActivity(intent);
                finish();
            }
        });

    }
}