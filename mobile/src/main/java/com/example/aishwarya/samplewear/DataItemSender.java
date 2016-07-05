package com.example.aishwarya.samplewear;

import android.content.Context;
import android.util.Log;

import com.example.sharemodule.Constants;
import com.example.sharemodule.MessageApiUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;


public class DataItemSender {

    public static void sendAccountListData(Context ctx, List<String> accountList) {

        GoogleApiClient googleApiClient = MessageApiUtils.initGoogleApiClient(ctx);
        ConnectionResult connectionResult = MessageApiUtils.initConnectionResult(googleApiClient);
        if (connectionResult.isSuccess() && googleApiClient.isConnected()) {
            ArrayList<DataMap> dataMapDepositAccountList = new ArrayList<>();

            for (String accountItem : accountList) {
                DataMap dataMap = new DataMap();
                //dataMap.putString("id", accountItem.getId());
                dataMap.putString("name", accountItem);
                dataMapDepositAccountList.add(dataMap);
            }

            Log.d("data map size is",dataMapDepositAccountList.size()+"");

            PutDataMapRequest putDataMap = PutDataMapRequest.create(Constants.Keys.DATA_ITEM);
            putDataMap.getDataMap().putDataMapArrayList(Constants.Keys.ACCOUNT_LIST_ITEM, dataMapDepositAccountList);
            putDataMap.getDataMap().putDouble("timeStamp", System.currentTimeMillis());
            PutDataRequest request = putDataMap.asPutDataRequest();
            DataApi.DataItemResult result = Wearable.DataApi.putDataItem(googleApiClient, request).await();

            Log.d("DataItemSender","called");
            if (!result.getStatus().isSuccess()) {
                Log.d("data_sender", "Failed to send data");
            }
            else
            {
                Log.d("data_sender", "sucess");
            }

        } else {
            GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
            googleAPI.showErrorNotification(ctx, connectionResult.getErrorCode());

        }

    }

}
