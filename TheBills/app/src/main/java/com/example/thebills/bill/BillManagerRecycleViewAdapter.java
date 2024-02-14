// Package declaration and imports
package com.example.thebills.bill;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebills.R;

import java.util.Map;

// Adapter class for managing bill-related RecyclerView items
public class BillManagerRecycleViewAdapter extends RecyclerView.Adapter<BillManagerRecycleViewAdapter.MyBillsViewHolder> {

    private final Context context;
    private final Map<String, String> billMap;
    private final String[] billIds;
    private final BillRecycleViewEvent listener;

    // Constructor to initialize the adapter with context, bill map, and event listener
    public BillManagerRecycleViewAdapter(Context context, Map<String, String> billMap, BillRecycleViewEvent listener) {
        this.context = context;
        this.billMap = billMap;
        this.billIds = billMap.keySet().toArray(new String[0]);
        this.listener = listener;
    }

    // Method to create view holder
    @NonNull
    @Override
    public MyBillsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_view_bill_row, parent, false);
        return new MyBillsViewHolder(view);
    }

    // Method to bind data to the view holder
    @Override
    public void onBindViewHolder(@NonNull MyBillsViewHolder holder, int position) {
        String billId = billIds[position];
        String billName = billMap.get(billId);

        holder.textViewRoomName.setText(billName);
        holder.textViewBillID.setText(billId);

        holder.cardView.setOnClickListener(v -> {
            Log.d("TheBills: BillManagerRecycleViewAdapter", "Entering bill: " + billId);
            listener.onItemClick(holder.getAdapterPosition(), billId);
        });
    }

    // Method to get the item count
    @Override
    public int getItemCount() {
        return billMap.size();
    }

    // View holder class
    public static class MyBillsViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewRoomName;
        public TextView textViewBillID;
        public CardView cardView;

        // Constructor to initialize the view holder
        public MyBillsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRoomName = itemView.findViewById(R.id.textViewRoomName);
            textViewBillID = itemView.findViewById(R.id.textViewBillID);
            cardView = itemView.findViewById(R.id.cardViewRecycleViewBills);
        }
    }
}
