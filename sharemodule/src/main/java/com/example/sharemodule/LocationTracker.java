package com.example.sharemodule;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by aishwarya on 23/6/16.
 */
public class LocationTracker implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener , LocationListener{

    private static final String TAG = LocationTracker.class.getSimpleName();
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long LOCATION_FETCH_TIMEOUT = 60 * 1000;
    private static final int REQUEST_LOCATION = 100;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static LocationTracker mLocationTracker;
    private Context mContext;
    private Handler mLocationFetchTimeOutHandler = new Handler();

    private LocationTracker(Context context) {
        mContext = context;
        buildGoogleApiClient();
        createLocationRequest();
    }

    public static synchronized LocationTracker getLocationTracker(Context appContext) {
        if (mLocationTracker == null) {
            mLocationTracker = new LocationTracker(appContext);
        }
        return mLocationTracker;
    }

    public void init() {
        mGoogleApiClient.connect();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission
                    .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                    .checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission not granted:");
                handlePermissionNotGranted();
            } else {
                Log.d(TAG, "Permission  granted:");
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                Log.d(TAG, "init() : location: " + location);
                if (location == null) {
                    startLocationUpdates();
                } else {
                    notifyLocationFound(location);
                }
            }
        }
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected() ||
                !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    private void notifyLocationFound(Location location) {
        Log.d(TAG, "notifyLocationFound: location: " + location);
        if (location != null) {
            Log.d(TAG, "Latitude: " + location.getLatitude());
            Log.d(TAG, "Longitude: " + location.getLongitude());
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.EXTRA_LOCATION_DATA, location);
            NotificationManager.getInstance().notifyObservers(Notification.NOTIFY_LOCATION_CHANGED, bundle);
        }
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected synchronized void buildGoogleApiClient() {
        Log.d(TAG, "Inside buildGoogleApiClient:");
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates()");
        startLocationFetchExpiryTimer();
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            handlePermissionNotGranted();
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest,  this);
        }
    }

    public void stopLocationUpdates() {
        Log.d(TAG, "stopLocationUpdates()");
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void handlePermissionNotGranted() {
        Log.d(TAG, "Requesting for permission:");
        ActivityCompat.requestPermissions((Activity) mContext,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION);
    }

    private void startLocationFetchExpiryTimer() {
        mLocationFetchTimeOutHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopLocationUpdates();
                Log.d(TAG, "Failed to get location");
                NotificationManager.getInstance().notifyObservers(Notification.NOTIFY_LOCATION_CHANGED, null);
            }
        }, LOCATION_FETCH_TIMEOUT);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "Connected:");
        if (mGoogleApiClient != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                handlePermissionNotGranted();
            } else {
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                Log.d(TAG, "onConnected(): location: " + location);
                if (location == null) {
                    startLocationUpdates();
                } else {
                    notifyLocationFound(location);
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "onConnectionSuspended()");
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged() : location: " + location);
        mLocationFetchTimeOutHandler.removeCallbacksAndMessages(null);
        stopLocationUpdates();
        notifyLocationFound(location);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed()");
    }
}
