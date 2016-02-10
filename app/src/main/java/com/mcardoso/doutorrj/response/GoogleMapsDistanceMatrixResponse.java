package com.mcardoso.doutorrj.response;

import com.google.gson.annotations.SerializedName;
import com.mcardoso.doutorrj.model.location.Row;

import java.util.List;

/**
 * Created by mcardoso on 1/3/16.
 */
public class GoogleMapsDistanceMatrixResponse {

    @SerializedName("destination_addresses")
    private List<String> destinationAddress;

    @SerializedName("origin_addresses")
    private List<String> originAddress;

    private List<Row> rows;

    private String status;

    public List<String> getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(List<String> destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public List<String> getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(List<String> originAddress) {
        this.originAddress = originAddress;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
