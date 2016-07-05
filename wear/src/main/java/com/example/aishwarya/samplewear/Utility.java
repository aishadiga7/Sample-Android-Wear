package com.example.aishwarya.samplewear;

import android.util.Log;


public class Utility {

    public static final boolean IS_DEBUG = BuildConfig.DEBUG;

    /**
     * To show the logs using tag and msg
     *
     * @param tag
     * @param msg
     */
    public static void Log(String tag, String msg) {
        if (IS_DEBUG)
            Log.d(tag, msg);
    }

    public interface Keys
    {
        String CALL_LOGIN ="/call_login";
    }
}
