package com.rbc.yelp.services;

import com.rbc.yelp.services.models.SearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YelpApi {
    @GET("/v3/businesses/search")
    Call<SearchResult> search(@Query("term") String term, @Query("location") String location);
}
