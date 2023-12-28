package com.example.thebills;

public class RoomTuple {

    private String roomId;
    private String roomName;
    private String ownerId;
    // macierz wydatków
    // lista rachunków

    public RoomTuple() {
    }

    public RoomTuple(String roomId, String roomName, String ownerId) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.ownerId = ownerId;
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

}
