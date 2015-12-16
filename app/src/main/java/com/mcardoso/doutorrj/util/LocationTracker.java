package com.mcardoso.doutorrj.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by mcardoso on 12/13/15.
 */
public class LocationTracker extends Service implements LocationListener {

    private static String TAG = "LocationTracker";
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private static LocationTracker ourInstance = new LocationTracker();

    public static LocationTracker getInstance() {
        return ourInstance;
    }

    private Context context;
    private Location currentLocation; // Location
    private LocationManager locationManager;

    private LocationTracker() {
    }

    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    public void setup(Context ctx) {
        if ( this.context == null ) {
            this.context = ctx;

            this.locationManager = (LocationManager) this.context.getSystemService(LOCATION_SERVICE);

            if (this.canGetLocation()) {
                try {
                    if (this.isNetworkEnabled()) {
                        registerLocationProvider(LocationManager.NETWORK_PROVIDER);
                    }
                    if (this.isGPSEnabled()) {
                        registerLocationProvider(LocationManager.GPS_PROVIDER);
                    }

                } catch (SecurityException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            } else {
                this.showSettingsAlert();
            }
        }
    }

    public void stopUsingGPS(){
        if(this.locationManager != null) {
            try {
                this.locationManager.removeUpdates(LocationTracker.this);
            } catch (SecurityException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    private void registerLocationProvider(String provider) throws SecurityException {
        this.locationManager.requestLocationUpdates(
                provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        this.currentLocation = this.locationManager.getLastKnownLocation(provider);
    }

    public boolean canGetLocation() {
        return isGPSEnabled() || isNetworkEnabled();
    }

    private boolean isGPSEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean isNetworkEnabled() {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showSettingsAlert(){
        Intent gpsOptionsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        this.context.startActivity(gpsOptionsIntent);
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//
//        // Setting Dialog Title
//        alertDialog.setTitle("GPS is settings");
//
//        // Setting Dialog Message
//        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
//
//        // On pressing the Settings button.
//        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog,int which) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                context.startActivity(intent);
//            }
//        });
//
//        // On pressing the cancel button
//        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        // Showing Alert Message
//        alertDialog.show();
    }

    public LatLng getCurrentLatLng() {
        Location currentLocation = this.getCurrentLocation();
        return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
    }


    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation = location;
    }


    @Override
    public void onProviderDisabled(String provider) {
    }


    @Override
    public void onProviderEnabled(String provider) {
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static LatLng getMidPoint(LatLng position1, LatLng position2){

        double lat1 = position1.latitude;
        double lon1 = position1.longitude;
        double lat2 = position2.latitude;
        double lon2 = position2.longitude;

        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        return new LatLng(Math.toDegrees(lat3), Math.toDegrees(lon3));
    }

}
