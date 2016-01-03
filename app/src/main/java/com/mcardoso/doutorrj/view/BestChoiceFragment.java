package com.mcardoso.doutorrj.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.Establishment;
import com.mcardoso.doutorrj.util.LocationTracker;

/**
 * Created by mcardoso on 12/8/15.
 */
public class BestChoiceFragment extends NotifiableFragment {

    private static String TAG = "BestChoiceFragment";
    private static int DEFAULT_ZOOM = 12;
    private static LatLng LAT_LNG_DEFAULT_CITY = new LatLng(-22.95,-43.2);

    private MapView mapView;
    private GoogleMap map;
    private RelativeLayout tagBoard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        this.mapView = (MapView) view.findViewById(R.id.map_view);
        this.mapView.onCreate(savedInstanceState);
        this.map = this.mapView.getMap();
        this.map.setMyLocationEnabled(true);
        MapsInitializer.initialize(this.getActivity());
        this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(LAT_LNG_DEFAULT_CITY, DEFAULT_ZOOM));
        this.tagBoard = (RelativeLayout) view.findViewById(R.id.tag_board);
        return view;
    }

    @Override
    protected boolean useLoadingScreen() {
        return false;
    }

    @Override
    protected Integer getTargetLayoutId() {
        return R.layout.fragment_best_choice;
    }

    @Override
    public void draw() {
        this.map.clear();
        this.tagBoard.removeAllViews();
        Establishment bestChoice = super.getCurrentList().get(0);
        LatLng bestChoiceLatLng = bestChoice.getLatLng();
        this.map.addMarker(
                new MarkerOptions()
                        .title(bestChoice.getName())
                        .position(bestChoiceLatLng)
        );

        LatLng midPoint = LocationTracker.getInstance().getMidPoint(LAT_LNG, bestChoiceLatLng);
        this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(midPoint, DEFAULT_ZOOM));

        BootstrapLabel label = new BootstrapLabel(this.getContext());
        label.setRounded(true);
        label.setText(bestChoice.getName());
        this.tagBoard.addView(label);
    }

    @Override
    public void onResume() {
        super.onResume();
        if( this.mapView != null ) {
            this.mapView.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if( this.mapView != null ) {
            this.mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if( this.mapView != null ) {
            this.mapView.onLowMemory();
        }
    }
}
