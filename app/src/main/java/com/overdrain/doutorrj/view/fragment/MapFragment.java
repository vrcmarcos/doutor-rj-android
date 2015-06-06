package com.overdrain.doutorrj.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.overdrain.doutorrj.MainActivity;
import com.overdrain.doutorrj.R;

public class MapFragment extends Fragment {

    private static MapView mapView;
    private static GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        this.mapView = (MapView) view.findViewById(R.id.mapView);
        this.mapView.onCreate(savedInstanceState);
        this.mapView.onResume();

        MapsInitializer.initialize(MainActivity.getInstance().getApplicationContext());

        this.googleMap = this.mapView.getMap();
        double latitude = 17.385044;
        double longitude = 78.486671;
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("Hello Maps");

        this.googleMap.addMarker(marker);

        try {
            float zoom = savedInstanceState.getFloat("ZOOM");
            if (zoom != 0) {
                this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));
            }
        } catch ( NullPointerException e ) {
            Log.i("hehe", "ozom nao encontrado");
        }

        return view;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putFloat("ZOOM", this.googleMap.getCameraPosition().zoom);
        super.onSaveInstanceState(outState);
    }
}
