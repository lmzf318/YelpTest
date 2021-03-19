package com.rbc.yelp.services.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Business {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("categories")
    private List<Category> categories;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
