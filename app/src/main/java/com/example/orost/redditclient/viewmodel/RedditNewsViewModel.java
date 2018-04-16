package com.example.orost.redditclient.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.orost.redditclient.RedditNewsApp;
import com.example.orost.redditclient.rest.ApiManager;
import com.example.orost.redditclient.rest.model.RedditNewsData;

import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * This ViewModel class cares about storing list of children separate from Activity lifecycle.
 * Used {@link LiveData} to easy handling changes.
 * If error occurred Activity will be notified through {@link PublishSubject} if subscribed correctly
 * Created by o.rostovtseva on 4/11/18.
 */

public class RedditNewsViewModel extends ViewModel {

    private String newsLastLoadedPage;
    private MutableLiveData<List<RedditNewsData>> newsData = new MutableLiveData<>();
    private PublishSubject<Throwable> exceptionWatcher = PublishSubject.create();

    public RedditNewsViewModel() {
        newsData.setValue(Collections.emptyList());
    }

    /**
     * Returns LiveData for children list.
     * If children list is empty perform loading request
     */
    public LiveData<List<RedditNewsData>> getNewsData() {
        if (isEmptyData()) {
            loadNews();
        }
        return newsData;
    }

    /**
     * Subscribe observer listen for errors.
     * Returns {@link Disposable}
     */
    public Disposable subscribeErrorHandler(@NonNull io.reactivex.functions.Consumer<Throwable> consumer) {
        return exceptionWatcher.subscribe(consumer);
    }

    /**
     * Get all news and handle
     */
    public void loadNews() {
        if (isEnoughData()) {
            return;
        }
        RedditNewsApp.getInstance()
                .getApiManager()
                .getRedditNews(newsLastLoadedPage)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(result -> newsLastLoadedPage = result.data.after)
                .observeOn(Schedulers.io())
                .map(result -> result.data)
                .flatMapIterable(result -> result.children)
                .map(child -> child.data)
                .startWith(newsData.getValue())
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDataLoaded, e -> exceptionWatcher.onNext(e));
    }

    public void clearData() {
        newsLastLoadedPage = "";
        newsData.setValue(Collections.emptyList());
    }

    private void onDataLoaded(List<RedditNewsData> redditNews) {
        newsData.setValue(redditNews);
        Log.d(getClass().getName(), "List size: " + redditNews.size());
    }

    /**
     * Check is any children data is already stored
     */
    private boolean isEmptyData() {
        List<?> collection = newsData.getValue();
        return collection == null || collection.isEmpty();
    }

    /**
     * Check if we want to load more data to show
     */
    private boolean isEnoughData() {
        List<?> collection = newsData.getValue();
        return collection != null && collection.size() >= ApiManager.MAX_ITEMS;
    }
}
