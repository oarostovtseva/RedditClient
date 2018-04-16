package com.example.orost.redditclient;

import android.app.Application;

import com.example.orost.redditclient.rest.ApiManager;

/**
 * Application is a robust Singletone in Android.
 * In this case used to store and provide instance of {@link ApiManager}
 * Created by o.rostovtseva on 4/11/18.
 */

public class RedditNewsApp extends Application {
    private static RedditNewsApp redditNewsApp;
    private ApiManager apiManager;

    /**
     * Get instance of {@link RedditNewsApp}
     */
    public static RedditNewsApp getInstance() {
        return redditNewsApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        redditNewsApp = this;
        apiManager = new ApiManager();
    }

    /**
     * Get instance of {@link ApiManager}
     */
    public ApiManager getApiManager() {
        return apiManager;
    }
}
