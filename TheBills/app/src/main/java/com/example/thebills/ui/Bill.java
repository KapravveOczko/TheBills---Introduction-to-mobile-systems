package com.example.thebills.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebills.R;
import com.example.thebills.bill.BillDataRecycleViewAdapter;
import com.example.thebills.bill.BillManager;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

public class Bill extends AppCompatActivity implements BillManager.GetBillDataCallback {

    TextView billName;
    TextView billOwner;
    TextView billDate;
    TextView billCost;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    String billKey;
    Button delete;

    RecyclerView.Adapter adapter;

    private BillManager billManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        Intent intent = getIntent();
        billKey = intent.getStringExtra("billKey");
        Log.d("TheBills: BillsView", "entered bill: " + billKey);

        billName = findViewById(R.id.textViewBillName);
        billOwner = findViewById(R.id.textViewBillOwner);
        billDate = findViewById(R.id.textViewBillDate);
        billCost = findViewById(R.id.textViewBillTotalCost);
        recyclerView = findViewById(R.id.recyclerViewBill);
        progressBar = findViewById(R.id.progressBarBill);
        delete = findViewById(R.id.buttonDeleteBill);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        billManager = new BillManager();
        getBillDataFromDatabase(billKey);
    }

    private void getBillDataFromDatabase(String billId) {
        billManager.getBillData(billId, this);
    }

    @Override
    public void onBillDataReceived(Map<String, Object> billData) {
        progressBar.setVisibility(View.INVISIBLE);

        String name = (String) billData.get("billName");
        String owner = (String) billData.get("billOwner");
        Map<String, Object> createDateMap = (Map<String, Object>) billData.get("createDate");
        Timestamp timestamp = Timestamp.valueOf(convertMapToTimestamp(createDateMap));
        float cost = Float.parseFloat(billData.get("totalCost").toString());

        billName.setText(name);
        billOwner.setText("Bill Owner: " + owner);
        billDate.setText("Create Date: " + timestamp.toString());
        billCost.setText("Total Cost: " + String.valueOf(cost));

        Map<String, Float> costMap = (Map<String, Float>) billData.get("costMap");
        adapter = new BillDataRecycleViewAdapter(costMap);
        recyclerView.setAdapter(adapter);
    }

    private String convertMapToTimestamp(Map<String, Object> createDateMap) {
        int year = ((Long) createDateMap.get("year")).intValue();
        int month = ((Long) createDateMap.get("month")).intValue();
        int day = ((Long) createDateMap.get("day")).intValue();
        int hours = ((Long) createDateMap.get("hours")).intValue();
        int minutes = ((Long) createDateMap.get("minutes")).intValue();
        int seconds = ((Long) createDateMap.get("seconds")).intValue();
        int nanos = ((Long) createDateMap.get("nanos")).intValue();

        Timestamp timestamp = new Timestamp(year, month, day, hours, minutes, seconds, nanos);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        return sdf.format(timestamp);
    }





    @Override
    public void onCancelled(String error) {
        Log.d("Bill", "error: " + error);
    }
}
