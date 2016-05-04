package com.renovavision.rxsearch.api;

import com.renovavision.rxsearch.model.SearchResponse;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface GoogleSearchService {

    @GET("/customsearch/v1")
    Observable<SearchResponse> getImages(@QueryMap Map<String, String> params);
}
