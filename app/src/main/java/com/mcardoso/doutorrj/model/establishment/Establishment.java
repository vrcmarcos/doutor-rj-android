package com.mcardoso.doutorrj.model.establishment;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mcardoso on 12/7/15.
 */
public class Establishment {

    @SerializedName("nome")
    String name;

    @SerializedName("logradouro")
    String address;

    @SerializedName("telefone")
    String phone;

    @SerializedName("esfera_administrativa")
    Boolean privateEstablishment;

    Double latitude;

    Double longitude;

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public LatLng getLatLng() {
        return new LatLng(this.getLatitude(), this.getLongitude());
    }

    public Location getLocation() {
        Location location = new Location("");
        location.setLatitude(getLatitude());
        location.setLongitude(getLongitude());
        return location;
    }
}
