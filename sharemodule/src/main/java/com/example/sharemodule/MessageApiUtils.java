package com.example.sharemodule;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.TimeUnit;


public class MessageApiUtils {

    public static String getNearByConnectedNode(GoogleApiClient googleApiClient) {

        String nearByConnectedNode = "";
        NodeApi.GetConnectedNodesResult nodes;
        nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
        for (com.google.android.gms.wearable.Node result : nodes.getNodes()) {
            if (result.isNearby()) {
                nearByConnectedNode = result.getId();
                break;
            }
        }
        return nearByConnectedNode;
    }

    public static GoogleApiClient initGoogleApiClient(Context ctx) {

        return new GoogleApiClient.Builder(ctx)
                .addApi(Wearable.API)
                .build();
    }

    public static ConnectionResult initConnectionResult(GoogleApiClient googleApiClient) {
        ConnectionResult connectionResult = googleApiClient.blockingConnect(
                Constants.GoogleApiClientConstant.GOOGLE_API_CLIENT_TIMEOUT_S, TimeUnit.SECONDS);
        return connectionResult;
    }

    public static void sendMessageApi(Context ctx, final String path, final String msg) {


        final GoogleApiClient googleApiClient = initGoogleApiClient(ctx);
        ConnectionResult connectionResult = initConnectionResult(googleApiClient);

        if (connectionResult.isSuccess() && googleApiClient.isConnected()) {
            String node = MessageApiUtils.getNearByConnectedNode(googleApiClient);
            MessageApiUtils.callMessageApi(googleApiClient, node, path, msg);
            googleApiClient.disconnect();
        } else {
            GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
            googleAPI.showErrorNotification(ctx, connectionResult.getErrorCode());
        }

    }


    public static void callMessageApi(GoogleApiClient googleApiClient, String node, String path, String text) {

        Wearable.MessageApi.sendMessage(googleApiClient, node, path, text.getBytes())
                .setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(@NonNull MessageApi.SendMessageResult sendMessageResult) {
                        if(sendMessageResult.getStatus().isSuccess())
                            Log.d("message sucess",sendMessageResult.getStatus().getStatusMessage());
                        Log.d("onResult:", "" + sendMessageResult);
                    }
                });
    }

}

