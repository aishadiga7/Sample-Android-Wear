package com.example.aishwarya.samplewear.ui;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.aishwarya.samplewear.DtaMapSendingTask;
import com.example.aishwarya.samplewear.R;
import com.example.aishwarya.samplewear.model.NearByAtm;
import com.example.aishwarya.samplewear.model.NearByAtmResult;
import com.example.sharemodule.AppConstants;
import com.example.sharemodule.Constants;
import com.example.sharemodule.LocationTracker;
import com.example.sharemodule.Notification;
import com.example.sharemodule.NotificationManager;
import com.example.sharemodule.SendMessageTask;
import com.example.sharemodule.Utils.DialogUtility;
import com.example.sharemodule.Utils.DownloadUtility;
import com.example.sharemodule.Utils.NetworkCheckUtility;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;


public class LoginActivity extends Activity implements View.OnClickListener,  NotificationManager
                                                                                        .Observer {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_LOCATION = 100;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btn_call_Watch).setOnClickListener(this);
        findViewById(R.id.btn_send_data).setOnClickListener(this);
        findViewById(R.id.load_image).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocationTracker.getLocationTracker(this).init();
        NotificationManager.getInstance().addObserver(Notification.NOTIFY_LOCATION_CHANGED,
                                                                        LoginActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationManager.getInstance().removeObserver(Notification.NOTIFY_LOCATION_CHANGED, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call_Watch:
                new SendMessageTask(this).execute(Constants.Keys.CALL_LOGIN, "logindata");
                break;
            case R.id.btn_send_data:
                makeApiCallToGetNearByAtms();
                break;
            case R.id.load_image:
                
                break;
               /* List<Account> list = new ArrayList<>();

                for (int i = 0; i < 10; i++) {
                    Account item = new Account();
                    item.setId( String.valueOf(i));
                    item.setName("Item" + i);
                    Log.d(TAG, "Id:" +item.getId());
                    Log.d(TAG, "Name:" +item.getName());
                    list.add(item);
                }

                new DtaMapSendingTask(this, list).execute(Constants.Keys.DATA_ITEM, "");*/
             /* Log.d("btn_send_data",list.size()+" list size");
                break;*/
        }
    }

    private void makeApiCallToGetNearByAtms() {
        DialogUtility.showProgressDialog(this, "");
        if (NetworkCheckUtility.isNetworkAvailable(this)) {
            new DownloadRawData().execute(constructUrl());
        } else {
            DialogUtility.showToast(this, getString(R.string.please_check_internet_connection));
        }

    }

    private String constructUrl() {
       /* String url =  AppConstants.TEXT_SEARCH_API + "location=" + mCurrentLocation.getLatitude() +
                "," +
                "" + mCurrentLocation.getLongitude() + "&radius=500" + "&key=" + AppConstants
                .SERVER_KEY + "&type=" + "atm";

        Log.d(TAG, "Url:" +url);*/

        String url =  AppConstants.TEXT_SEARCH_API + "location=13.332222,74.74611"+
                "&radius=500" + "&key=" + AppConstants.SERVER_KEY + "&type=" + "atm";
        Log.d(TAG, "Location Url:" +url);
        return url;
    }

    @Override
    public void update(Notification notificationName, Bundle data) {
        if (notificationName == Notification.NOTIFY_LOCATION_CHANGED) {
            if (data != null) {
                mCurrentLocation   = data.getParcelable(Constants
                        .EXTRA_LOCATION_DATA);


            } else {
                DialogUtility.showToast(this, getString(R.string.location_cannot_be_fetched));

            }
        }
    }


    private class DownloadRawData extends AsyncTask<String, Void, ArrayList<NearByAtm>> {
        @Override
        protected ArrayList<NearByAtm> doInBackground(String... params) {
            try {
                String url = DownloadUtility.downloadUrl(params[0]);
                Gson gson = new Gson();
                NearByAtmResult placeResults = gson.fromJson(url, NearByAtmResult.class);
                Log.d(TAG, "placeResults:" + placeResults);
                ArrayList<NearByAtm> results = placeResults.getResults();
                if (placeResults.getStatus().equalsIgnoreCase("Ok") && placeResults
                        .getResults().size() > 0) {
                    Log.d(TAG, "Place greater than 0");
                    return results;

                } else {
                    return null;
                }

            } catch (IOException e) {
                return null;
            }catch (JsonSyntaxException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<NearByAtm> nearByAtm) {
            Log.d(TAG, "Inside onPostExecute:");
            if (nearByAtm != null) {
                handleLocationFindSuccess(nearByAtm);
            } else {
                handleLocationFindFailure();
            }
        }
    }

    private void handleLocationFindSuccess(ArrayList<NearByAtm> nearByAtm ) {
        DialogUtility.dismissProgressDialog();
        Log.d(TAG, "onLocationFindSuccess()");
        if (nearByAtm != null) {
            ArrayList<String> nearByAtmList = new ArrayList<>();
            for (NearByAtm result : nearByAtm) {
                nearByAtmList.add(result.getName());
            }
            new DtaMapSendingTask(this, nearByAtmList).execute(Constants.Keys.DATA_ITEM, "");
        } else {
            DialogUtility.dismissProgressDialog();
            /*DialogUtils.showSnackBar(getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.no_places_found));*/
        }
    }

    private void handleLocationFindFailure() {
        DialogUtility.dismissProgressDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                DialogUtility.showToast(this, getString(R.string.permission_granted));
            } else {
                DialogUtility.showToast(this, getString(R.string.permission_not_granted));
            }
        }
    }
}
