package com.mcardoso.doutorrj.model.location;

/**
 * Created by mcardoso on 1/3/16.
 */
public class Bounds {

    private LatLng northeast;
    private LatLng southwest;

    public LatLng getNortheast() {
        return northeast;
    }

    public void setNortheast(LatLng northeast) {
        this.northeast = northeast;
    }

    public LatLng getSouthwest() {
        return southwest;
    }

    public void setSouthwest(LatLng southwest) {
        this.southwest = southwest;
    }
}
