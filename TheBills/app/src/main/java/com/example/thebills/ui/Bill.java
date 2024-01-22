package com.example.thebills.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.thebills.R;
import com.example.thebills.bill.BillManager;

import java.util.Map;

public class Bill extends AppCompatActivity implements BillManager.GetBillDataCallback {

    TextView billName;
    TextView billOwner;
    TextView billDate;
    TextView billId;
    TextView billCost;
    RecyclerView recyclerView;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        billName = findViewById(R.id.textViewBillName);
        billOwner = findViewById(R.id.textViewBillOwner);
        billDate = findViewById(R.id.textViewBillDate);
        billId = findViewById(R.id.textViewBillId);
        billCost = findViewById(R.id.textViewBillTotalCost);
        recyclerView = findViewById(R.id.recyclerViewBill);
        progressBar = findViewById(R.id.progressBarBill);

    }

    @Override
    public void onBillDataReceived(Map<String, Object> billData) {

    }

    @Override
    public void onCancelled(String error) {
        Log.d("Bill", "error: " + error);
    }
}