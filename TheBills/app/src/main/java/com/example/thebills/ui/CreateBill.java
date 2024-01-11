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
import android.widget.TextView;

import com.example.thebills.R;
import com.example.thebills.bill.BillManager;
import com.example.thebills.bill.CostRecycleViewAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateBill extends AppCompatActivity {

    String roomKey;

    ProgressBar progressBar;
    Button autoCalculate;
    Button acceptCost;
    TextView owner;
    TextView date;
    TextInputEditText billName;
    TextInputEditText billTotalCost;

    BillManager billManager;
    Context context;
    FirebaseAuth auth;
    FirebaseUser user;

    List<String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);

        Intent intent = getIntent();
        roomKey = intent.getStringExtra("roomId");
        Log.d("TheBills: BillView activity", "we are in room: " + roomKey);

        context = this;
        billManager = new BillManager();
        billManager.setCurrentRoom(roomKey);

        progressBar = findViewById(R.id.progressBarCost);
        progressBar.setVisibility(View.VISIBLE);

        setRecycleView();
    }

    public void setRecycleView() {
        billManager.getRoomUsers(new BillManager.GetRoomUsersCallback() {

            RecyclerView recyclerView = findViewById(R.id.costRecyclerView);

            @Override
            public void onUsersReceived(Map<String, Boolean> usersMap) {
                progressBar.setVisibility(View.INVISIBLE);
                users = new ArrayList<>(usersMap.keySet());
                CostRecycleViewAdapter adapter = new CostRecycleViewAdapter(context, usersMap);

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }

            @Override
            public void onCancelled(String error) {
                Log.d("MainActivity", "Błąd: " + error);
            }
        });
    }
}
