// Package declaration and imports
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

// Class for managing bill-related operations
public class BillManager {

    // Firebase database URLs and references
    private static final String DATABASE_URL = "https://thebills-66df6-default-rtdb.europe-west1.firebasedatabase.app";
    private static final String ROOMS_REFERENCE = "rooms";
    private static final String USERS_REFERENCE = "users";
    private static final String BILLS_REFERENCE = "bills";

    private final FirebaseAuth auth;
    private final FirebaseUser currentUser;
    private final DatabaseReference roomsRef;
    private final DatabaseReference appUsersRef;
    private final DatabaseReference billsRef;

    private Context context;
    private String currentRoom;

    // Setter method to set the context
    public void setContext(Context context) {
        this.context = context;
    }

    // Constructor to initialize FirebaseAuth, FirebaseUser, and Firebase database references
    public BillManager() {
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        roomsRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(ROOMS_REFERENCE);
        appUsersRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(USERS_REFERENCE);
        billsRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference(BILLS_REFERENCE);
    }

    // Getter and setter methods for the current room
    public String getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }

    // Interface for callback to get room users
    public interface GetRoomUsersCallback {
        void onUsersReceived(Map<String, Boolean> usersMap);
        void onCancelled(String error);
    }

    // Method to get room users
    public void getRoomUsers(BillManager.GetRoomUsersCallback callback) {
        roomsRef.child(getCurrentRoom()).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TheBills: BillManager", "room users - query done");

                GenericTypeIndicator<Map<String, Boolean>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Boolean>>() {};
                Map<String, Boolean> dataMap = dataSnapshot.getValue(genericTypeIndicator);

                if (dataMap != null) {
                    Log.d("TheBills: BillManager", "Firebase, Data as map: " + dataMap);
                    callback.onUsersReceived(dataMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TheBills: BillManager", "database error");
                callback.onCancelled(databaseError.getMessage());
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////

    // Method to add a bill
    public void addBill(String roomKey, Map<String, Double> localCostMap, Timestamp createDate, Double totalCost, String billName, String billOwner) {
        String billKey = billsRef.push().getKey();

        for (Map.Entry<String, Double> entry : localCostMap.entrySet()) {
            String userId = entry.getKey();
            Double cost = entry.getValue();
            localCostMap.put(userId, cost + 0.0001D);
        }

        Map<String, Object> billData = new HashMap<>();
        billData.put("createDate", createDate);
        billData.put("totalCost", totalCost);
        billData.put("costMap", localCostMap);
        billData.put("billName", billName);
        billData.put("billOwner", billOwner);

        billsRef.child(billKey).setValue(billData).addOnCompleteListener(unused -> {
            addBillToRoom(billKey, roomKey, billName);

            for (String user : localCostMap.keySet()) {
                addBillToUser(billKey, user);
            }
        });
    }

    // Method to add a bill to a user
    public void addBillToUser(String billKey, String userKey) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(billKey, true);
        appUsersRef.child(userKey).child("bills").updateChildren(childUpdates);
    }

    // Method to add a bill to a room
    public void addBillToRoom(String billKey, String roomKey, String billName) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(billKey, billName);
        roomsRef.child(roomKey).child("bills").updateChildren(childUpdates);
    }

    ///////////////////////////////////////////////////////////////////////////
//    Method to get room bills

    // Interface for callback to get room bills
    public interface GetRoomBillsCallback {
        void onBillsReceived(Map<String, String> billMap);
        void onNullReceived();
        void onCancelled(String error);
    }

    // Method to get room bills
    public void getRoomBills(String roomKey, GetRoomBillsCallback callback) {
        roomsRef.child(roomKey).child("bills").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TheBills: BillManager", "query done");

                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                Map<String, String> dataMap = dataSnapshot.getValue(genericTypeIndicator);

                if (dataMap != null) {
                    Log.d("TheBills: BillManager", "Firebase, Data as map: " + dataMap);
                    callback.onBillsReceived(dataMap);
                }
                else {
                    callback.onNullReceived();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TheBills: BillManager", "database error");
                callback.onCancelled(databaseError.getMessage());
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////
//  Method to get bill data

    // Interface for callback to get bill data
    public interface GetBillDataCallback {
        void onBillDataReceived(Map<String, Object> billData);
        void onCancelled(String error);
    }

    // Method to get bill data
    public void getBillData(String billId, GetBillDataCallback callback) {
        billsRef.child(billId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TheBills: BillManager", "Query done");

                GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {};
                Map<String, Object> billData = dataSnapshot.getValue(genericTypeIndicator);

                if (billData != null) {
                    Log.d("TheBills: BillManager", "Firebase, Bill data as map: " + billData);
                    callback.onBillDataReceived(billData);
                } else {
                    Log.d("TheBills: BillManager", "Firebase, No data for bill with ID: " + billId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TheBills: BillManager", "Database error");
                callback.onCancelled(databaseError.getMessage());
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////
}
