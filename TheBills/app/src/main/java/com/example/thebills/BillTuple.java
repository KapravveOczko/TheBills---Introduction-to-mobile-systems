package com.example.thebills;

import java.util.UUID;

public class BillTuple {
    private float cost;
    private UUID user;
    private boolean paid;

    public BillTuple(float cost, UUID user, boolean paid) {
        this.cost = cost;
        this.user = user;
        this.paid = paid;
    }

    public float getCost() {
        return cost;
    }

    public UUID getUser() {
        return user;
    }

    public boolean isPaid() {
        return paid;
    }

//    @Override
//    public String toString() {
//        return String.format("CostTuple{cost=%.2f, user=%s, paid=%b}", cost, user.toString(), paid);
//    }
}
