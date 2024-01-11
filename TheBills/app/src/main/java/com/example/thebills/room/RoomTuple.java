package com.example.thebills.room;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


public class RoomTuple {

    private String roomId;
    private String roomName;
    private String ownerId;
    private Timestamp createDate;
    private Map<String, Boolean> users = new HashMap<>();

    // macierz wydatków
    // lista rachunków <- tworzona w momęcie dodania pierwszego rachunku

    public RoomTuple() {
    }

    public RoomTuple(String roomId, String roomName, String ownerId) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.ownerId = ownerId;
        this.createDate = new Timestamp(System.currentTimeMillis());
        this.users.put(ownerId, true);
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public Map<String, Boolean> getUsers() {
        return users;
    }

    public void addUser(String user){
        this.users.putIfAbsent(user, true);
    }

}
