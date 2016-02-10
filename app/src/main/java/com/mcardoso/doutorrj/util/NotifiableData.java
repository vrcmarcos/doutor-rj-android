package com.mcardoso.doutorrj.util;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.mcardoso.doutorrj.R;
import com.mcardoso.doutorrj.model.establishment.Establishment;
import com.mcardoso.doutorrj.model.establishment.EstablishmentType;
import com.mcardoso.doutorrj.model.establishment.EstablishmentsPerType;
import com.mcardoso.doutorrj.model.location.Element;
import com.mcardoso.doutorrj.response.GoogleMapsDistanceMatrixResponse;
import com.mcardoso.doutorrj.view.NotifiableFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by mcardoso on 1/7/16.
 */
public class NotifiableData {

    private static String TAG = "NotifiableData";

    private static NotifiableData ourInstance = new NotifiableData();

    public static NotifiableData getInstance() {
        return ourInstance;
    }

    private LatLng userLatLng;
    private Activity activity;
    private EstablishmentType currentType;
    private List<NotifiableFragment> fragments;
    private List<EstablishmentsPerType> establishmentsPerType;
    private Map<EstablishmentType, Boolean> sortedFlag;

    private NotifiableData() {
        this.fragments = new ArrayList<>();
        this.currentType = EstablishmentType.getDefault();
        this.sortedFlag = new HashMap<>();
        for (EstablishmentType type : EstablishmentType.values()) {
            this.sortedFlag.put(type, false);
        }
    }

    public void registerActivity(Activity activity) {
        this.activity = activity;
    }

    public void registerFragment(NotifiableFragment fragment) {
        this.fragments.add(fragment);
    }

    public void broadcastEstablishmentTypeChange(EstablishmentType newType) {
        if ( !this.currentType.equals(newType) ) {
            this.currentType = newType;
            checkConditions();
        }
    }

    public void broadcastEstablishmentsPerTypeResponse(List<EstablishmentsPerType> response) {
        this.establishmentsPerType = response;
        checkConditions();
    }

    public void broadcastLocation(Location location) {
        this.userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        checkConditions();
    }

    private void checkConditions() {
        if ( this.establishmentsPerType != null && this.userLatLng != null ) {
            fetchListForCurrentType();
        }
    }

    private void fetchListForCurrentType() {
        if (this.sortedFlag.get(this.currentType)) {
            draw();
        } else {
            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    StringBuilder formattedLatLngBuilder = new StringBuilder();
                    final List<Establishment> establishments = getEstablishmentsForType(currentType);
                    for (Establishment e : establishments) {
                        formattedLatLngBuilder.append(e.getLatitude());
                        formattedLatLngBuilder.append(",");
                        formattedLatLngBuilder.append(e.getLongitude());
                        formattedLatLngBuilder.append("|");
                    }

                    String formattedLatLng = formattedLatLngBuilder
                            .deleteCharAt(formattedLatLngBuilder.length() - 1).toString();

                    String url = activity.getResources().getString(R.string.maps_api_directions,
                            userLatLng.latitude,
                            userLatLng.longitude,
                            formattedLatLng
                    );

                    new RestRequest(url, RestRequest.Method.GET, new RestRequest.RestRequestCallback() {
                        @Override
                        public void onRequestSuccess(String json) {
                            Log.d(TAG, json);
                            GoogleMapsDistanceMatrixResponse response = new Gson().fromJson(json, GoogleMapsDistanceMatrixResponse.class);
                            List<Element> elements = response.getRows().get(0).getElements();
                            for ( int i = 0; i < establishments.size(); i++ ) {
                                try {
                                    Element element = elements.get(i);
                                    Establishment establishment =  establishments.get(i);
                                    establishment.setDistance(Float.parseFloat(element.getDistance().getText().replace(" km", "")));
                                    establishment.setDuration(Integer.parseInt(element.getDuration().getText().replace(" mins", "")));
                                } catch ( Exception e ) {
                                    Log.e(TAG, e.getMessage(), e);
                                }
                            }
                            Collections.sort(establishments);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    draw();
                                }
                            });
                        }

                        @Override
                        public void onRequestFail(Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }).sync();

                    return null;
                }
            }.execute();
        }
    }

    private void draw() {
        for ( NotifiableFragment frag : this.fragments ) {
            frag.draw();
        }
    }

    private List<Establishment> getEstablishmentsForType(EstablishmentType type) {
        List<Establishment> establishments = null;
        for ( EstablishmentsPerType e : this.establishmentsPerType ) {
            if ( type.equals(e.getType()) ) {
                establishments = e.getEstablishments();
                break;
            }
        }
        return establishments;
    }

}
