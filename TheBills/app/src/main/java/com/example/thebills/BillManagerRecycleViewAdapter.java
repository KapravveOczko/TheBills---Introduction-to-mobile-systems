package com.example.thebills;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BillManagerRecycleViewAdapter extends  RecyclerView.Adapter<BillManagerRecycleViewAdapter.MyBillsViewHolder> {

    @NonNull
    @Override
    public BillManagerRecycleViewAdapter.MyBillsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BillManagerRecycleViewAdapter.MyBillsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyBillsViewHolder extends RecyclerView.ViewHolder {

        public MyBillsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}