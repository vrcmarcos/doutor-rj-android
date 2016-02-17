package com.mcardoso.doutorrj.model.location;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mcardoso on 1/3/16.
 */
public class Step {

    private Property distance;

    private Property duration;

    @SerializedName("end_location")
    private LatLng endLocation;

    @SerializedName("html_instructions")
    private String instructions;

    private Polyline polyline;

    @SerializedName("start_location")
    private LatLng startLocation;

    @SerializedName("travel_mode")
    private String travelMode;

    public Property getDistance() {
        return distance;
    }

    public void setDistance(Property distance) {
        this.distance = distance;
    }

    public Property getDuration() {
        return duration;
    }

    public void setDuration(Property duration) {
        this.duration = duration;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLng endLocation) {
        this.endLocation = endLocation;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }
}
