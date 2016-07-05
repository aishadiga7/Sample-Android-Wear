package com.example.sharemodule.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by aishwarya on 22/6/16.
 */
public class DialogUtility {
    private static ProgressDialog mProgressDialog = null;

    public static void showProgressDialog(Context context, String message){
        if(mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }


    public static void dismissProgressDialog() {
        if((mProgressDialog != null) && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }



    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
