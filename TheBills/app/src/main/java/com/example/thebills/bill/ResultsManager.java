package com.example.thebills.bill;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultsManager {

    private BillManager billManager = new BillManager();

    public void getBillsForRoom(String roomKey) {
        billManager.getRoomBills(roomKey, new BillManager.GetRoomBillsCallback() {
            @Override
            public void onBillsReceived(Map<String, String> billMap) {
                List<String> billKeys = new ArrayList<>(billMap.keySet());

                // Iterate through each bill key
                for (String billKey : billKeys) {
                    Log.d("ResultsManager","bill keys: " + billKey);
                    billManager.getBillData(billKey, new BillManager.GetBillDataCallback() {
                        @Override
                        public void onBillDataReceived(Map<String, Object> billData) {
                            Map<String, Float> costMap = (Map<String, Float>) billData.get("costMap");
                            String billOwner = (String) billData.get("billOwner");

                            Log.d("ResultsManager - costMap", costMap.toString());
                            Log.d("ResultsManager - billOwner", billOwner);
//  tutaj trzeba jebnąć obsługę
                            Log.d("ResultsManager","bill recived");
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


}
