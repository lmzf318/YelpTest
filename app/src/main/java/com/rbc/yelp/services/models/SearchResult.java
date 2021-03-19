package com.rbc.yelp.services.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {
    @SerializedName("total")
    private int total;
    @SerializedName("businesses")
    private List<Business> businesses;

    public int getTotal() {
        return total;
    }

    public List<Business> getBusinesses() {
        return businesses;
    }
}
