package com.example.thebills.bill;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebills.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BillDataRecycleViewAdapter extends RecyclerView.Adapter<BillDataRecycleViewAdapter.MyBillDataViewHolder> {

    private List<Map.Entry<String, Float>> costMapEntries;

    public BillDataRecycleViewAdapter(Map<String, Float> costMap) {
        this.costMapEntries = new ArrayList<>(costMap.entrySet());
    }

    @NonNull
    @Override
    public MyBillDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_billdata_row, parent, false);
        return new MyBillDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBillDataViewHolder holder, int position) {
        Map.Entry<String, Float> entry = costMapEntries.get(position);

        holder.name.setText(entry.getKey());
        holder.cost.setText(String.valueOf(entry.getValue()));
    }

    @Override
    public int getItemCount() {
        return costMapEntries.size();
    }

    public static class MyBillDataViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView cost;

        public MyBillDataViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewUser);
            cost = itemView.findViewById(R.id.textViewUserCost);
        }
    }
}
