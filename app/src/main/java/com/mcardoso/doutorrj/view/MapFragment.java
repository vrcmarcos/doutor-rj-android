package com.mcardoso.doutorrj.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.helper.BootstrapHelper;
import com.mcardoso.doutorrj.helper.RequestHelper;
import com.mcardoso.doutorrj.model.establishment.Establishment;
import com.mcardoso.doutorrj.model.location.Leg;
import com.mcardoso.doutorrj.model.location.Property;
import com.mcardoso.doutorrj.model.location.Step;
import com.mcardoso.doutorrj.response.GoogleMapsDirectionsResponse;

import java.util.List;

/**
 * Created by mcardoso on 12/8/15.
 */
public class MapFragment extends NotifiableFragment {

    private static String TAG = "MapFragment";
    private static int DEFAULT_ZOOM = 12;
    private static LatLng LAT_LNG_DEFAULT_CITY = new LatLng(-22.95,-43.2);

    private MapView mapView;
    private GoogleMap map;
    private RelativeLayout dashboard;
    private Gson gson;

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
        this.map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                checkConditions();
            }
        });

        MapsInitializer.initialize(this.getActivity());
        this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(LAT_LNG_DEFAULT_CITY, DEFAULT_ZOOM));
        this.dashboard = (RelativeLayout) view.findViewById(R.id.dashboard);
        return view;
    }

    @Override
    protected boolean useLoadingScreen() {
        return false;
    }

    @Override
    protected Integer getTargetLayoutId() {
        return R.layout.fragment_map;
    }

    private Establishment getCurrentEstablishment() {
        return super.getCurrentList().get(0);
    }

    public void update(Establishment establishment) {
        this.drawMap(establishment);
    }

    @Override
    public void draw() {
        this.drawMap(this.getCurrentEstablishment());
    }

    private void drawMap(Establishment establishment) {
        this.map.clear();
        LatLng establishmentLatLng = establishment.getLatLng();
        final Marker marker = this.map.addMarker(
                new MarkerOptions()
                        .title(establishment.getName())
                        .position(establishmentLatLng)
        );
        this.map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (!marker.isInfoWindowShown()) {
                    marker.showInfoWindow();
                }
            }
        });

        Double pivotLatitude = ( 2 * establishment.getLatitude() ) - LAT_LNG.latitude;
        Double pivotLongitude = ( 2 * establishment.getLongitude() ) - LAT_LNG.longitude;

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(pivotLatitude, pivotLongitude))
                .include(LAT_LNG)
                .include(establishmentLatLng)
                .build();
        CameraUpdate camUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 50);
        this.map.animateCamera(camUpdate, 250, null);
        marker.showInfoWindow();
        this.updateDashboard(marker.getPosition());

        String mapsUrl = getString(
                R.string.maps_api_travel_info,
                LAT_LNG.latitude,
                LAT_LNG.longitude,
                establishmentLatLng.latitude,
                establishmentLatLng.longitude
        );

        new RequestHelper(mapsUrl, RequestHelper.Method.GET, new RequestHelper.RestRequestCallback() {
            @Override
            public void onRequestSuccess(String json) {
                Log.d(TAG, json);

                GoogleMapsDirectionsResponse directions = gson.fromJson(json, GoogleMapsDirectionsResponse.class);
                Leg leg = directions.getRoutes().get(0).getLegs().get(0);
                final Property duration = leg.getDuration();
                final List<Step> steps = leg.getSteps();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addLine(steps);
//                        addTag(duration.getText());
                    }
                });
            }

            @Override
            public void onRequestFail() {
            }
        }).execute();
    }

    private void updateDashboard(final LatLng position) {

        final String goToURL = getString(R.string.maps_api_go_to,
                position.latitude,
                position.longitude
        );

        BootstrapButton goToButton = (BootstrapButton) this.dashboard.findViewById(R.id.button_go_to);
        goToButton.setVisibility(View.VISIBLE);
        goToButton.setBootstrapBrand(BootstrapHelper.getGoToBrand());
        goToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(goToURL));
                startActivity(intent);
            }
        });

        BootstrapButton requestUberButton = (BootstrapButton) this.dashboard.findViewById(R.id.button_request_uber);
        requestUberButton.setVisibility(View.VISIBLE);
        requestUberButton.setBootstrapBrand(BootstrapHelper.getUberBrand());
        requestUberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PackageManager pm = getContext().getPackageManager();
                    pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
                    String uri = getString(
                            R.string.uber_deep_linking_url,
                            R.string.uber_client_id,
                            position.latitude,
                            position.longitude
                    );
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                } catch (PackageManager.NameNotFoundException e) {
                    String url = getString(
                            R.string.uber_mobile_website,
                            R.string.uber_client_id
                    );
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });
    }

    private void addLine(List<Step> steps) {
        PolylineOptions rectLine = new PolylineOptions();

        for (Step step : steps) {
            rectLine.add(step.getStartLocation().getLatLng());
            for(LatLng latLng : step.getPolyline().getDecodedPolyline()) {
                rectLine.add(latLng);
            }
            rectLine.add(step.getEndLocation().getLatLng());
        }

        rectLine.width(30).color(this.getResources().getColor(R.color.mapPolylineBorder));
        this.map.addPolyline(rectLine);
        rectLine.width(18).color(this.getResources().getColor(R.color.mapPolyline));
        this.map.addPolyline(rectLine);
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
