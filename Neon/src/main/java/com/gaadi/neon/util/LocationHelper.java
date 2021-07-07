package com.gaadi.neon.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.lang.ref.WeakReference;

/**
 * Created by salman on 2/1/18.
 */

public class LocationHelper extends LocationCallback
{

    public static final int REQUEST_CHECK_SETTINGS = 505;
    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 606;
    private static final String TAG = "GPSLocationTracker";
    private static final long INTERVAL = 10000;
    private static final long FASTEST_INTERVAL = 1000;
    private LocationRequest mLocationRequest;
    private WeakReference<AppCompatActivity> activity;
    private LocationSettingsRequest mLocationSettingsRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationListener tracker;

    public LocationHelper(AppCompatActivity activity)
    {
        this.activity = new WeakReference<>(activity);

        //setup LocationRequest
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(INTERVAL);

        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mSettingsClient = LocationServices.getSettingsClient(activity);
    }

    public void getLocation()
    {

        if(checkPermissions())
        {
            startLocationUpdates();
        }
        else
        {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation()
    {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>()
        {
            @Override
            public void onSuccess(Location location)
            {
                // GPS location can be null if GPS is switched off
                if(location != null)
                {
                    tracker.onLocationChanged(location);
                }
                else
                {
                    LocationHelper.this.getLocation();
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(
                    @NonNull
                            Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onLocationResult(LocationResult locationResult)
    {
        super.onLocationResult(locationResult);
        if(tracker != null)
        {
            tracker.onLocationChanged(locationResult.getLastLocation());
        }
        stopLocationUpdates();
    }

    public boolean checkPermissions()
    {

        int permissionState = ActivityCompat.checkSelfPermission(activity.get(), Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public void stopLocationUpdates()
    {

        if(null != activity)
        {
            mFusedLocationClient.removeLocationUpdates(this);
        }
    }

    private void startLocationUpdates()
    {
        if(null != activity)
        {
            LocationManager lm = (LocationManager) activity.get().getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            //boolean network_enabled = false;

            try
            {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
            catch(Exception ex)
            {
            }

            //        try
            //        {
            //            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            //        }
            //        catch(Exception ex)
            //        {
            //        }

            if(!gps_enabled)
            {
                activity.get().startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 505);
            }
            else
            {
                if(ActivityCompat.checkSelfPermission(activity.get(),
                                                      Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                        .checkSelfPermission(activity.get(),
                                             Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, LocationHelper.this, Looper.myLooper());
                //getLastLocation();
            }
            //        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
            //                .addOnSuccessListener(activity.get(), new OnSuccessListener<LocationSettingsResponse>() {
            //                    @Override
            //                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
            //                        Log.i(TAG, "All location settings are satisfied.");
            //
            //                        //noinspection MissingPermission
            ////                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
            ////                                LocationHelper.this, Looper.myLooper());
            //                        getLastLocation();
            //
            //
            //                    }
            //                })
            //                .addOnFailureListener(activity.get(), new OnFailureListener() {
            //                    @Override
            //                    public void onFailure(@NonNull Exception e) {
            //                        int statusCode = ((ApiException) e).getStatusCode();
            //                        switch (statusCode) {
            //                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
            //                                Log.i(TAG, "Location settings are not satisfied.");
            //                                try {
            //                                    // Show the dialog by calling startResolutionForResult(), and check the
            //                                    // result in onActivityResult().
            //                                    ResolvableApiException rae = (ResolvableApiException) e;
            //                                    rae.startResolutionForResult(activity.get(), REQUEST_CHECK_SETTINGS);
            //
            //                                } catch (IntentSender.SendIntentException sie) {
            //                                    Log.i(TAG, "PendingIntent unable to execute request.");
            //                                }
            //                                break;
            //                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
            //                                String errorMessage = "Location settings are inadequate, and cannot be " +
            //                                        "fixed here. Fix in Settings.";
            //                                Log.e(TAG, errorMessage);
            //                                break;
            //                            default:
            //                                Log.e(TAG, "Location Update Failed.");
            //
            //                        }
            //
            //                    }
            //                });
        }
    }


    private void requestPermissions() {

        ActivityCompat.requestPermissions(activity.get(),
                                          new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                          REQUEST_PERMISSIONS_REQUEST_CODE);
    }


    public void setLocationListener(LocationListener tracker) {
        this.tracker = tracker;
    }
}
