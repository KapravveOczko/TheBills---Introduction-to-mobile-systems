package com.example.thebills.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.thebills.R;
import com.example.thebills.bill.BillManager;
import com.example.thebills.bill.CostRecycleViewAdapter;
import com.example.thebills.room.RoomManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateBill extends AppCompatActivity {

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

        context = this;
        billManager = new BillManager();

        progressBar = findViewById(R.id.progressBarCost);
        progressBar.setVisibility(View.VISIBLE);

        setRecycleView();

    }

    public void setRecycleView() {
        billManager.getRoomUsers(new RoomManager.GetUserRoomsCallback() {

            RecyclerView recyclerView = findViewById(R.id.costRecyclerView);
            @Override
            public void onUsersReceived(Map<String, String> usersMap) {
                progressBar.setVisibility(View.INVISIBLE);
                users = new ArrayList<>(usersMap.keySet());
                CostRecycleViewAdapter adapter = new CostRecycleViewAdapter(context, usersMap);

                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onRoomsReceived(Map<String, String> roomMap) {

            }

            @Override
            public void onCancelled(String error) {
                Log.d("MainActivity", "Błąd: " + error);
            }
        });
    }
}