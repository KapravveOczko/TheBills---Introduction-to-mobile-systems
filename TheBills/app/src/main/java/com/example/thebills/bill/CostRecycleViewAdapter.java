package com.example.thebills.bill;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebills.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CostRecycleViewAdapter extends RecyclerView.Adapter<CostRecycleViewAdapter.MyCostsViewHolder> {

    private Context context;
    private Map<String, Boolean> usersMap;
    private Map<String, Double> localCostsMap;

    public CostRecycleViewAdapter(Context context, Map<String, Boolean> usersMap) {
        this.context = context;
        this.usersMap = usersMap;
        this.localCostsMap = new HashMap<>();
    }

    public interface CostChangeListener {
        void onCostChanged(String userId, Double newCost);
    }

    private CostChangeListener costChangeListener;

    public void setCostChangeListener(CostChangeListener costChangeListener) {
        this.costChangeListener = costChangeListener;
    }

    @NonNull
    @Override
    public MyCostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_view_cost_row, parent, false);
        return new CostRecycleViewAdapter.MyCostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCostsViewHolder holder, int position) {
        List<String> keys = new ArrayList<>(usersMap.keySet());
        String key = keys.get(position);

        holder.user.setText(key);

        holder.cost.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String newCostString = holder.cost.getText().toString();

                if (TextUtils.isEmpty(newCostString)) {
                    newCostString = "0";
                }

                Double newCost = Double.valueOf(newCostString);

                localCostsMap.put(key, newCost);

                if (costChangeListener != null) {
                    costChangeListener.onCostChanged(key, newCost);
                }
            }
        });

        Double localCost = localCostsMap.get(key);
        if (localCost != null) {
            holder.cost.setText(String.valueOf(localCost));
        }
    }

    @Override
    public int getItemCount() {
        return usersMap.size();
    }


    public static class MyCostsViewHolder extends RecyclerView.ViewHolder {

        public TextView user;
        public TextInputEditText cost;

        public MyCostsViewHolder(@NonNull View itemView) {
            super(itemView);

            user = itemView.findViewById(R.id.textViewUser);
            cost = itemView.findViewById(R.id.textInputUserCost);
        }
    }

    public int getLocalCostsMapSize(){
        return localCostsMap.size();
    }

    public Map<String, Double> getLocalCostsMap() {
        return localCostsMap;
    }

    public Double getLocalCostMapSum() {
        Double sum = 0.0D;
        for (Map.Entry<String, Double> entry : localCostsMap.entrySet()) {
            sum += entry.getValue();
        }
        return sum;
    }

    public void setLocalCostsMapAutoCalc(Double totalCost) {
        Log.d("LocalCostsMapBefore", "Before setting: " + localCostsMap.toString());

        int localCostsMapSize = getLocalCostsMapSize();
        Log.d("LocalCostsMapSize", "Size: " + localCostsMapSize);

        if (localCostsMapSize > 0) {
            Double costPerUser = totalCost / localCostsMapSize;
            Log.d("TotalCost", "Before division: " + totalCost);
            Log.d("CostPerUser", "After division: " + costPerUser);

            for (Map.Entry<String, Double> entry : localCostsMap.entrySet()) {
                entry.setValue(costPerUser);
            }
        }

        Log.d("LocalCostsMapAfter", "After setting: " + localCostsMap.toString());
        refreshAdapter();
    }


    public void refreshAdapter(){
        notifyDataSetChanged();
    }



}
