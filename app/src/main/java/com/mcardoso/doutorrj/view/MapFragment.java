package com.mcardoso.doutorrj.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.helper.AnalyticsHelper;
import com.mcardoso.doutorrj.helper.BootstrapHelper;
import com.mcardoso.doutorrj.helper.RequestHelper;
import com.mcardoso.doutorrj.model.establishment.Establishment;
import com.mcardoso.doutorrj.model.location.Leg;
import com.mcardoso.doutorrj.model.location.Property;
import com.mcardoso.doutorrj.model.location.Step;
import com.mcardoso.doutorrj.response.GoogleMapsDirectionsResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mcardoso on 12/8/15.
 */
public class MapFragment extends NotifiableFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static String TAG = "MapFragment";

    private static int DEFAULT_ZOOM = 12;
    private static LatLng LAT_LNG_DEFAULT_CITY = new LatLng(-22.95,-43.2);
    private static float DEFAULT_MARKER_NO_FOCUS_ALPHA = 0.4f;
    private static BitmapDescriptor MARKER_PRIMARY_COLOR;
    private static BitmapDescriptor MARKER_SECONDARY_COLOR;

    private BootstrapLabel labelTime;
    private BootstrapButton buttonCentralize;
    private BootstrapButton buttonGoTo;
    private BootstrapButton buttonUber;
    private MapView mapView;
    private GoogleMap map;
    private Gson gson;
    private CameraUpdate camUpdate;
    private List<Polyline> polylines;
    private Map<Establishment, Marker> markersMap;
    private Establishment currentEstablishment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        this.gson = new Gson();
        this.mapView = (MapView) view.findViewById(R.id.map_view);
        this.mapView.onCreate(savedInstanceState);
        this.mapView.getMapAsync(this);

        RelativeLayout dashboard = (RelativeLayout) view.findViewById(R.id.dashboard);
        this.buttonGoTo = (BootstrapButton) dashboard.findViewById(R.id.button_go_to);
        this.buttonGoTo.setVisibility(View.INVISIBLE);

        this.buttonUber = (BootstrapButton) dashboard.findViewById(R.id.button_request_uber);
        this.buttonUber.setVisibility(View.INVISIBLE);

        this.labelTime = (BootstrapLabel) dashboard.findViewById(R.id.label_time);
        this.labelTime.setVisibility(View.INVISIBLE);
        this.labelTime.setBootstrapHeading(BootstrapHelper.Heading.H7);

        this.buttonCentralize = (BootstrapButton) dashboard.findViewById(R.id.button_centralize);
        this.buttonCentralize.setVisibility(View.INVISIBLE);

        this.markersMap = new HashMap<>();
        this.polylines = new ArrayList<>();

        MARKER_PRIMARY_COLOR = getHsvColor(R.color.markerPrimaryColor);
        MARKER_SECONDARY_COLOR = getHsvColor(R.color.markerSecondaryColor);

        return view;
    }

    private BitmapDescriptor getHsvColor(int colorId) {
        float[] hsv = new float[3];
        Color.colorToHSV(getResources().getColor(colorId), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        this.map.getUiSettings().setMapToolbarEnabled(false);
        this.map.getUiSettings().setMyLocationButtonEnabled(false);
        this.map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                checkConditions();
            }
        });

        MapsInitializer.initialize(getActivity());
        this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(LAT_LNG_DEFAULT_CITY, DEFAULT_ZOOM));
    }

    @Override
    protected Integer getTargetLayoutId() {
        return R.layout.fragment_map;
    }

    public void update(Establishment establishment) {
        this.camUpdate = null;
        this.currentEstablishment = establishment;
        this.drawMarkerSpecifics();
    }

    @Override
    public void draw() throws SecurityException {
        if (super.isAdded() && this.map != null) {
            this.currentEstablishment = super.getCurrentList().get(0);
            this.map.clear();
            this.map.setMyLocationEnabled(true);
            this.map.setOnMarkerClickListener(this);
            this.drawMap();
            this.drawDashboard();
            super.removeLoadingScreen();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            draw();
                        }
                    });
                }
            }, 1000 * SCHEDULE_DELAY_IN_SECONDS);
        }
    }

    private void drawMap() {
        this.drawMarkers();
        this.drawMarkerSpecifics();
    }

    private void drawMarkerSpecifics() {
        Marker currentMarker = this.markersMap.get(this.currentEstablishment);
        currentMarker.showInfoWindow();
        currentMarker.setAlpha(1.0f);

        String mapsUrl = getString(
                R.string.maps_api_travel_info,
                LAT_LNG.latitude,
                LAT_LNG.longitude,
                this.currentEstablishment.getLatitude(),
                this.currentEstablishment.getLongitude()
        );

        centralize();

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
                        addTimeTag(duration.getText());
                    }
                });
            }

            @Override
            public void onRequestFail() {
            }
        }).execute();
    }

    private void drawMarkers() {
        for (int i = 0; i < super.getCurrentList().size(); i++) {
            Establishment establishment = super.getCurrentList().get(i);
            BitmapDescriptor color = i == 0 ? MARKER_PRIMARY_COLOR : MARKER_SECONDARY_COLOR;
            Marker marker = this.map.addMarker(
                    new MarkerOptions()
                            .title(establishment.getName())
                            .position(establishment.getLatLng())
                            .alpha(DEFAULT_MARKER_NO_FOCUS_ALPHA)
                            .icon(color)
            );
            this.markersMap.put(establishment, marker);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Marker oldMarker = this.markersMap.get(this.currentEstablishment);
        oldMarker.setAlpha(DEFAULT_MARKER_NO_FOCUS_ALPHA);

        Establishment result = null;
        for (Map.Entry<Establishment, Marker> entry : this.markersMap.entrySet())
        {
            if(entry.getValue().equals(marker)) {
                result = entry.getKey();
                break;
            }
        }
        this.update(result);
        return true;
    }

    private void drawDashboard() {
        this.buttonGoTo.setBootstrapBrand(BootstrapHelper.Brand.GO_TO);
        this.buttonGoTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsHelper.trackAction(getContext(), "Route");
                String goToURL = getString(R.string.maps_api_go_to,
                        currentEstablishment.getLatitude(),
                        currentEstablishment.getLongitude()
                );
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(goToURL));
                startActivity(intent);
            }
        });

        this.buttonUber.setBootstrapBrand(BootstrapHelper.Brand.UBER);
        this.buttonUber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsHelper.trackAction(getContext(), "Uber");
                try {
                    PackageManager pm = getContext().getPackageManager();
                    pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
                    String uri = getString(
                            R.string.uber_deep_linking_url,
                            R.string.uber_client_id,
                            currentEstablishment.getLatitude(),
                            currentEstablishment.getLongitude(),
                            currentEstablishment.getName()
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

        this.buttonCentralize.setBootstrapBrand(BootstrapHelper.Brand.CENTRALIZE);
        this.buttonCentralize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsHelper.trackAction(getContext(), "Centralize");
                centralize();
            }
        });

        this.buttonGoTo.setVisibility(View.VISIBLE);
        this.buttonUber.setVisibility(View.VISIBLE);
        this.buttonCentralize.setVisibility(View.VISIBLE);

    }

    private void centralize() {
        if (this.map != null) {
            if(this.camUpdate == null) {
                Double pivotLatitude = ( 2 * this.currentEstablishment.getLatitude() ) - LAT_LNG.latitude;
                Double pivotLongitude = ( 2 * this.currentEstablishment.getLongitude() ) - LAT_LNG.longitude;

                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(new LatLng(pivotLatitude, pivotLongitude))
                        .include(LAT_LNG)
                        .include(this.currentEstablishment.getLatLng())
                        .build();

                this.camUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 200);
            }
            this.map.animateCamera(camUpdate, 250, null);
        }
    }

    private void addLine(List<Step> steps) {
        for(Polyline polyline : this.polylines) {
            polyline.remove();
        }
        this.polylines.clear();

        PolylineOptions rectLine = new PolylineOptions();

        for (Step step : steps) {
            rectLine.add(step.getStartLocation().getLatLng());
            for(LatLng latLng : step.getPolyline().getDecodedPolyline()) {
                rectLine.add(latLng);
            }
            rectLine.add(step.getEndLocation().getLatLng());
        }

        rectLine.width(30).color(this.getResources().getColor(R.color.mapPolylineBorder));
        this.polylines.add(this.map.addPolyline(rectLine));
        rectLine.width(18).color(this.getResources().getColor(R.color.mapPolyline));
        this.polylines.add(this.map.addPolyline(rectLine));
    }

    private void addTimeTag(String text) {
        this.labelTime.setText(text);
        this.labelTime.setVisibility(View.VISIBLE);
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
