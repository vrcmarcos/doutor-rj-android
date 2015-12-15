package com.mcardoso.doutorrj.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mcardoso.doutorrj.MainActivity;
import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.Establishment;
import com.mcardoso.doutorrj.util.LocationTracker;

/**
 * Created by mcardoso on 12/8/15.
 */
public class BestChoiceFragment extends Fragment {

    private static String TAG = "BestChoiceFragment";

    MapView mapView;
    GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_best_choice, container, false);
        this.mapView = (MapView) rootView.findViewById(R.id.mapview);
        this.mapView.onCreate(savedInstanceState);

        this.map = this.mapView.getMap();
        this.map.getUiSettings().setMyLocationButtonEnabled(true);
        this.map.setMyLocationEnabled(true);

        Establishment bestChoice = MainActivity.ESTABLISHMENTS.getResults().get(0);

        MapsInitializer.initialize(this.getActivity());

        LatLng bestChoiceLatLng = bestChoice.getLatLng();
        this.map.addMarker(new MarkerOptions()
                        .position(bestChoiceLatLng)
                        .title(bestChoice.getName())
        );

        LatLng currentLatLng = LocationTracker.getInstance().getCurrentLatLng();
        this.map.addMarker(new MarkerOptions()
                        .position(currentLatLng)
                        .title(getResources().getString(R.string.best_choice_map_title))
        );

        LatLng midPoint = LocationTracker.getMidPoint(bestChoiceLatLng, currentLatLng);

        CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(midPoint, 10);
        this.map.animateCamera(camUpdate);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        this.mapView.onLowMemory();
    }
}
