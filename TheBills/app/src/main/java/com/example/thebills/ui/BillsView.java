package com.example.thebills.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.thebills.R;
import com.example.thebills.bill.BillManager;
import com.example.thebills.bill.BillManagerRecycleViewAdapter;
import com.example.thebills.bill.BillRecycleViewEvent;

import java.util.Map;

public class BillsView extends AppCompatActivity implements BillRecycleViewEvent {

    String roomKey;
    RecyclerView billsViewRecycleView;
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
        Log.d("TheBills: BillView activity", "we are in room: " + roomKey);

        context = this;
        billManager = new BillManager();
        billManager.setCurrentRoom(roomKey);
        billsViewProgressBar = findViewById(R.id.progressBarBillsView);
        billsViewProgressBar.setVisibility(View.VISIBLE);
        addBillButton = findViewById(R.id.buttonAddBill);

        addBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateBill.class);
                intent.putExtra("roomId",roomKey);
                startActivity(intent);
                finish();
            }
        });

        setRecycleView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        RecyclerView recyclerView = findViewById(R.id.billsRecyclerView);
        if (recyclerView != null) {
            setRecycleView();
        }
    }

    public void setRecycleView() {
        billManager.getRoomBills(roomKey, new BillManager.GetRoomBillsCallback() {
            RecyclerView recyclerView = findViewById(R.id.billsRecyclerView);

            @Override
            public void onBillsReceived(Map<String, String> billsMap) {
                billsViewProgressBar.setVisibility(View.INVISIBLE);
                adapter = new BillManagerRecycleViewAdapter(context, billsMap, BillsView.this);

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }

            @Override
            public void onCancelled(String error) {
                Log.d("BillView", "error: " + error);
            }
        });
    }

    @Override
    public void onItemClick(int position, String billKey) {
        Toast.makeText(this, "entering Bill", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, Bill.class);
        intent.putExtra("billKey", billKey);
        startActivity(intent);
        finish();
    }
}