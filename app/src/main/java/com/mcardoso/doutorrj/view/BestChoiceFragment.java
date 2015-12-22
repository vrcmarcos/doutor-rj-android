package com.mcardoso.doutorrj.view;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.EstablishmentsList;

/**
 * Created by mcardoso on 12/8/15.
 */
public class BestChoiceFragment extends NotifiableFragment {

    private static String TAG = "BestChoiceFragment";

    private MapView mapView;
    private GoogleMap map;

    @Override
    protected Integer getTargetLayoutId() {
        return R.layout.fragment_best_choice;
    }

    @Override
    public void handleNotification(EstablishmentsList establishmentsList) {
        Log.d(TAG, "Handling notification"+establishmentsList);

        //        this.mapView = (MapView) rootView.findViewById(R.id.map_view);
//        this.mapView.onCreate(savedInstanceState);
//
//        this.map = this.mapView.getMap();
//        this.map.getUiSettings().setMyLocationButtonEnabled(true);
//        this.map.setMyLocationEnabled(true);

//        Establishment bestChoice = MainActivity.ESTABLISHMENTS.getResults().get(0);
//
//        MapsInitializer.initialize(this.getActivity());
//
//        LatLng bestChoiceLatLng = bestChoice.getLatLng();
//        this.map.addMarker(new MarkerOptions()
//                        .position(bestChoiceLatLng)
//                        .title(bestChoice.getName())
//        );
//
//        LatLng currentLatLng = LocationTracker.getInstance().getCurrentLatLng();
//        this.map.addMarker(new MarkerOptions()
//                        .position(currentLatLng)
//                        .title(getResources().getString(R.string.best_choice_map_title))
//        );
//
//        LatLng midPoint = LocationTracker.getMidPoint(bestChoiceLatLng, currentLatLng);
//
//        CameraUpdate camUpdate = CameraUpdateFactory.newLatLngZoom(midPoint, 10);
//        this.map.animateCamera(camUpdate);

//        // TODO Auto-generated method stub
//        super.onConfigurationChanged(newConfig);
//        this.
//        RelativeLayout rl = (RelativeLayout) findViewById(R.id.about);
//        rl.removeAllViews();
//        rl.addView(View.inflate(myView.getContext(), R.layout.about, null));
//        this.mapView = (MapView) rootView.findViewById(R.id.map_view);
//        this.mapView.onCreate(savedInstanceState);
//
//        this.map = this.mapView.getMap();
//        this.map.getUiSettings().setMyLocationButtonEnabled(true);
//        this.map.setMyLocationEnabled(true);
//        super.changeLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
//        this.mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        this.mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        this.mapView.onLowMemory();
    }
}
