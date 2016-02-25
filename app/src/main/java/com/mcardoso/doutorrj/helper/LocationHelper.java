package com.mcardoso.doutorrj.helper;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.mcardoso.doutorrj.MainActivity;
import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.view.NotifiableFragment;

import java.util.ArrayList;
import java.util.List;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.ProviderError;
import fr.quentinklein.slt.TrackerSettings;

/**
 * Created by mcardoso on 12/13/15.
 */
public class LocationHelper {

    private static final int BROADCAST_LATLNG_DELAY_IN_SECONDS = 1;
    private static List<String> NECESSARY_PERMITIONS = new ArrayList<String>() {{
        add(Manifest.permission.ACCESS_FINE_LOCATION);
        add(Manifest.permission.ACCESS_COARSE_LOCATION);
    }};

    private Context ctx;
    private LocationTracker tracker;
    private Boolean locationFound;
    private AlertDialog alertDialog;

    public LocationHelper(Context ctx) {
        this.ctx = ctx;
        this.locationFound = false;
        this.start();
    }

    public void start() {
        if( !this.locationFound ) {
            List<String> unavailablePermissions = new ArrayList<String>();
            for (String permission : NECESSARY_PERMITIONS) {
                if (ContextCompat.checkSelfPermission(ctx, permission) != PackageManager.PERMISSION_GRANTED) {
                    unavailablePermissions.add(permission);
                }
            }
            if (unavailablePermissions.size() > 0) {
                ActivityCompat.requestPermissions(
                        (MainActivity) this.ctx,
                        unavailablePermissions.toArray(new String[0]),
                        1
                );
            } else {
                if (this.tracker == null) {
                    TrackerSettings settings = new TrackerSettings()
                            .setUseGPS(true)
                            .setUsePassive(true)
                            .setUseNetwork(true);
                    this.tracker = new LocationTracker(this.ctx, settings) {
                        @Override
                        public void onLocationFound(@NonNull final Location location) {
                            locationFound = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ((MainActivity) ctx).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            NotifiableFragment.broadcastLocation(location);
                                        }
                                    });
                                }
                            }, 1000 * BROADCAST_LATLNG_DELAY_IN_SECONDS);
                            stopListening();
                        }

                        @Override
                        public void onTimeout() {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            super.onProviderDisabled(provider);
                            showGPSOffDialog();
                        }

                        @Override
                        public void onProviderError(@NonNull ProviderError providerError) {
                            super.onProviderError(providerError);
                            showGPSOffDialog();
                        }
                    };
                }
                this.tracker.startListening();
            }
        }
    }

    private void showGPSOffDialog() {
        if( this.alertDialog == null ) {
            Resources res = this.ctx.getResources();
            this.alertDialog = new AlertDialog.Builder(this.ctx)
                    .setTitle(res.getString(R.string.gps_off_dialog_title))
                    .setCancelable(false)
                    .setMessage(res.getString(R.string.gps_off_dialog_text))
                    .setPositiveButton(
                            res.getString(R.string.gps_off_dialog_positive_button),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    ctx.startActivity(intent);
                                    alertDialog = null;
                                }
                            })
                    .create();
            this.alertDialog.show();
        }
    }

    public void onResume() {
        this.start();
    }

    public void onPause() {
        if ( this.tracker != null ) {
            this.tracker.stopListening();
        }
    }
}