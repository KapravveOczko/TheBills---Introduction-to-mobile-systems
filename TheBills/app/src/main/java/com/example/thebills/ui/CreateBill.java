package com.example.thebills.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thebills.R;
import com.example.thebills.user.UserManager;
import com.example.thebills.bill.BillManager;
import com.example.thebills.bill.CostRecycleViewAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateBill extends AppCompatActivity {

    // UI elements
    ProgressBar progressBar;
    Button autoCalculate;
    Button acceptCost;
    TextView owner;
    TextView date;
    TextInputEditText billName;
    TextInputEditText billTotalCost;
    FirebaseAuth auth;
    FirebaseUser user;
    String roomKey;
    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
    BillManager billManager;
    UserManager userManager;
    Context context;
    List<String> users;
    CostRecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);

        // Initialize Firebase Authentication instance
        auth = FirebaseAuth.getInstance();

        // Get room key from intent
        Intent intent = getIntent();
        roomKey = intent.getStringExtra("roomId");
        Log.d("TheBills: CreateBill activity", "in room: " + roomKey);

        // Initialize context, managers, and set current room key
        context = this;
        billManager = new BillManager();
        billManager.setCurrentRoom(roomKey);
        userManager = new UserManager();

        // Find views by their IDs
        owner = findViewById(R.id.textViewOwner);
        user = auth.getCurrentUser();
        owner.setText(user.getUid());

        date = findViewById(R.id.textViewDate);
        date.setText(currentTime.toString());

        progressBar = findViewById(R.id.progressBarCost);
        progressBar.setVisibility(View.VISIBLE);

        billName = findViewById(R.id.textInputBillName);
        billTotalCost = findViewById(R.id.textInputTotalCost);
        autoCalculate = findViewById(R.id.buttonAutoCalculateCost);
        acceptCost = findViewById(R.id.buttonCalculateCost);

        // Auto-calculate cost button functionality
        autoCalculate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(billTotalCost.getText())) {
                Toast.makeText(context, "total cost is null", Toast.LENGTH_SHORT).show();
            } else {
                Double totalCost = Double.parseDouble(billTotalCost.getText().toString());
                adapter.setLocalCostsMapAutoCalc(totalCost);
                adapter.setLocalCostsMapAutoCalc(totalCost);
            }
        });

        // Accept cost button functionality
        acceptCost.setOnClickListener(v -> {
            Map<String, Double> localCostsMap = adapter.getLocalCostsMap();

            for (Map.Entry<String, Double> entry : localCostsMap.entrySet()) {
                Log.d("TheBills: CreateBill activity", "Local cost map entry: User: " + entry.getKey() + ", Cost: " + entry.getValue());
            }

            if (TextUtils.isEmpty(billName.getText())){
                Toast.makeText(context, "bill name is null", Toast.LENGTH_SHORT).show();
            } else {
                if (TextUtils.isEmpty(billTotalCost.getText())) {
                    Toast.makeText(context, "total cost is null", Toast.LENGTH_SHORT).show();
                } else {
                    Double totalCost = Double.parseDouble(billTotalCost.getText().toString());
                    if (Math.abs(totalCost - adapter.getLocalCostMapSum()) > 0.01){
                        Toast.makeText(context, "total cost is not equal to given cost", Toast.LENGTH_SHORT).show();
                    } else {
                        // Add bill to the database
                        billManager.addBill(roomKey, adapter.getLocalCostsMap(), currentTime, totalCost, billName.getText().toString(), owner.getText().toString());
                        new Handler().postDelayed(() -> {
                            Toast.makeText(context, "bill created and added", Toast.LENGTH_SHORT).show();
                            moveToRoom();
                        }, 500);
                    }
                }
            }
        });

        // Set owner's username
        setUsername(new UserManager.GetUsernameCallback() {
            @Override
            public void onUsernameReceived(String name) {
                owner.setText(name);
            }

            @Override
            public void onCancelled(String error) {
                Toast.makeText(CreateBill.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
        setRecycleView();
    }

    // Set up the RecyclerView for displaying users in the current room
    public void setRecycleView() {
        billManager.getRoomUsers(new BillManager.GetRoomUsersCallback() {
            RecyclerView recyclerView = findViewById(R.id.costRecyclerView);

            @Override
            public void onUsersReceived(Map<String, Boolean> usersMap) {
                progressBar.setVisibility(View.INVISIBLE);
                users = new ArrayList<>(usersMap.keySet());
                adapter = new CostRecycleViewAdapter(context, usersMap);

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }

            @Override
            public void onCancelled(String error) {
                Log.d("TheBills: CreateBill activity", "error: " + error);
            }
        });
    }

    // Move to the Room activity
    public void moveToRoom(){
        Intent intent = new Intent(context, Room.class);
        intent.putExtra("roomId", roomKey);
        startActivity(intent);
        finish();
    }

    // Set the current user's username
    public void setUsername(UserManager.GetUsernameCallback callback) {
        userManager.getUsername(user.getUid(), new UserManager.GetUsernameCallback() {
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
