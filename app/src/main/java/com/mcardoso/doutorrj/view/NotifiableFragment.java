package com.mcardoso.doutorrj.view;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.model.LatLng;
import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.EstablishmentsList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcardoso on 12/17/15.
 */
public abstract class NotifiableFragment extends Fragment {

    private static List<NotifiableFragment> NOTIFIABLE_FRAGMENTS = new ArrayList<>();

    protected View view;
    protected Bundle savedInstanceState;
    protected static EstablishmentsList ESTABLISHMENTS_LIST;
    protected static Location LOCATION;
    protected static LatLng LAT_LNG;

    protected abstract Integer getTargetLayoutId();
    public abstract void draw();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NOTIFIABLE_FRAGMENTS.add(this);
        this.view = inflater.inflate(R.layout.fragment_load, container, false);
        this.savedInstanceState = savedInstanceState;
        return this.view;
    }

    public static void broadcastEstablishmentsList(EstablishmentsList establishmentsList) {
        ESTABLISHMENTS_LIST = establishmentsList;
        checkConditions();
    }

    public static void broadcastLocation(Location location) {
        LOCATION = location;
        LAT_LNG = new LatLng(location.getLatitude(), location.getLongitude());
        checkConditions();
    }

    private static void checkConditions() {
        if ( ESTABLISHMENTS_LIST != null && LOCATION != null ) {
            ESTABLISHMENTS_LIST.sort(LAT_LNG);
            for (NotifiableFragment frag : NOTIFIABLE_FRAGMENTS) {
                frag.changeLayout();
                frag.draw();
            }
        }
    }

    protected void changeLayout() {
        RelativeLayout layout = (RelativeLayout) this.view.findViewById(R.id.fragment_load);
        layout.removeAllViews();
        layout.addView(View.inflate(this.view.getContext(), this.getTargetLayoutId(), null));
    }

    public LatLng getMidPoint(LatLng latLng){

        double lat1 = latLng.latitude;
        double lon1 = latLng.longitude;
        double lat2 = LAT_LNG.latitude;
        double lon2 = LAT_LNG.longitude;

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
