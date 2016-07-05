package com.example.sharemodule;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class SendMessageTask extends AsyncTask<String, Void, Void> {
    private Context mCtx;

    public SendMessageTask(Context ctx) {
        mCtx = ctx;
    }


    @Override
    protected Void doInBackground(String... params) {

        String path = params[0];
        String text = "";
        if (params.length > 0) {
            text = params[1];
        }

        Log.d("path", path);
        //String node = MessageApiUtils.getNearByConnectedNode(mGoogleApiClient);
        MessageApiUtils.sendMessageApi(mCtx, path, text);
        return null;
    }
}
