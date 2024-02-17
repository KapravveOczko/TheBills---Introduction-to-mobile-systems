// Package declaration and imports
package com.example.thebills.bill;

import android.annotation.SuppressLint;
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
import com.example.thebills.user.UserManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

// Adapter class for managing cost-related RecyclerView items
public class CostRecycleViewAdapter extends RecyclerView.Adapter<CostRecycleViewAdapter.MyCostsViewHolder> {

    private final Context context;
    private final Map<String, Boolean> usersMap;
    private final Map<String, Double> localCostsMap;
    private UserManager userManager;

    // Constructor to initialize the adapter with context and user map
    public CostRecycleViewAdapter(Context context, Map<String, Boolean> usersMap) {
        this.context = context;
        this.usersMap = usersMap;
        this.localCostsMap = new HashMap<>();
        this.userManager = new UserManager();
    }

    // Interface for listening to cost changes
    public interface CostChangeListener {
        void onCostChanged(String userId, Double newCost);
    }

    private CostChangeListener costChangeListener;

    // Method to set the cost change listener
    public void setCostChangeListener(CostChangeListener costChangeListener) {
        this.costChangeListener = costChangeListener;
    }

    // Method to create view holder
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
        String userId = keys.get(position);

        userManager.getUsername(userId, new UserManager.GetUsernameCallback() {
            @Override
            public void onUsernameReceived(String name) {
                setHolder(holder, name);
            }

            @Override
            public void onCancelled(String error) {
                setHolder(holder, userId);
            }
        });
    }

    // Method to get the item count
    @Override
    public int getItemCount() {
        return usersMap.size();
    }

    //sets proper holders, depending on gathered info
    public void setHolder(@NonNull MyCostsViewHolder holder, String name){
        holder.user.setText(name);

        holder.cost.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String newCostString = holder.cost.getText().toString();

                if (TextUtils.isEmpty(newCostString)) {
                    newCostString = "0";
                }

                Double newCost = Double.valueOf(newCostString);

                localCostsMap.put(name, newCost);

                if (costChangeListener != null) {
                    costChangeListener.onCostChanged(name, newCost);
                }
            }
        });
    }

    // View holder class
    public static class MyCostsViewHolder extends RecyclerView.ViewHolder {

        public TextView user;
        public TextInputEditText cost;

        // Constructor to initialize the view holder
        public MyCostsViewHolder(@NonNull View itemView) {
            super(itemView);

            user = itemView.findViewById(R.id.textViewUser);
            cost = itemView.findViewById(R.id.textInputUserCost);
        }
    }

    // Method to get the size of the local costs map
    public int getLocalCostsMapSize(){
        return localCostsMap.size();
    }

    // Method to get the local costs map
    public Map<String, Double> getLocalCostsMap() {
        return localCostsMap;
    }

    // Method to calculate the sum of local costs
    public Double getLocalCostMapSum() {
        Double sum = 0.0D;
        for (Map.Entry<String, Double> entry : localCostsMap.entrySet()) {
            sum += entry.getValue();
        }
        return sum;
    }

    // Method to set local costs map with automatic calculation based on total cost
    public void setLocalCostsMapAutoCalc(Double totalCost) {
        Log.d("TheBills: CostRecycleViewAdapter", "LocalCostsMap, Before setting: " + localCostsMap);

        int localCostsMapSize = getLocalCostsMapSize();
        Log.d("TheBills: CostRecycleViewAdapter", "LocalCostsMapSize, Size: " + localCostsMapSize);

        if (localCostsMapSize > 0) {
            Double costPerUser = totalCost / localCostsMapSize;
            Log.d("TotalCost", "Before division: " + totalCost);
            Log.d("CostPerUser", "After division: " + costPerUser);

            for (Map.Entry<String, Double> entry : localCostsMap.entrySet()) {
                entry.setValue(costPerUser);
            }
        }

        Log.d("TheBills: CostRecycleViewAdapter", "LocalCostsMap, After setting: " + localCostsMap);
        refreshAdapter();
    }

    // Method to refresh the adapter
    @SuppressLint("NotifyDataSetChanged")
    public void refreshAdapter(){
        notifyDataSetChanged();
    }
}
