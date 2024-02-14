package com.example.thebills.results;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.util.Map;

// Class representing a result tuple with an owner and a map of costs associated with other users
public class ResultTuple {

    private final String owner;
    private final Map<String, Double> ownerCostMap; // Map representing costs associated with each other user

    public ResultTuple(String owner, Map<String, Double> ownerCostMap) {
        this.owner = owner;
        this.ownerCostMap = ownerCostMap;
    }

    // Getter method to retrieve the owner of the result
    public String getOwner() {
        return owner;
    }

    // Getter method to retrieve the cost map associated with the result
    public Map<String, Double> getOwnerCostMap() {
        return ownerCostMap;
    }

    // Override toString method to provide a string representation of the result tuple
    @NonNull
    @Override
    public String toString() {
        StringBuilder resultString = new StringBuilder();

        for (Map.Entry<String, Double> entry : ownerCostMap.entrySet()) {
            String userId = entry.getKey();
            Double value = entry.getValue();

            if (!userId.equals(owner)) {
                @SuppressLint("DefaultLocale") String formattedValue = String.format("%.2f", value);
                resultString.append(userId)
                        .append(" owns value ")
                        .append(formattedValue)
                        .append("\n");
            }
        }

        return resultString.toString();
    }
}
