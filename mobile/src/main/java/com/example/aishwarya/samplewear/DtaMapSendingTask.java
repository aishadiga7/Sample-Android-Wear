package com.example.aishwarya.samplewear;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;


public class DtaMapSendingTask extends AsyncTask<String, Void, Void> {
    private List<String> mNearByAtms;
    private Context mCtx;

    public DtaMapSendingTask(Context ctx, List<String> list) {

        mNearByAtms = list;
        mCtx = ctx;
    }


    @Override
    protected Void doInBackground(String... params) {
        String path = params[0];
        DataItemSender.sendAccountListData(mCtx, mNearByAtms);
        Log.d("DtaMapSendingTask","called");
        return null;
    }

}
