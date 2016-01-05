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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
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

    public List<EstablishmentsPerType> getResults() {
        return results;
    }

    public void setResults(List<EstablishmentsPerType> results) {
        this.results = results;
    }

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
}


