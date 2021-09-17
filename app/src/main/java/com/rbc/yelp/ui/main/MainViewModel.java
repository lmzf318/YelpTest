package com.rbc.yelp.ui.main;

import com.rbc.yelp.services.models.Business;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private LiveData<List<Business>> businessList;

//    protected LiveData<List<Business>> getBusinessList(){
//        if (businessList == null){
//
//        }
//    }
}