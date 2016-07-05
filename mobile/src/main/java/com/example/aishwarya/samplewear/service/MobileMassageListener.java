package com.example.aishwarya.samplewear.service;

import android.util.Log;

import com.example.sharemodule.Constants;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;


public class MobileMassageListener extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        //Constants
        Log.d("MobileMassageListener","called"+messageEvent.getPath());

        switch (messageEvent.getPath())
        {
            case Constants.Keys.CALL_LOGIN:
                UtilityService.triggerLoginClick(this);
                break;
        }
    }
}
