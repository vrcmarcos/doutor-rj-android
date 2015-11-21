package com.overdrain.doutorrj.model.navigation;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by mcardoso on 9/17/15.
 */
public class MapPointItem implements ClusterItem {

    private final LatLng position;

    public MapPointItem(double lat, double lng) {
        position = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return position;
    }
}
