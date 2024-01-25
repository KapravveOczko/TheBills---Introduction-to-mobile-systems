package com.example.thebills.results;

import java.util.Map;

public class ResultTuple {

    private String owner;
    private Map<String, Double> ownerCostMap;

    public ResultTuple(String owner, Map<String, Double> ownerCostMap) {
        this.owner = owner;
        this.ownerCostMap = ownerCostMap;
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
