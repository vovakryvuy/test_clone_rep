package com.example.volodymyr.sunrisesunset;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by Volodymyr on 1/23/2018.
 */

public class GPS implements LocationListener {
    private LocationManager mLocationManager;
    private Context mContext;
    private Location mLocation;
    private double latitude;
    private double longitude;

    public GPS(Context context) {
        this.mContext = context;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (mLocationManager != null) {
                if (ContextCompat.checkSelfPermission(mContext,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mContext,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, this);
                    mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    latitude = mLocation.getLatitude();
                    longitude = mLocation.getLongitude();
                }
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("LOG", "onStatusChanged: ");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("LOG", "onProviderEnabled: ");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("LOG", "onProviderDisabled: ");
    }


}
