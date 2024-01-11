package com.example.thebills.bill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebills.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CostRecycleViewAdapter extends  RecyclerView.Adapter<CostRecycleViewAdapter.MyCostsViewHolder>{

    private Context context;
    private Map<String,Boolean> usersMap;

    public CostRecycleViewAdapter(Context context, Map<String, Boolean> usersMap) {
        this.context = context;
        this.usersMap = usersMap;
    }

    @NonNull
    @Override
    public MyCostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_view_cost_row, parent,false);
        return new CostRecycleViewAdapter.MyCostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCostsViewHolder holder, int position) {

        List<String> keys = new ArrayList<>(usersMap.keySet());
        String key = keys.get(position);

        holder.user.setText(key);
    }

    @Override
    public int getItemCount() {
        return usersMap.size();
    }

    public static class MyCostsViewHolder extends RecyclerView.ViewHolder {

        public TextView user;
        public TextInputEditText cost;
//        public Switch switchUserCost;

        public MyCostsViewHolder(@NonNull View itemView) {
            super(itemView);

            user = itemView.findViewById(R.id.textViewUser);
            cost = itemView.findViewById(R.id.textInputUserCost);
//            switchUserCost = itemView.findViewById(R.id.switchUserCost);

        }
    }

}
