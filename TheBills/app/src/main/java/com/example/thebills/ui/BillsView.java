package com.example.thebills.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.thebills.R;

public class BillsView extends AppCompatActivity {

    RecyclerView billsViewRecycleView;
    ProgressBar billsViewProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_view);

        billsViewProgressBar = findViewById(R.id.progressBarBillsView);
        billsViewProgressBar.setVisibility(View.VISIBLE);

    }
}