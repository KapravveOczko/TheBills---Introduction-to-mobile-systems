package com.example.thebills.bill;

import java.util.Map;

public class BillTuple {
    private String owner;
    private Map<String, Long> costmap;

    public BillTuple(String owner, Map<String, Long> costmap) {
        this.owner = owner;
        this.costmap = costmap;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Owner: ").append(owner).append("\n");
        stringBuilder.append("Cost Map: ").append(costmap).append("\n");
        return stringBuilder.toString();
    }

    public String getOwner() {
        return owner;
    }

    public Map<String, Long> getCostmap() {
        return costmap;
    }
}