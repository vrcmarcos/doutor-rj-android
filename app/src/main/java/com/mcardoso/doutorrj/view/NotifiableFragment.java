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
import com.mcardoso.doutorrj.model.Establishment;
import com.mcardoso.doutorrj.model.EstablishmentType;
import com.mcardoso.doutorrj.response.EstablishmentsPerTypeResponse;
import com.mcardoso.doutorrj.util.EstablishmentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcardoso on 12/17/15.
 */
public abstract class NotifiableFragment extends Fragment {

    private static List<NotifiableFragment> NOTIFIABLE_FRAGMENTS = new ArrayList<>();

    protected View view;
    protected Bundle savedInstanceState;

    protected static EstablishmentsPerTypeResponse ESTABLISHMENTS_PER_TYPE_RESPONSE;
    protected static Location LOCATION;
    protected static LatLng LAT_LNG;

    private static EstablishmentType CURRENT_TYPE = EstablishmentType.getDefault();
    private static List<Establishment> CURRENT_LIST = null;
    private static Boolean ESTABLISHMENTS_LIST_SORTED = false;

    public abstract void draw();

    protected abstract Integer getTargetLayoutId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NOTIFIABLE_FRAGMENTS.add(this);
        this.view = inflater.inflate(R.layout.fragment_load, container, false);
        this.savedInstanceState = savedInstanceState;
        return this.view;
    }

    public static void updateEstablishmentsPerTypeResponse(EstablishmentsPerTypeResponse response) {
        ESTABLISHMENTS_PER_TYPE_RESPONSE = response;
    }

    public static void broadcastEstablishmentTypeChange(EstablishmentType newType) {
        if ( !CURRENT_TYPE.equals(newType) ) {
            CURRENT_TYPE = newType;
            CURRENT_LIST = null;
            checkConditions();
        }
    }

    public static void broadcastEstablishmentsPerTypeResponse(EstablishmentsPerTypeResponse response) {
        ESTABLISHMENTS_PER_TYPE_RESPONSE = response;
        checkConditions();
    }

    public static void broadcastLocation(Location location) {
        LOCATION = location;
        LAT_LNG = new LatLng(location.getLatitude(), location.getLongitude());
        checkConditions();
    }

    private static void checkConditions() {
        if ( ESTABLISHMENTS_PER_TYPE_RESPONSE != null && LOCATION != null ) {

            if ( !ESTABLISHMENTS_LIST_SORTED ) {
                for (EstablishmentsPerTypeResponse.EstablishmentsPerType establishmentsList : ESTABLISHMENTS_PER_TYPE_RESPONSE.getResults()) {
                    EstablishmentUtils.sort(establishmentsList.getEstablishments(), LAT_LNG);
                }
                ESTABLISHMENTS_LIST_SORTED = true;
            }

            for (NotifiableFragment frag : NOTIFIABLE_FRAGMENTS) {
                frag.changeLayout();
                frag.draw();
            }
        }
    }

    protected List<Establishment> getCurrentList() {
        if ( CURRENT_LIST == null ) {
            for (EstablishmentsPerTypeResponse.EstablishmentsPerType e : ESTABLISHMENTS_PER_TYPE_RESPONSE.getResults()) {
                if (e.getType().equals(CURRENT_TYPE)) {
                    CURRENT_LIST = e.getEstablishments();
                    break;
                }
            }
        }
        return CURRENT_LIST;
    }

    protected void changeLayout() {
        RelativeLayout layout = (RelativeLayout) this.view.findViewById(R.id.fragment_load);
        layout.removeAllViews();
        layout.addView(View.inflate(this.view.getContext(), this.getTargetLayoutId(), null));
    }

    protected LatLng getMidPoint(LatLng latLng){

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
