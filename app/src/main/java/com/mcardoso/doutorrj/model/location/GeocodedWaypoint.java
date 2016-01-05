package com.mcardoso.doutorrj.model.location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mcardoso on 1/3/16.
 */
public class GeocodedWaypoint {

    @SerializedName("geocoder_status")
    private String status;

    @SerializedName("place_id")
    private String placeId;

    private List<String> types;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
