package com.example.thebills.bill;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebills.R;
import com.example.thebills.user.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Adapter class for RecyclerView to manage bill data
public class BillDataRecycleViewAdapter extends RecyclerView.Adapter<BillDataRecycleViewAdapter.MyBillDataViewHolder> {

    private final List<Map.Entry<String, Double>> costMapEntries;
    private UserManager userManager;

    // Constructor to initialize adapter with bill data
    public BillDataRecycleViewAdapter(Map<String, Double> costMap) {
        this.costMapEntries = new ArrayList<>(costMap.entrySet());
        this.userManager = new UserManager();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MyBillDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_billdata_row, parent, false);
        return new MyBillDataViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyBillDataViewHolder holder, int position) {
        Map.Entry<String, Double> entry = costMapEntries.get(position);

//        Log.d("xdddddddddddddddddddddddddddddddd",entry.toString());
//        userManager.getUsername(entry.getKey(), new UserManager.GetUsernameCallback() {
//            @Override
//            public void onUsernameReceived(String name) {
//                setHolder(holder, name, entry.getValue());
//            }
//
//            @Override
//            public void onCancelled(String error) {
//                setHolder(holder, entry.getKey(), entry.getValue());
//            }
//        });

        holder.name.setText(entry.getKey());
        @SuppressLint("DefaultLocale") String formattedValue = String.format("%.2f", entry.getValue());
        holder.cost.setText(formattedValue);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return costMapEntries.size();
    }

    // Method to refresh the adapter
    @SuppressLint("NotifyDataSetChanged")
    public void refreshAdapter(){
        notifyDataSetChanged();
    }

    public void setHolder(@NonNull MyBillDataViewHolder holder, String name, Double cost){
        Log.d("TheBills: BillDataRecycleViewAdapter", "setHolder is on");
        holder.name.setText(name);
        @SuppressLint("DefaultLocale") String formattedValue = String.format("%.2f", cost);
        holder.cost.setText(formattedValue);
//        refreshAdapter();
    }

    // ViewHolder class to hold the views of a single row in the RecyclerView
    public static class MyBillDataViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView cost;

        // Constructor to initialize views of the ViewHolder
        public MyBillDataViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewUser);
            cost = itemView.findViewById(R.id.textViewUserCost);
        }

    }
}
