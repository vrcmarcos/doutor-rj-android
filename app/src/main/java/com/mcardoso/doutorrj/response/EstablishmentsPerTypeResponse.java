package com.mcardoso.doutorrj.response;

import com.google.gson.annotations.SerializedName;
import com.mcardoso.doutorrj.model.establishment.Establishment;
import com.mcardoso.doutorrj.model.establishment.EstablishmentType;

import java.util.List;

/**
 * Created by mcardoso on 12/23/15.
 */
public class EstablishmentsPerTypeResponse {

    private int count;
    private String next;
    private String previous;
    private List<EstablishmentsPerType> results;

    public List<EstablishmentsPerType> getResults() {
        return results;
    }

    public class EstablishmentsPerType {
        @SerializedName("nome")
        private EstablishmentType type;

        @SerializedName("estabelecimentos")
        List<Establishment> establishments;

        public EstablishmentType getType() {
            return type;
        }

        public List<Establishment> getEstablishments() {
            return establishments;
        }
    }
}