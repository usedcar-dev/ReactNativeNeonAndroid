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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
    private Location location;
    private boolean locationInProgress = false;

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
        if(this.location!=null && tracker!=null){
            tracker.onLocationChanged(this.location);
        }else{
            startLocationUpdates();
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
        this.location = locationResult.getLastLocation();
        LocationHolder.getInstance().setLocation(location);
        locationInProgress = false;
        if(tracker != null && this.location!=null)
        {
            tracker.onLocationChanged(this.location);
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

    public void setLocationInProgress(boolean locationInProgress) {
        this.locationInProgress = locationInProgress;
    }

    private void startLocationUpdates()
    {
        if(null != activity)
        {
            if(locationInProgress){
                return;
            }
            locationInProgress = true;

            if(!isGPSEnabled())
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
                    return;
                }
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, LocationHelper.this, Looper.myLooper());
                //getLastLocation();
            }
        }
    }

    public boolean isGPSEnabled(){
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

        return gps_enabled;
    }


    public void requestPermissions() {

        ActivityCompat.requestPermissions(activity.get(),
                                          new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                          REQUEST_PERMISSIONS_REQUEST_CODE);
    }


    public void setLocationListener(LocationListener tracker) {
        this.tracker = tracker;
    }
}
