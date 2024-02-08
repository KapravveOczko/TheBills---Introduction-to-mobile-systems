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
import com.example.thebills.UserManager;
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
    Timestamp currentTime = new Timestamp(System.currentTimeMillis());

    UserManager userManager;

    List<String> users;
    CostRecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);
        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        roomKey = intent.getStringExtra("roomId");
        Log.d("TheBills: BillView activity", "we are in room: " + roomKey);

        context = this;
        billManager = new BillManager();
        billManager.setCurrentRoom(roomKey);
        userManager = new UserManager();

        owner = findViewById(R.id.textViewOwner);
        user = auth.getCurrentUser();

        // to do wyjebania
        owner.setText(user.getUid());

        date = findViewById(R.id.textViewDate);
        date.setText(currentTime.toString());

        progressBar = findViewById(R.id.progressBarCost);
        progressBar.setVisibility(View.VISIBLE);

        billName = findViewById(R.id.textInputBillName);
        billTotalCost = findViewById(R.id.textInputTotalCost);
        autoCalculate = findViewById(R.id.buttonAutoCalculateCost);
        acceptCost = findViewById(R.id.buttonCalculateCost);

        // it's not a bug it's a feature
        // 1 click -> all to owner
        // 2 click -> equal distribution
        autoCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(billTotalCost.getText())) {
                    Toast.makeText(context, "total cost is null", Toast.LENGTH_SHORT).show();
                } else {
                    Double totalCost = Double.parseDouble(billTotalCost.getText().toString());
                    adapter.setLocalCostsMapAutoCalc(totalCost);
                    adapter.setLocalCostsMapAutoCalc(totalCost);
                }
            }
        });

        acceptCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Double> localCostsMap = adapter.getLocalCostsMap();

                for (Map.Entry<String, Double> entry : localCostsMap.entrySet()) {
                    Log.d("LocalCostsMap", "User: " + entry.getKey() + ", Cost: " + entry.getValue());
                }

                if (TextUtils.isEmpty(billName.getText())){
                    Toast.makeText(context, "bill name is null", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (TextUtils.isEmpty(billTotalCost.getText())) {
                        Toast.makeText(context, "total cost is null", Toast.LENGTH_SHORT).show();
                    } else {
                        Double totalCost = Double.parseDouble(billTotalCost.getText().toString());
//                        if (totalCost != adapter.getLocalCostMapSum()) {
                        if (Math.abs(totalCost - adapter.getLocalCostMapSum()) > 0.01){
                            Toast.makeText(context, "total cost is not equal to given cost", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            billManager.addBill(roomKey,adapter.getLocalCostsMap(),currentTime,totalCost,billName.getText().toString(), user.getUid());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "bill created and added", Toast.LENGTH_SHORT).show();
                                    moveToRoom();
                                }
                            }, 500);
                        }
                    }
                }

            }
        });

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

    public void setRecycleView() {
        billManager.getRoomUsers(new BillManager.GetRoomUsersCallback() {
            RecyclerView recyclerView = findViewById(R.id.costRecyclerView);

            @Override
            public void onUsersReceived(Map<String, Boolean> usersMap) {
                progressBar.setVisibility(View.INVISIBLE);
                users = new ArrayList<>(usersMap.keySet());
                adapter = new CostRecycleViewAdapter(context, usersMap);

//                adapter.setCostChangeListener(new CostRecycleViewAdapter.CostChangeListener() {
//                    @Override
//                    public void onCostChanged(String userId, String newCost) {
//                        updateCostInDatabase(userId, newCost);
//                    }
//                });

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }

            @Override
            public void onCancelled(String error) {
                Log.d("MainActivity", "error: " + error);
            }
        });
    }

    public void moveToRoom(){
        Intent intent = new Intent(context, Room.class);
        intent.putExtra("roomId", roomKey);
        startActivity(intent);
        finish();
    }

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