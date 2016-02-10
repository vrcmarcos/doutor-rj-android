package com.mcardoso.doutorrj.model.establishment;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mcardoso on 1/7/16.
 */
public class EstablishmentsPerType {

    @SerializedName("nome")
    private EstablishmentType type;

    @SerializedName("estabelecimentos")
    List<Establishment> establishments;

    public EstablishmentType getType() {
        return type;
    }

    public void setType(EstablishmentType type) {
        this.type = type;
    }

    public List<Establishment> getEstablishments() {
        return establishments;
    }

    public void setEstablishments(List<Establishment> establishments) {
        this.establishments = establishments;
    }
}
