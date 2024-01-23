package com.example.thebills.results;

import java.util.Map;

public class ResultTuple {

    private String owner;
    private Map<String, Long> ownerCostMap;

    public ResultTuple(String owner, Map<String, Long> ownerCostMap) {
        this.owner = owner;
        this.ownerCostMap = ownerCostMap;
    }

    public String getOwner() {
        return owner;
    }

    public Map<String, Long> getOwnerCostMap() {
        return ownerCostMap;
    }

    @Override
    public String toString() {
        StringBuilder resultString = new StringBuilder();

        for (Map.Entry<String, Long> entry : ownerCostMap.entrySet()) {
            String userId = entry.getKey();
            Long value = entry.getValue();

            if (!userId.equals(owner)) {
                resultString.append(userId)
                        .append(" owns value ")
                        .append(value)
                        .append("\n");
            }
        }

        return resultString.toString();
    }
}
