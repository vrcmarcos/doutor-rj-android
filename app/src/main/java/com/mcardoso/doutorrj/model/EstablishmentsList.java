package com.mcardoso.doutorrj.model;

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

}