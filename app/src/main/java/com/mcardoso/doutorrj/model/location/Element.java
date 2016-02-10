package com.mcardoso.doutorrj.model.location;

/**
 * Created by mcardoso on 1/7/16.
 */
public class Element {

    private Property distance;
    private Property duration;
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
