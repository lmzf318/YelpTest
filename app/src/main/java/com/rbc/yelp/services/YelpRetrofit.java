package com.rbc.yelp.services;

import androidx.annotation.NonNull;

import com.rbc.yelp.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YelpRetrofit {

    public Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new ApiKeyInterceptor())
                        .build())
                .baseUrl("https://api.yelp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static class ApiKeyInterceptor implements Interceptor {

        @Override
        public @NonNull Response intercept(Chain chain) throws IOException {
            return chain.proceed(chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer " + BuildConfig.API_KEY)
                    .build());
        }
    }
}
