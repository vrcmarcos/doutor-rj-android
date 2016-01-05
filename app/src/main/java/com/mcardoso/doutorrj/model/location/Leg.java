package com.mcardoso.doutorrj.model.location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mcardoso on 1/3/16.
 */
public class Leg {

    private Property distance;

    private Property duration;

    @SerializedName("end_address")
    private String endAddress;

    @SerializedName("end_location")
    private LatLng endLocation;

    @SerializedName("start_address")
    private String startAddress;

    @SerializedName("start_location")
    private LatLng startLocation;

    private List<Step> steps;

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

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLng endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
