package com.example.thebills.results;

import com.example.thebills.UserManager;

import java.util.Map;

public class ResultTuple {

    private String owner;
    private Map<String, Double> ownerCostMap;
    private UserManager userManager;

    public ResultTuple(String owner, Map<String, Double> ownerCostMap, UserManager userManager) {
        this.owner = owner;
        this.ownerCostMap = ownerCostMap;
        this.userManager = userManager;
    }

    public String getOwner() {
        return owner;
    }

    public Map<String, Double> getOwnerCostMap() {
        return ownerCostMap;
    }

    @Override
    public String toString() {
        StringBuilder resultString = new StringBuilder();

        for (Map.Entry<String, Double> entry : ownerCostMap.entrySet()) {
            String userId = entry.getKey();
            Double value = entry.getValue();

            if (!userId.equals(owner)) {
                String formattedValue = String.format("%.2f", value);
                resultString.append(userId)
                        .append(" owns value ")
                        .append(formattedValue)
                        .append("\n");
            }
        }

        return resultString.toString();
    }
}
