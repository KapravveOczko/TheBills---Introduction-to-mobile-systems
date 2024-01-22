package com.example.thebills.bill;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BillDataRecycleViewAdapter extends RecyclerView.Adapter<BillDataRecycleViewAdapter.MyBillDataViewHolder> {

    @NonNull
    @Override
    public MyBillDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyBillDataViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyBillDataViewHolder extends RecyclerView.ViewHolder{

        public MyBillDataViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
