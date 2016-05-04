package com.renovavision.rxsearch.api;

import android.support.annotation.NonNull;

import com.renovavision.rxsearch.BuildConfig;
import com.renovavision.rxsearch.model.SearchResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class GoogleSearchApi {

    private static volatile GoogleSearchApi sInstance;

    public static GoogleSearchApi getInstance(){
        if (sInstance == null){
            synchronized (GoogleSearchApi.class){
                if (sInstance == null) {
                    sInstance = new GoogleSearchApi();
                }
            }
        }
        return sInstance;
    }

    private GoogleSearchService searchService;

    private GoogleSearchApi(){
        initServices();
    }

    private void initServices(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.GOOGLE_API_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        searchService = retrofit.create(GoogleSearchService.class);
    }

    public Observable<SearchResponse> getImages(@NonNull String text, int start, int count) {
        Map<String, String> params = new HashMap<>();

        // set search key
        params.put("key", BuildConfig.GOOGLE_CUSTOM_SEARCH_API_KEY);

        // set search engine id
        params.put("cx", BuildConfig.GOOGLE_CUSTOM_SEARCH_ENGINE_ID);

        params.put("searchType", "image");

        params.put("fileType", "jpg,png,gif");
        // http://blogoscoped.com/archive/2009-07-09-n80.html
        // free to use, share or modify, even commercially;
        params.put("rights", "(cc_publicdomain|cc_attribute|cc_sharealike).-(cc_noncommercial|cc_nonderived)");
        params.put("q", text);
        params.put("start", String.valueOf(Math.max(1, start)));
        params.put("num", String.valueOf(Math.max(1, count)));

        return searchService.getImages(params);
    }

}
