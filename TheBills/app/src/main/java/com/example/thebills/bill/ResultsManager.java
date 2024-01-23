package com.example.thebills.bill;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultsManager {

    private BillManager billManager = new BillManager();
    private List<BillTuple> billList = new ArrayList<>();
    private int billsReceivedCount = 0; // Licznik odebranych rachunków

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
                            Map<String, Float> costMap = (Map<String, Float>) billData.get("costMap");
                            String billOwner = (String) billData.get("billOwner");

                            Log.d("ResultsManager - costMap", costMap.toString());
                            Log.d("ResultsManager - billOwner", billOwner);
                            Log.d("ResultsManager", "bill received");

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

        getBills();
    }

    public void getBills() {
        Log.d("ResultsManager", "size: " + billList.size());
        for (int i = 0; i < billList.size(); i++) {
            Log.d("ResultsManager", "bill: " + billList.get(i).toString());
        }
    }
}
