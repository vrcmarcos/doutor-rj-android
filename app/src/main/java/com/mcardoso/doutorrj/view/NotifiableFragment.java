package com.mcardoso.doutorrj.view;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.model.LatLng;
import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.helper.EstablishmentHelper;
import com.mcardoso.doutorrj.model.establishment.Establishment;
import com.mcardoso.doutorrj.model.establishment.EstablishmentType;
import com.mcardoso.doutorrj.response.EstablishmentsPerTypeResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcardoso on 12/17/15.
 */
public abstract class NotifiableFragment extends Fragment {

    private static List<NotifiableFragment> NOTIFIABLE_FRAGMENTS = new ArrayList<>();
    private static EstablishmentType CURRENT_TYPE = EstablishmentType.getDefault();
    private static List<Establishment> CURRENT_LIST = null;
    private static Boolean ESTABLISHMENTS_LIST_SORTED = false;

    protected static final int SCHEDULE_DELAY_IN_SECONDS = 2;
    protected static EstablishmentsPerTypeResponse ESTABLISHMENTS_PER_TYPE_RESPONSE;
    protected static Location LOCATION;
    protected static LatLng LAT_LNG;

    protected View view;
    protected Bundle savedInstanceState;
    protected RelativeLayout loadingLayout;
    protected FragmentActivity activity;

    public abstract void draw();

    protected abstract Integer getTargetLayoutId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NOTIFIABLE_FRAGMENTS.add(this);
        int layoutId = this.getTargetLayoutId();
        this.view = inflater.inflate(layoutId, container, false);
        this.loadingLayout = (RelativeLayout) this.view.findViewById(R.id.fragment_load);
        this.savedInstanceState = savedInstanceState;
        this.activity = getActivity();
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

    public static void checkConditions() {
        if ( ESTABLISHMENTS_PER_TYPE_RESPONSE != null && LOCATION != null ) {

            if ( !ESTABLISHMENTS_LIST_SORTED ) {
                for (EstablishmentsPerTypeResponse.EstablishmentsPerType establishmentsList : ESTABLISHMENTS_PER_TYPE_RESPONSE.getResults()) {
                    EstablishmentHelper.sort(establishmentsList.getEstablishments(), LAT_LNG);
                }
                ESTABLISHMENTS_LIST_SORTED = true;
            }

            for (NotifiableFragment frag : NOTIFIABLE_FRAGMENTS) {
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

    protected void removeLoadingScreen() {
        this.loadingLayout.setVisibility(View.INVISIBLE);
    }
}
