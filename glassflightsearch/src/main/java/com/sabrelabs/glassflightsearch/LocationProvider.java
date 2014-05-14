package com.sabrelabs.glassflightsearch;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class LocationProvider implements LocationListener {
    private final String TAG = LocationProvider.class.getSimpleName();

    private static LocationProvider INSTANCE = null;
    private LocationManager locationManager = null;
    private Location location = null;

    private LocationProvider() {
        Context context = ApplicationContextProvider.getContext();

        Log.d(TAG, "location manager....");
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        List<String> providers = locationManager.getProviders(criteria, true);
        for (String provider : providers) {
            Log.d(TAG, provider);
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                this.onLocationChanged(location);
                // one location is probably sufficient
                break;
            } else {
                // probably don't need regular updates, but if we miss a last known location we might
                locationManager.requestLocationUpdates(provider, 35000, 10, this);
            }
        }
    }

    public static LocationProvider getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new LocationProvider();
        }
        return INSTANCE;
    }

    public Location getLocation() {
        if( location == null) {
            throw new RuntimeException("This location provider is unable to provide a location");
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        Log.d(TAG, "LOCATION! " +
                String.valueOf(location.getLatitude()) + " " +
                String.valueOf(location.getLongitude()) + " " +
                String.valueOf(location.getAccuracy())
        );
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
