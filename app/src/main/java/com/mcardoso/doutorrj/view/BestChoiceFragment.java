package com.mcardoso.doutorrj.view;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.Establishment;

/**
 * Created by mcardoso on 12/8/15.
 */
public class BestChoiceFragment extends NotifiableFragment {

    private static String TAG = "BestChoiceFragment";
    private static int DEFAULT_ZOOM = 10;

    private MapView mapView;
    private GoogleMap map;

    @Override
    protected Integer getTargetLayoutId() {
        return R.layout.fragment_best_choice;
    }

    @Override
    public void draw() {

        this.mapView = (MapView) super.view.findViewById(R.id.map_view);
        this.mapView.onCreate(savedInstanceState);

        this.map = this.mapView.getMap();
        this.map.getUiSettings().setMyLocationButtonEnabled(true);
        this.map.setMyLocationEnabled(true);

        MapsInitializer.initialize(this.getActivity());

        Establishment bestChoice = ESTABLISHMENTS_LIST.getResults().get(0);

        LatLng bestChoiceLatLng = bestChoice.getLatLng();
        this.map.addMarker(new MarkerOptions()
                        .position(bestChoiceLatLng)
                        .title(bestChoice.getName())
        );

        this.map.addMarker(new MarkerOptions()
                        .position(LAT_LNG)
                        .title(getResources().getString(R.string.best_choice_map_title))
        );

        LatLng midPoint = super.getMidPoint(bestChoiceLatLng);

        CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(midPoint, DEFAULT_ZOOM);
        this.map.animateCamera(camUpdate);
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
