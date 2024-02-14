package com.example.thebills.results;

import androidx.annotation.NonNull;

import java.util.Map;

// Class representing a bill tuple with an owner and a map of costs
public class BillTuple {
    private final String owner;
    private final Map<String, Double> costmap;  // Map representing costs associated with each user

    public BillTuple(String owner, Map<String, Double> costmap) {
        this.owner = owner;
        this.costmap = costmap;
    }

    // Override toString method to provide a string representation of the bill tuple
    @NonNull
    @Override
    public String toString() {
        return "Owner: " + owner + "\n" +
                "Cost Map: " + costmap + "\n";
    }

    // Getter method to retrieve the owner of the bill
    public String getOwner() {
        return owner;
    }

    // Getter method to retrieve the cost map associated with the bill
    public Map<String, Double> getCostmap() {
        return costmap;
    }
}
