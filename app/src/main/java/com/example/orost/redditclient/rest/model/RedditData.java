package com.example.orost.redditclient.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * POJO represent Reddit children JSON
 * Created by o.rostovtseva on 4/11/18.
 */

public class RedditData {
    @SerializedName("after")
    public String after;

    @SerializedName("before")
    public String before;

    @SerializedName("children")
    public List<RedditChildren> children;
}
