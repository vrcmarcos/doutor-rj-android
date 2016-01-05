package com.mcardoso.doutorrj.model.location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mcardoso on 1/3/16.
 */
public class Route {

    private Bounds bounds;

    private String copyrights;

    private List<Leg> legs;

    @SerializedName("overview_polyline")
    private Polyline overviewPolyline;

    private String summary;

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public Polyline getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(Polyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
