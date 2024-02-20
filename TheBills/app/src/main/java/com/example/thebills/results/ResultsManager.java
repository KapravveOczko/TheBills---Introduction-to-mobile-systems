package com.example.thebills.results;

import android.util.Log;
import android.widget.TextView;

import com.example.thebills.bill.BillManager;
import com.example.thebills.user.UserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Class responsible for managing results
public class ResultsManager {

    private final BillManager billManager = new BillManager(); // Instance of BillManager to handle bills
    private final List<BillTuple> billList = new ArrayList<>(); // List to store bill tuples
    private final List<ResultTuple> resultList = new ArrayList<>(); // List to store result tuples
    private final Set<String> uniqueOwners = new HashSet<>(); // Set to store unique owners
    private UserManager userManager;
    private int billsReceivedCount = 0; // Counter for received bills

    private TextView resultsTextField; // TextView to display results

    // Constructor to initialize ResultsManager with a TextView to display results
    public ResultsManager(TextView resultsTextField) {
        this.resultsTextField = resultsTextField;
        this.userManager = new UserManager();
    }

    // Method to retrieve bills for a given room
    public void getBillsForRoom(String roomKey) {
        billManager.getRoomBills(roomKey, new BillManager.GetRoomBillsCallback() {
            @Override
            public void onBillsReceived(Map<String, String> billMap) {
                List<String> billKeys = new ArrayList<>(billMap.keySet());
                billsReceivedCount = 0;

                // Iterate through each bill key
                for (String billKey : billKeys) {
                    billManager.getBillData(billKey, new BillManager.GetBillDataCallback() {
                        @Override
                        public void onBillDataReceived(Map<String, Object> billData) {
                            Map<String, Double> costMap = (Map<String, Double>) billData.get("costMap");
                            String billOwner = (String) billData.get("billOwner");

                            uniqueOwners.add(billOwner);
                            billList.add(new BillTuple(billOwner, costMap));
                            billsReceivedCount++;

                            // Perform operation after all bills are received
                            if (billsReceivedCount == billKeys.size()) {
                                performOperationAfterBillsReceived();
                            }
                        }

                        @Override
                        public void onCancelled(String error) {
                            // Handle cancellation
                        }
                    });
                }
            }

            @Override
            public void onNullReceived(){

            }

            @Override
            public void onCancelled(String error) {
                // Handle cancellation
            }
        });
    }

    // Method to perform operations after all bills are received
    private void performOperationAfterBillsReceived() {
        getOwners();
        getBills();
        logic();
        resultsTextField.setText(getAllToString());
    }

    // Method to log unique owners
    private void getOwners() {
        for (String owner : uniqueOwners) {
            Log.d("TheBills: ResultManager", "owner: " + owner);
        }
    }

    // Method to log received bills
    public void getBills() {
        Log.d("TheBills: ResultManager", "billList size: " + billList.size());
        for (int i = 0; i < billList.size(); i++) {
            Log.d("TheBills: ResultManager", "bill: " + billList.get(i).toString());
        }
    }

    // Method to perform logic for calculating results
    public void logic() {
        for (String owner : uniqueOwners) {
            Map<String, Double> userTotalmap = new HashMap<>();

            for (int i = 0; i < billList.size(); i++) {
                if (billList.get(i).getOwner().equals(owner)) {
                    Map<String, Double> singleBillMap = billList.get(i).getCostmap();

                    for (String key : singleBillMap.keySet()) {
                        // Checking if key exists
                        if (userTotalmap.containsKey(key)) {
                            Double existingValue = userTotalmap.get(key);
                            Double billValue = singleBillMap.get(key);
                            userTotalmap.put(key, existingValue + billValue);
                        } else {
                            userTotalmap.put(key, singleBillMap.get(key));
                        }
                    }
                }
            }

            // Log user total map
            for (Map.Entry<String, Double> entry : userTotalmap.entrySet()) {
                Log.d("TheBills: ResultManager", "User: " + entry.getKey() + ", Total Cost: " + entry.getValue());
            }

            resultList.add(new ResultTuple(owner, userTotalmap));
        }
    }

    // Method to convert all results to a string
    public String getAllToString() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < resultList.size(); i++) {
            result.append(resultList.get(i).getOwner())
                    .append("\n\n")
                    .append(resultList.get(i).toString())
                    .append("\n ------------------------------ \n\n");

        }

        return result.toString();
    }
}
