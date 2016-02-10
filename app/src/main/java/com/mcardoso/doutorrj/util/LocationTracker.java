package com.mcardoso.doutorrj.util;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.mcardoso.doutorrj.MainActivity;
import com.mcardoso.doutorrj.R;

/**
 * Created by mcardoso on 12/13/15.
 */
public class LocationTracker extends Service {

    private static final int MAX_SCHEDULE_RETRIES = 0;
    private static final int SCHEDULE_DELAY_IN_SECONDS = 2;
    private static final int BROADCAST_LATLNG_DELAY_IN_SECONDS = 1;

    private static String TAG = "LocationTracker";
    private static LocationTracker ourInstance = new LocationTracker();

    private Location lastKnowLocation = null;
    private LocationManager locationManager = null;
    private AlertDialog dialog = null;
    private boolean isFirstTime = true;
    private int retries = 0;
    private Resources res = null;

    public Context ctx;

    private LocationTracker() {
    }

    public static LocationTracker getInstance() {
        return ourInstance;
    }

    public void setContext(Context ctx) {
        this.ctx = ctx;
        this.res = this.ctx.getResources();
        this.locationManager = (LocationManager) this.ctx.getSystemService(LOCATION_SERVICE);
    }

    public void setup() throws SecurityException {

        boolean hasPermissions = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            MainActivity activity = (MainActivity) this.ctx;
            if( !activity.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ) {
                hasPermissions = false;
                activity.askPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }

        if( hasPermissions ) {
            if (this.locationManager != null) {

                boolean isNetworkEnabled = this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                boolean isGPSEnabled = this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (isNetworkEnabled || isGPSEnabled) {
                    if (isNetworkEnabled) {
                        Log.d(TAG, "Network enabled");
                        Location location = this.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            this.lastKnowLocation = location;
                        }
                    }

                    if (isGPSEnabled) {
                        Log.d(TAG, "GPS enabled");
                        Location location = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            this.lastKnowLocation = location;
                        }
                    }

                    if (this.lastKnowLocation == null) {
                        Log.d(TAG, "Not ready. Need to schedule.");
                        if (this.retries < MAX_SCHEDULE_RETRIES) {
                            this.retries += 1;
                            this.schedule();
                        } else {
                            this.showLocationNotDefinedDialog();
                        }
                    } else {
                        this.broadcastData();
                    }
                } else {
                    Log.d(TAG, "Show alert");
                    this.showGPSOffDialog();
                }
            } else {
                this.schedule();
            }
        }
    }

    private void broadcastData() {

        Log.d(TAG, "READY! " + this.lastKnowLocation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((MainActivity) ctx).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NotifiableData.getInstance().broadcastLocation(lastKnowLocation);
                    }
                });
            }
        }, 1000 * BROADCAST_LATLNG_DELAY_IN_SECONDS);
    }

    private void showGPSOffDialog() {
        this.dialog = new AlertDialog.Builder(this.ctx)
                .setTitle(this.res.getString(R.string.gps_off_dialog_title))
                .setCancelable(false)
                .setMessage(this.res.getString(R.string.gps_off_dialog_text))
                .setPositiveButton(
                        this.res.getString(R.string.gps_off_dialog_positive_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                ctx.startActivity(intent);
                            }
                        })
                .create();
        this.dialog.show();
    }

    private void showLocationNotDefinedDialog() {
        this.dialog = new AlertDialog.Builder(this.ctx)
                .setTitle(this.res.getString(R.string.location_not_defined_dialog_title))
                .setCancelable(false)
                .setMessage(this.res.getString(R.string.location_not_defined_dialog_text))
                .setPositiveButton(R.string.location_not_defined_dialog_positive_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                retries = 0;
                                schedule();
                            }
                        })
                .setNegativeButton("Usar o hack", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lastKnowLocation = new Location("");
                        lastKnowLocation.setLatitude(-22.95);
                        lastKnowLocation.setLongitude(-43.);
                    }
                })
                .create();
        this.dialog.show();
    }

    public void onResume() {
        if ( this.locationManager != null ) {
            if (this.dialog != null) {
                this.dialog.dismiss();
            }
            if (this.lastKnowLocation == null) {
                if (this.isFirstTime) {
                    this.isFirstTime = false;
                    this.setup();
                } else {
                    this.schedule();
                }
            }
        }
    }

    private void schedule() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setup();
            }
        }, 1000 * SCHEDULE_DELAY_IN_SECONDS);
    }

    public LatLng getMidPoint(LatLng from, LatLng to){

        double lat1 = from.latitude;
        double lon1 = from.longitude;
        double lat2 = to.latitude;
        double lon2 = to.longitude;

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
