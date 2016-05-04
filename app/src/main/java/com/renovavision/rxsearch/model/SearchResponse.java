
package com.renovavision.rxsearch.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse {

    @SerializedName("kind")
    public String kind;

    @SerializedName("items")
    public List<Item> items = new ArrayList<>();
}
