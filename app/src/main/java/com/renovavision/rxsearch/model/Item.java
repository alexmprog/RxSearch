
package com.renovavision.rxsearch.model;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("kind")
    public String kind;

    @SerializedName("title")
    public String title;

    @SerializedName("htmlTitle")
    public String htmlTitle;

    @SerializedName("link")
    public String link;

    @SerializedName("displayLink")
    public String displayLink;

    @SerializedName("snippet")
    public String snippet;

    @SerializedName("htmlSnippet")
    public String htmlSnippet;

    @SerializedName("mime")
    public String mime;

    @SerializedName("image")
    public Image image;
}
