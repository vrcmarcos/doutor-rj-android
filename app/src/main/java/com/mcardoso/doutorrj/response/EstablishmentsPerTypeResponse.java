package com.mcardoso.doutorrj.response;

import com.mcardoso.doutorrj.model.establishment.EstablishmentsPerType;

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

}