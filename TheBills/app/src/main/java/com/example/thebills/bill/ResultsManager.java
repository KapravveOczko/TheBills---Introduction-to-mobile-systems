package com.example.thebills.bill;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResultsManager {

    private BillManager billManager = new BillManager();
    private List<BillTuple> billList = new ArrayList<>();
    private List<ResultTuple> resultList = new ArrayList<>();
    private Set<String> uniqueOwners = new HashSet<>();
    private int billsReceivedCount = 0; // bill counter

    public void getBillsForRoom(String roomKey) {
        billManager.getRoomBills(roomKey, new BillManager.GetRoomBillsCallback() {
            @Override
            public void onBillsReceived(Map<String, String> billMap) {
                List<String> billKeys = new ArrayList<>(billMap.keySet());

                billsReceivedCount = 0;

                // Iterate through each bill key
                for (String billKey : billKeys) {
                    Log.d("ResultsManager", "bill keys: " + billKey);
                    billManager.getBillData(billKey, new BillManager.GetBillDataCallback() {
                        @Override
                        public void onBillDataReceived(Map<String, Object> billData) {
                            Map<String, Long> costMap = (Map<String, Long>) billData.get("costMap");
                            String billOwner = (String) billData.get("billOwner");

                            Log.d("ResultsManager - costMap", costMap.toString());
                            Log.d("ResultsManager - billOwner", billOwner);
                            Log.d("ResultsManager", "bill received");

                            uniqueOwners.add(billOwner);
                            billList.add(new BillTuple(billOwner, costMap));
                            billsReceivedCount++;

                            if (billsReceivedCount == billKeys.size()) {
                                performOperationAfterBillsReceived();
                            }
                        }

                        @Override
                        public void onCancelled(String error) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(String error) {
            }
        });
    }

    private void performOperationAfterBillsReceived() {
        Log.d("ResultsManager", "All bills received. Performing the operation...");

        getOwners();
        getBills();
        logic();
    }

    private void getOwners() {
        for (String owner : uniqueOwners) {
            Log.d("DEBUG  - Owners", owner);
        }
    }

    public void getBills() {
        Log.d("DEBUG      ResultsManager", "size: " + billList.size());
        for (int i = 0; i < billList.size(); i++) {
            Log.d("DEBUG      ResultsManager", "bill: " + billList.get(i).toString());
        }
    }

    public void logic(){

        for (String owner : uniqueOwners) {
            Map<String, Long> userTotalmap = new HashMap<>();

            for (int i = 0; i < billList.size(); i++) {
                if(billList.get(i).getOwner().equals(owner)){
                    Log.d("LOGIC  DEBUG", "owner of the bill: " + owner);

                    Map<String, Long> singleBillMap = billList.get(i).getCostmap();

                    for (String key :  singleBillMap.keySet()) {
                        // checking if key exists
                        if (userTotalmap.containsKey(key)) {
                            Long existingValue = userTotalmap.get(key);
                            Long billValue = singleBillMap.get(key);
                            userTotalmap.put(key, existingValue + billValue);
                        } else {
                            userTotalmap.put(key, singleBillMap.get(key));
                        }
                    }
                }
            }

            // user totall map logs
            for (Map.Entry<String, Long> entry : userTotalmap.entrySet()) {
                Log.d("LOGIC  DEBUG", "User: " + entry.getKey() + ", Total Cost: " + entry.getValue());
            }

            resultList.add(new ResultTuple(owner,userTotalmap));

        }

    }

}
