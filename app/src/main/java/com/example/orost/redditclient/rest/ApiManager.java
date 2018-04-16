package com.example.orost.redditclient.rest;

import com.example.orost.redditclient.rest.model.RedditNews;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Main utility class to get data via network through Reddit API
 * Created by o.rostovtseva on 4/11/18.
 */

public class ApiManager {

    public static final int MAX_ITEMS = 50;
    private static final int COUNT_PER_PAGE = 10;
    private static final String URL_API = "https://www.reddit.com";

    private RedditAPI api;

    public ApiManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_API)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(RedditAPI.class);
    }

    /**
     * Get RxJava Observale with Reddit children list
     * Items count controls by {@link ApiManager#COUNT_PER_PAGE}
     *
     * @param after@return
     */
    public Observable<RedditNews> getRedditNews(String after) {
        return api.getRedditNews(after, COUNT_PER_PAGE)
                .map(response -> response.body());
    }

}
