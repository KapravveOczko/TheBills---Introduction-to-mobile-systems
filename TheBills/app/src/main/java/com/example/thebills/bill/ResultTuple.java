package com.example.thebills.bill;

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
}
