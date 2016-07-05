package com.example.sharemodule.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by aishwarya on 22/6/16.
 */
public class DownloadUtility {

    private static final String TAG = DownloadUtility.class.getSimpleName();
    public static final int READ_TIMEOUT = 10000;
    public static final int CONNECT_TIMEOUT = 15000;


    public static String downloadUrl(String myurl) throws IOException {
        InputStream inputStream = null;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            Log.d(TAG, "Response message:" +conn.getResponseMessage());
            inputStream = conn.getInputStream();
            String contentAsString = readResponseData(inputStream);
            return contentAsString;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /*
       Method to read  api response data from input stream
    */
    public static String readResponseData(InputStream inputStream)  {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder data = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(data);
    }

}
