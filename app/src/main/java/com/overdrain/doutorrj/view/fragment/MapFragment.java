package com.overdrain.doutorrj.view.fragment;

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
import com.google.maps.android.clustering.ClusterManager;
import com.overdrain.doutorrj.MainActivity;
import com.overdrain.doutorrj.R;
import com.overdrain.doutorrj.database.StorageManager;
import com.overdrain.doutorrj.model.navigation.MapPointItem;
import com.overdrain.doutorrj.model.rest.Establishment;
import com.overdrain.doutorrj.utils.GPSTracker;

import java.util.List;

public class MapFragment extends Fragment {

    private static ClusterManager<MapPointItem> clusterManager;

    private enum DataKey {
        ZOOM
    }

    private static String TAG = "MapFragment";

    private static Float DEFAULT_ZOOM = 9f;

    private static MapView mapView;
    private static GoogleMap googleMap;

    public MapFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        this.mapView = (MapView) view.findViewById(R.id.mapView);
        this.mapView.onCreate(savedInstanceState);
        this.mapView.onResume();

        MapsInitializer.initialize(MainActivity.getInstance().getApplicationContext());

        this.googleMap = this.mapView.getMap();
        float zoom = StorageManager.getInstance().getFloat(DataKey.ZOOM.toString(), DEFAULT_ZOOM);
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(GPSTracker.getInstance().getLatLng(), zoom));

        this.clusterManager = new ClusterManager<MapPointItem>(this.mapView.getContext(), this.googleMap);

        this.googleMap.setOnCameraChangeListener(this.clusterManager);
        this.googleMap.setOnMarkerClickListener(this.clusterManager);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "Zoom: " + this.googleMap.getCameraPosition().zoom);
        StorageManager.getInstance().setFloat(DataKey.ZOOM.toString(), this.googleMap.getCameraPosition().zoom);
    }

    public static void updateMap(List<Establishment> establishments) {

        for( Establishment establishment : establishments) {

            double latitude = Double.parseDouble(establishment.getLatitude());
            double longitude = Double.parseDouble(establishment.getLongitude());
            clusterManager.addItem(new MapPointItem(latitude, longitude));
        }
    }
}
