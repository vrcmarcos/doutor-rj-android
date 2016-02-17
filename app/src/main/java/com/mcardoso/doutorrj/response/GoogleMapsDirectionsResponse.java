package com.mcardoso.doutorrj.response;

import com.google.gson.annotations.SerializedName;
import com.mcardoso.doutorrj.model.location.GeocodedWaypoint;
import com.mcardoso.doutorrj.model.location.Route;

import java.util.List;

/**
 * Created by mcardoso on 1/3/16.
 */
public class GoogleMapsDirectionsResponse {

    @SerializedName("geocoded_waypoints")
    private List<GeocodedWaypoint> geocodedWaypoints;

    private List<Route> routes;

    private String status;

    public List<GeocodedWaypoint> getGeocodedWaypoints() {
        return geocodedWaypoints;
    }

    public void setGeocodedWaypoints(List<GeocodedWaypoint> geocodedWaypoints) {
        this.geocodedWaypoints = geocodedWaypoints;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
