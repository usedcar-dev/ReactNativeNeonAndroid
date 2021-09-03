package com.gaadi.neon.util;

import android.location.Location;

public class LocationHolder {
    private static LocationHolder instance;
    private Location location;


    private LocationHolder(){}


    public static LocationHolder getInstance(){
        if(instance == null){
            synchronized (LocationHolder.class){
                instance = new LocationHolder();
            }
        }
        return instance;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
