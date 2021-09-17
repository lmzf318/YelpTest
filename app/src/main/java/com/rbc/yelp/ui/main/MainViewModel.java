package com.rbc.yelp.ui.main;

import com.rbc.yelp.services.YelpRepository;
import com.rbc.yelp.services.models.Business;
import com.rbc.yelp.services.models.SearchResult;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private MutableLiveData<SearchResult> resultMutableLiveData;
    private YelpRepository yelpRepository;

    public LiveData<SearchResult> get(String term, String location) {
        return resultMutableLiveData = yelpRepository.getSearchResult(term, location);
    }

    public MainViewModel(){
        super();
        yelpRepository = YelpRepository.getInstance();
    }

}