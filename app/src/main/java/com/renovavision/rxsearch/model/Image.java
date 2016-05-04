package com.renovavision.rxsearch.model;

import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("contextLink")
    public String contextLink;

    @SerializedName("height")
    public int height;

    @SerializedName("width")
    public int width;

    @SerializedName("byteSize")
    public int byteSize;

    @SerializedName("thumbnailLink")
    public String thumbnailLink;

    @SerializedName("thumbnailHeight")
    public int thumbnailHeight;

    @SerializedName("thumbnailWidth")
    public int thumbnailWidth;
}
