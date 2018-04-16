package com.example.orost.redditclient.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by o.rostovtseva on 14.04.2018.
 */

public class RedditNews {
    @SerializedName("data")
    public RedditData data;
}
