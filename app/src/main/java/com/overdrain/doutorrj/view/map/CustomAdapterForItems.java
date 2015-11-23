package com.overdrain.doutorrj.view.map;

import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.overdrain.doutorrj.R;

/**
 * Created by mcardoso on 11/22/15.
 */
public class CustomAdapterForItems implements GoogleMap.InfoWindowAdapter {

    private final View contentsView;

    public CustomAdapterForItems(LayoutInflater inflater) {
        this.contentsView = inflater.inflate(R.layout.info_window_item, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return this.contentsView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return this.contentsView;
    }
}
