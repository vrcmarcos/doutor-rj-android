package com.mcardoso.doutorrj.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.ButtonMode;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.establishment.Establishment;
import com.mcardoso.doutorrj.util.BootstrapCustom;

import java.util.List;

/**
 * Created by mcardoso on 12/8/15.
 */
public class BestChoiceFragment extends NotifiableFragment {

    private static String TAG = "BestChoiceFragment";
    private static int DEFAULT_ZOOM = 12;
    private static int MARKER_PADDING = 200;
    private static LatLng LAT_LNG_DEFAULT_CITY = new LatLng(-22.95,-43.2);

    private MapView mapView;
    private GoogleMap map;
    private RelativeLayout tagBoard;
    private Gson gson;
    private BootstrapButton button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        this.gson = new Gson();
        this.mapView = (MapView) view.findViewById(R.id.map_view);
        this.mapView.onCreate(savedInstanceState);
        this.map = this.mapView.getMap();
        this.map.setMyLocationEnabled(true);
        this.map.getUiSettings().setMapToolbarEnabled(false);
        this.map.getUiSettings().setMyLocationButtonEnabled(false);

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

//        Establishment bestChoice = super.getCurrentList().get(0);
//        LatLng bestChoiceLatLng = bestChoice.getLatLng();
//        final Marker marker = this.map.addMarker(
//                new MarkerOptions()
//                        .title(bestChoice.getName())
//                        .position(bestChoiceLatLng)
//        );
//        this.map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                if (!marker.isInfoWindowShown()) {
//                    marker.showInfoWindow();
//                }
//            }
//        });
//
//        Double latComp = bestChoiceLatLng.latitude - LAT_LNG.latitude;
//        Double lngComp = bestChoiceLatLng.longitude - LAT_LNG.longitude;
//
//        LatLng complement = new LatLng(bestChoiceLatLng.latitude + latComp,
//                bestChoiceLatLng.longitude + lngComp);
//
//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(marker.getPosition())
//                .include(LAT_LNG)
//                .include(complement)
//                .build();
//
//        this.map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, MARKER_PADDING), 1000, null);
//        marker.showInfoWindow();
//        this.createGoToButton(marker.getPosition());
//
//        List<Establishment> establishments = super.getCurrentList();
//        StringBuilder formattedLatLng = new StringBuilder();
//        for ( Establishment es : super.getCurrentList() ) {
//            formattedLatLng.append(es.getLatitude() + "," + es.getLongitude() + "|");
//        }
//
//        String latLngs = formattedLatLng.toString();
//        latLngs = latLngs.substring(0, latLngs.length() - 2);
//
//        String mapsUrl = getString(
//                R.string.maps_api_travel_info,
//                LAT_LNG.latitude,
//                LAT_LNG.longitude,
//                latLngs
//        );
//
//        mapsUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?origin=-22.99978157,-43.33069258&destination=-23.0055596,-43.3147587&key=AIzaSyDCcPHpSobZDi-9xrWBr5mznH3pLZEf-Is";
//
//        new RestRequest(mapsUrl, RestRequest.Method.GET, new RestRequest.RestRequestCallback() {
//            @Override
//            public void onRequestSuccess(String json) {
//                Log.d(TAG, json);
//
////                GoogleMapsDistanceMatrixResponse directions = gson.fromJson(json, GoogleMapsDistanceMatrixResponse.class);
////                Route route = directions.getRoutes().get(0);
////                final Bounds bounds = route.getBounds();
////                Leg leg = route.getLegs().get(0);
////                final Property duration = leg.getDuration();
////                final List<Step> steps = leg.getSteps();
////
////                getActivity().runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        addLine(bounds, steps);
//////                        addTag(duration.getText());
////                    }
////                });
//            }
//
//            @Override
//            public void onRequestFail(Exception e) {
//                Log.d(TAG, e.getMessage(), e);
//            }
//        }).execute();
    }

    @Override
    protected void onCurrentListReceived(List<Establishment> currentList) {

    }

    private void createGoToButton(LatLng position) {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 30);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);

        final String goToURL = getString(R.string.maps_api_go_to,
                position.latitude,
                position.longitude
        );

        this.button = new BootstrapButton(this.getContext());
        this.button.setText(this.getString(R.string.best_choice_go_to));
        this.button.setBootstrapSize(DefaultBootstrapSize.XL);
        this.button.setButtonMode(ButtonMode.REGULAR);
        this.button.setBootstrapBrand(BootstrapCustom.getBrand());
        this.button.setRounded(true);
        this.button.setLayoutParams(params);
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(goToURL));
                startActivity(intent);
            }
        });

        this.tagBoard.addView(this.button);
    }

//    private void addLine(Bounds bounds, List<Step> steps) {
//        PolylineOptions rectLine = new PolylineOptions();
//
//        for (Step step : steps) {
//            rectLine.add(step.getStartLocation().getLatLng());
//            rectLine.addAll(step.getPolyline().getDecodedPolyline());
//            rectLine.add(step.getEndLocation().getLatLng());
//        }
//
//        rectLine.width(30).color(this.getResources().getColor(R.color.polylineBorder));
//        this.map.addPolyline(rectLine);
//        rectLine.width(18).color(this.getResources().getColor(R.color.polyline));
//        this.map.addPolyline(rectLine);
//    }

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
