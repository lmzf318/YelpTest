package com.rbc.yelp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rbc.yelp.services.YelpApi;
import com.rbc.yelp.services.YelpRetrofit;
import com.rbc.yelp.services.models.SearchResult;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class YelpTest {

    @Test
    public void test_yelp_api() throws IOException {
        Call<SearchResult> resultCall = new YelpRetrofit()
                .getRetrofitInstance()
                .create(YelpApi.class)
                .search("Sushi", "Toronto");
        Response<SearchResult> res = resultCall.execute();
        assertThat(res.code(), is(200));
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(res.body()));
    }
}