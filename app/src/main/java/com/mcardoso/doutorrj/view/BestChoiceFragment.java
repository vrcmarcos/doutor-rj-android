package com.mcardoso.doutorrj.view;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.mcardoso.doutorrj.R;

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

        MapsInitializer.initialize(this.getActivity());
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
