package com.example.thebills.bill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebills.R;

import java.util.Map;

public class BillManagerRecycleViewAdapter extends RecyclerView.Adapter<BillManagerRecycleViewAdapter.MyBillsViewHolder> {

    private Context context;
    private Map<String, String> billMap;
    private String[] billIds;

    public BillManagerRecycleViewAdapter(Context context, Map<String, String> billMap) {
        this.context = context;
        this.billMap = billMap;
        this.billIds = billMap.keySet().toArray(new String[0]);
    }

    @NonNull
    @Override
    public MyBillsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_view_bill_row, parent, false);
        return new MyBillsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBillsViewHolder holder, int position) {
        String billId = billIds[position];
        String billName = billMap.get(billId);

        holder.textViewRoomName.setText(billName);
        holder.textViewBillID.setText(billId);
    }

    @Override
    public int getItemCount() {
        return billMap.size();
    }

    public static class MyBillsViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewRoomName;
        public TextView textViewBillID;

        public MyBillsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRoomName = itemView.findViewById(R.id.textViewRoomName);
            textViewBillID = itemView.findViewById(R.id.textViewBillID);
        }
    }
}
