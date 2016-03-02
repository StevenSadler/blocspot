package com.stevensadler.android.blocspot.geofence;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * Created by Steven on 2/28/2016.
 */
public class GeofenceStore implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static String TAG = GeofenceStore.class.getSimpleName();

    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private PendingIntent mPendingIntent;
    private ArrayList<Geofence> mGeofences;
    private GeofencingRequest mGeofencingRequest;
    private LocationRequest mLocationRequest;
    private ArrayList<String> mStaleGeofenceIds;

    public GeofenceStore(Context context, ArrayList<Geofence> geofences, ArrayList<String> staleGeofenceIds) {
        mContext = context;
        mGeofences = new ArrayList<Geofence>(geofences);
        mStaleGeofenceIds = new ArrayList<String>(staleGeofenceIds);
        mPendingIntent = null;

        // Build a new GoogleApiClient, specify that we want to use LocationServices
        // by adding the API to the client, specify the connection callbacks are in
        // this class as well as the OnConnectionFailed method.
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        // This is purely optional and has nothing to do with geofencing.
        // I added this as a way of debugging.
        // Define the LocationRequest.
        mLocationRequest = new LocationRequest();
        // We want a location update every 10 seconds.
        mLocationRequest.setInterval(10000);
        // We want the location to be as accurate as possible.
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (!mGeofences.isEmpty()) {
            mGoogleApiClient.connect();
        }
        Toast.makeText(context, TAG + " constructed", Toast.LENGTH_LONG).show();
    }

    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");
        // We're connected, now we need to create a GeofencingRequest with
        // the geofences we have stored.
        mGeofencingRequest = new GeofencingRequest.Builder().addGeofences(mGeofences).build();

        if (mPendingIntent == null) {
            Log.v(TAG, "Creating PendingIntent");
            Intent intent = new Intent(mContext, GeofenceIntentService.class);
            mPendingIntent = PendingIntent.getService(mContext, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

        // This is for debugging only and does not affect
        // geofencing.
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        // submit the request to remove stale geofences
        PendingResult<Status> pendingRemoveResult = LocationServices.GeofencingApi
                .removeGeofences(mGoogleApiClient, mStaleGeofenceIds);

        pendingRemoveResult.setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status result) {
                Log.v(TAG, "Remove Geofences result");
                logStatus(result);

                // Submitting the request to monitor geofences.
                try {
                    PendingResult<Status> pendingResult = LocationServices.GeofencingApi
                            .addGeofences(mGoogleApiClient, mGeofencingRequest,
                                    mPendingIntent);
                    // Set the result callbacks listener to this class.
                    pendingResult.setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status result) {
                            Log.v(TAG, "Add Geofences result");
                            logStatus(result);
                        }
                    });
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void logStatus(Status result) {
        if (result.isSuccess()) {
            Log.v(TAG, "Success!");
        } else if (result.hasResolution()) {
            // TODO Handle resolution
        } else if (result.isCanceled()) {
            Log.v(TAG, "Canceled");
        } else if (result.isInterrupted()) {
            Log.v(TAG, "Interrupted");
        } else {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG, "Connection suspended.");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, "Location Information\n"
                + "==========\n"
                + "Provider:\t" + location.getProvider() + "\n"
                + "Lat & Long:\t" + location.getLatitude() + ", "
                + location.getLongitude() + "\n"
                + "Altitude:\t" + location.getAltitude() + "\n"
                + "Bearing:\t" + location.getBearing() + "\n"
                + "Speed:\t\t" + location.getSpeed() + "\n"
                + "Accuracy:\t" + location.getAccuracy() + "\n");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v(TAG, "Connection failed.");
    }

}
