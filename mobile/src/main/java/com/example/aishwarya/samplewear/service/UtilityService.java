package com.example.aishwarya.samplewear.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.example.aishwarya.samplewear.ui.LoginActivity;


public class UtilityService extends IntentService {
    private static final String TAG = UtilityService.class.getSimpleName();
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UtilityService(String name) {
        super(name);
    }
    public UtilityService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent loginIntent =new Intent(this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);
    }

    public static void triggerLoginClick(Context ctx) {

       // intent.setAction(Constants.DataApiConstant.ACCOUNT);
        Intent intent = new Intent(ctx, UtilityService.class);
        ctx.startService(intent);
    }

}
