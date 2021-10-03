package com.rbc.yelp.services;

import com.rbc.yelp.services.models.SearchResult;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YelpRepository {
    private static YelpRepository yelpRepository;
    private static YelpApi yelpApi;
    MutableLiveData<SearchResult> data = new MutableLiveData<>();

    public YelpRepository() {
        yelpApi = YelpService.createService(YelpApi.class);
    }

    public static YelpRepository getInstance() {
        if (yelpRepository == null) {
            yelpRepository = new YelpRepository();
        }
        return yelpRepository;
    }

    public void getSearchResult(String term, String location) {
        yelpApi.search(term, location).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                data.setValue(new SearchResult());
            }
        });
    }

    public MutableLiveData<SearchResult> getLiveData() {
        return data;
    }
}
