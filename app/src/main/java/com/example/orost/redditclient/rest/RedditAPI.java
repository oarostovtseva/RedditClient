package com.example.orost.redditclient.rest;

import com.example.orost.redditclient.rest.model.RedditNews;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * API interface used by {@link retrofit2.Retrofit}
 * Should be used with Reddit REST Api
 * Created by o.rostovtseva on 4/11/18.
 */

public interface RedditAPI {

    @GET("/top.json")
    Observable<Response<RedditNews>> getRedditNews(
            @Query("after") String after,
            @Query("limit") int limit
    );

}
