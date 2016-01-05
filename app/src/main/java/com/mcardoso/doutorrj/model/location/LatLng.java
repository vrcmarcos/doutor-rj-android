package com.mcardoso.doutorrj.model.location;

/**
 * Created by mcardoso on 1/3/16.
 */
public class LatLng {

    private float lat;
    private float lng;

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public com.google.android.gms.maps.model.LatLng getLatLng() {
        return new com.google.android.gms.maps.model.LatLng(lat, lng);
    }
}
