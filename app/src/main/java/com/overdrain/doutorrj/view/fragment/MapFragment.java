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
import com.google.maps.android.clustering.ClusterManager;
import com.overdrain.doutorrj.MainActivity;
import com.overdrain.doutorrj.R;
import com.overdrain.doutorrj.model.navigation.MapPointItem;
import com.overdrain.doutorrj.model.rest.Establishment;
import com.overdrain.doutorrj.utils.GPSTracker;

import java.util.List;

public class MapFragment extends Fragment {

    private static String TAG = "MapFragment";
    private static ClusterManager<MapPointItem> CLUSTER_MANAGER;
    private static Float DEFAULT_ZOOM = 11f;
    private static MapView MAP_VIEW;
    private static GoogleMap GOOGLE_MAP;

    public MapFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        MapsInitializer.initialize(MainActivity.getInstance().getApplicationContext());

        MAP_VIEW = (MapView) view.findViewById(R.id.mapView);
        MAP_VIEW.onCreate(savedInstanceState);
        MAP_VIEW.onResume();

        GOOGLE_MAP = MAP_VIEW.getMap();
        GOOGLE_MAP.animateCamera(CameraUpdateFactory.newLatLngZoom(GPSTracker.getInstance().getLatLng(), DEFAULT_ZOOM));

        CLUSTER_MANAGER = new ClusterManager<MapPointItem>(MAP_VIEW.getContext(), GOOGLE_MAP);

        GOOGLE_MAP.setOnCameraChangeListener(CLUSTER_MANAGER);
        GOOGLE_MAP.setOnMarkerClickListener(CLUSTER_MANAGER);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "Zoom: " + GOOGLE_MAP.getCameraPosition().zoom);
    }

    public static void updateMap(List<Establishment> establishments) {

        for( Establishment establishment : establishments) {

            double latitude = Double.parseDouble(establishment.getLatitude());
            double longitude = Double.parseDouble(establishment.getLongitude());
            CLUSTER_MANAGER.addItem(new MapPointItem(latitude, longitude));
        }
    }
}
