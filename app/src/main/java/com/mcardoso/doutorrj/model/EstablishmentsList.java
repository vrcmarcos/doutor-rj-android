package com.mcardoso.doutorrj.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mcardoso on 9/15/15.
 */
public class EstablishmentsList extends StorageModel {

    Integer count;
    String next;
    String previous;
    List<Establishment> results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Establishment> getResults() {
        return results;
    }

    public void setResults(List<Establishment> results) {
        this.results = results;
    }

    public void sort(final LatLng latLng) {
        final Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);

        Collections.sort(this.getResults(), new Comparator<Establishment>() {
            @Override
            public int compare(Establishment lhs, Establishment rhs) {
                Location location1 = new Location("");
                location1.setLongitude(Double.parseDouble(lhs.getLongitude()));
                location1.setLatitude(Double.parseDouble(lhs.getLatitude()));
                Float distance1 = location.distanceTo(location1);

                Location location2 = new Location("");
                location2.setLongitude(Double.parseDouble(rhs.getLongitude()));
                location2.setLatitude(Double.parseDouble(rhs.getLatitude()));
                Float distance2 = location.distanceTo(location2);

                return Math.round(distance1 - distance2);
            }
        });
    }

}