package com.example.thebills.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebills.R;
import com.example.thebills.bill.BillManager;
import com.example.thebills.bill.BillManagerRecycleViewAdapter;
import com.example.thebills.bill.BillRecycleViewEvent;

import java.util.Map;

public class BillsView extends AppCompatActivity implements BillRecycleViewEvent {

    String roomKey;
    ProgressBar billsViewProgressBar;
    Button addBillButton;
    Context context;
    BillManagerRecycleViewAdapter adapter;
    BillManager billManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_view);

        Intent intent = getIntent();
        roomKey = intent.getStringExtra("roomId");
        Log.d("TheBills: BillView activity", "in room: " + roomKey);

        // Initialize context and bill manager
        context = this;
        billManager = new BillManager();
        billManager.setCurrentRoom(roomKey);

        billsViewProgressBar = findViewById(R.id.progressBarBillsView);
        addBillButton = findViewById(R.id.buttonAddBill);

        // Add bill button click listener
        addBillButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(getApplicationContext(), CreateBill.class);
            intent1.putExtra("roomId",roomKey);
            startActivity(intent1);
            finish();
        });
        setRecycleView();

    }

    // Refresh data on resume
    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    // Refresh data method
    private void refreshData() {
        RecyclerView recyclerView = findViewById(R.id.billsRecyclerView);
        if (recyclerView != null) {
            setRecycleView();
        }
    }

    // Set up the RecyclerView to display bills in the current room
    public void setRecycleView() {
        billsViewProgressBar.setVisibility(View.INVISIBLE);

        // Retrieve bills for the current room from the database
        billManager.getRoomBills(roomKey, new BillManager.GetRoomBillsCallback() {
            RecyclerView recyclerView = findViewById(R.id.billsRecyclerView);

            @Override
            public void onBillsReceived(Map<String, String> billsMap) {

                // Initialize and set up the adapter
                adapter = new BillManagerRecycleViewAdapter(context, billsMap, BillsView.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

                // Hide progress bar
                billsViewProgressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(String error) {
                Log.d("TheBills: BillView activity", "error: " + error);
                billsViewProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    // Handle item click in the RecyclerView
    @Override
    public void onItemClick(int position, String billKey) {
        Toast.makeText(this, "entering Bill", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, Bill.class);
        intent.putExtra("billKey", billKey);
        intent.putExtra("roomKey", roomKey);
        startActivity(intent);
        finish();
    }
}
