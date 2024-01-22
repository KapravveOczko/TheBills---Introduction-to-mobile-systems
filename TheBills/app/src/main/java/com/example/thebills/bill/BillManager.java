package com.example.thebills.bill;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class BillManager {

    private static final String DATABASE_URL = "https://thebills-66df6-default-rtdb.europe-west1.firebasedatabase.app";

    private static final String ROOMS_REFERENCE = "rooms";
    private static final String USERS_REFERENCE = "users";
    private static final String BILLS_REFERENCE = "bills";

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    private DatabaseReference roomsRef;
    private DatabaseReference appUsersRef;
    private DatabaseReference billsRef;

    private Context context;
    private String currentRoom;

    public void setContext(Context context) {
        this.context = context;
    }

    public BillManager() {
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        roomsRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(ROOMS_REFERENCE);
        appUsersRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(USERS_REFERENCE);
        billsRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(BILLS_REFERENCE);
    }

    public String getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }

    public interface GetRoomUsersCallback {
        void onUsersReceived(Map<String, Boolean> usersMap);
        void onCancelled(String error);
    }

    public void getRoomUsers(BillManager.GetRoomUsersCallback callback) {
        roomsRef.child(getCurrentRoom()).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TheBills - room users", "query done");

                GenericTypeIndicator<Map<String, Boolean>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Boolean>>() {};
                Map<String, Boolean> dataMap = dataSnapshot.getValue(genericTypeIndicator);

                if (dataMap != null) {
                    Log.d("Firebase", "Dane jako mapa: " + dataMap.toString());
                    callback.onUsersReceived(dataMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TheBills - joinRoom", "database error");
                callback.onCancelled(databaseError.getMessage());
            }
        });
    }

    public void addBill(String roomKey, Map<String, Float> localCostMap, Timestamp createDate, float totalCost, String billName) {
        String billKey = billsRef.push().getKey();

        Map<String, Object> billData = new HashMap<>();
        billData.put("createDate", createDate);
        billData.put("totalCost", totalCost);
        billData.put("costMap", localCostMap);
        billData.put("billName", billName);

        billsRef.child(billKey).setValue(billData).addOnCompleteListener(unused -> {
            addBillToRoom(billKey, roomKey, billName);

            for (String user : localCostMap.keySet()) {
                addBillToUser(billKey, user);
            }
        });
    }

    public void addBillToUser(String billKey, String userKey) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(billKey, true);
        appUsersRef.child(userKey).child("bills").updateChildren(childUpdates);
    }

    public void addBillToRoom(String billKey, String roomKey, String billName) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(billKey, billName);
        roomsRef.child(roomKey).child("bills").updateChildren(childUpdates);
    }

    public interface GetRoomBillsCallback {
        void onBillsReceived(Map<String, String> billMap);
        void onCancelled(String error);
    }

    public void getRoomBills(String roomKey, GetRoomBillsCallback callback) {
        roomsRef.child(roomKey).child("bills").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TheBills - billsView", "query done");

                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                Map<String, String> dataMap = dataSnapshot.getValue(genericTypeIndicator);

                if (dataMap != null) {
                    Log.d("Firebase", "Dane jako mapa: " + dataMap.toString());
                    callback.onBillsReceived(dataMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TheBills - billsView", "database error");
                callback.onCancelled(databaseError.getMessage());
            }
        });
    }

    public interface GetBillDataCallback {
        void onBillDataReceived(Map<String, Object> billData);
        void onCancelled(String error);
    }

    public void getBillData(String billId, GetBillDataCallback callback) {
        billsRef.child(billId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TheBills - BillManager", "Query done");

                GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {};
                Map<String, Object> billData = dataSnapshot.getValue(genericTypeIndicator);

                if (billData != null) {
                    Log.d("Firebase", "Dane rachunku jako mapa: " + billData.toString());
                    callback.onBillDataReceived(billData);
                } else {
                    Log.d("Firebase", "Brak danych dla rachunku o ID: " + billId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TheBills - BillManager", "Database error");
                callback.onCancelled(databaseError.getMessage());
            }
        });
    }
}
