package com.example.thebills.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebills.R;
import com.example.thebills.UserManager;
import com.example.thebills.bill.BillDataRecycleViewAdapter;
import com.example.thebills.bill.BillManager;
import com.example.thebills.remover.Remover;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Bill extends AppCompatActivity implements BillManager.GetBillDataCallback {

    // UI elements
    TextView billName;
    TextView billOwner;
    TextView billDate;
    TextView billCost;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Button delete;

    // Keys
    String billKey;
    String roomKey;

    // User manager
    UserManager userManager;

    // List of users
    ArrayList<String> users;

    // Adapter for RecyclerView
    RecyclerView.Adapter<BillDataRecycleViewAdapter.MyBillDataViewHolder> adapter;

    // Bill manager
    private BillManager billManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        // Get intent data
        Intent intent = getIntent();
        billKey = intent.getStringExtra("billKey");
        roomKey = intent.getStringExtra("roomKey");
        Log.d("TheBills: Bill activity", "entered bill: " + billKey);

        // Initialize remover and user manager
        Remover remover = new Remover(this);
        userManager = new UserManager();

        // Find views by their IDs
        billName = findViewById(R.id.textViewBillName);
        billOwner = findViewById(R.id.textViewBillOwner);
        billDate = findViewById(R.id.textViewBillDate);
        billCost = findViewById(R.id.textViewBillTotalCost);
        recyclerView = findViewById(R.id.recyclerViewBill);
        progressBar = findViewById(R.id.progressBarBill);
        delete = findViewById(R.id.buttonDeleteBill);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize bill manager
        billManager = new BillManager();
        // Retrieve bill data from database
        getBillDataFromDatabase(billKey);

        // Delete button click listener
        delete.setOnClickListener(v -> {
            remover.removeBillById(billKey, roomKey, users);
            moveAfterDeleting();
        });
    }

    // Retrieve bill data from the database
    private void getBillDataFromDatabase(String billId) {
        billManager.getBillData(billId, this);
    }

    // Handle bill data received from the database
    @SuppressLint("SetTextI18n")
    @Override
    public void onBillDataReceived(Map<String, Object> billData) {
        progressBar.setVisibility(View.INVISIBLE);

        // Extract bill data
        String name = (String) billData.get("billName");
        String owner = (String) billData.get("billOwner");
        Map<String, Object> createDateMap = (Map<String, Object>) billData.get("createDate");
        Timestamp timestamp = Timestamp.valueOf(convertMapToTimestamp(createDateMap));
        double cost = Double.parseDouble(billData.get("totalCost").toString());

        // Set text views
        billName.setText(name);
        billDate.setText("Create Date: " + timestamp.toString());
        billCost.setText("Total Cost: " + cost);

        // Set up RecyclerView adapter
        Map<String, Double> costMap = (Map<String, Double>) billData.get("costMap");
        users = new ArrayList<>(costMap.keySet());
        adapter = new BillDataRecycleViewAdapter(costMap);
        recyclerView.setAdapter(adapter);

        // Set owner username
        setUsername(owner, new UserManager.GetUsernameCallback() {
            @Override
            public void onUsernameReceived(String name) {
                billOwner.setText("Bill Owner: " + name);
            }

            @Override
            public void onCancelled(String error) {
                Toast.makeText(Bill.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Handle database error
    @Override
    public void onCancelled(String error) {
        Log.d("TheBills: Bill activity", "error: " + error);
    }

    // Convert map to timestamp string
    private String convertMapToTimestamp(Map<String, Object> createDateMap) {
        int year = ((Long) Objects.requireNonNull(createDateMap.get("year"))).intValue();
        int month = ((Long) Objects.requireNonNull(createDateMap.get("month"))).intValue();
        int day = ((Long) Objects.requireNonNull(createDateMap.get("day"))).intValue();
        int hours = ((Long) Objects.requireNonNull(createDateMap.get("hours"))).intValue();
        int minutes = ((Long) Objects.requireNonNull(createDateMap.get("minutes"))).intValue();
        int seconds = ((Long) Objects.requireNonNull(createDateMap.get("seconds"))).intValue();
        int nanos = ((Long) Objects.requireNonNull(createDateMap.get("nanos"))).intValue();

        Timestamp timestamp = new Timestamp(year, month, day, hours, minutes, seconds, nanos);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        return sdf.format(timestamp);
    }

    // Move to Room activity after deleting bill
    private void moveAfterDeleting() {
        Intent intent = new Intent(this, Room.class);
        intent.putExtra("roomId", roomKey);
        startActivity(intent);
        finish();
    }

    // Set username for owner
    public void setUsername(String uid, UserManager.GetUsernameCallback callback) {
        userManager.getUsername(uid, new UserManager.GetUsernameCallback() {
            @Override
            public void onUsernameReceived(String name) {
                callback.onUsernameReceived(name);
            }

            @Override
            public void onCancelled(String error) {
                callback.onCancelled(error);
            }
        });
    }
}
