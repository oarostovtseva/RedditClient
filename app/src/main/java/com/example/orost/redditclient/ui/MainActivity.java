package com.example.orost.redditclient.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.orost.redditclient.R;
import com.example.orost.redditclient.viewmodel.RedditNewsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements RedditNewsAdapter.ItemHelper {

    private static final String KEY_LIST_STATE = "state.list_position";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.newsItemsRecycler)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private RedditNewsAdapter newsAdapter;
    private RedditNewsViewModel newsViewModel;
    private Disposable errorHandler;
    private LinearLayoutManager layoutManager;
    private RedditScrollListener redditScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsViewModel = ViewModelProviders.of(this).get(RedditNewsViewModel.class);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        newsAdapter = new RedditNewsAdapter();
        newsAdapter.setItemHelper(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(5);
        recyclerView.setDrawingCacheEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(newsAdapter);
        redditScrollListener = new RedditScrollListener(() -> newsViewModel.loadNews(), layoutManager);
        recyclerView.addOnScrollListener(redditScrollListener);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable listState = savedInstanceState.getParcelable(KEY_LIST_STATE);
            if (listState != null) {
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        errorHandler = newsViewModel.subscribeErrorHandler(e -> notifyError(e.getMessage()));
        newsViewModel.getNewsData().observe(this, (data) -> {
            newsAdapter.setRedditNewsList(data);
            hideProgressbar();
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_LIST_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
    }


    @Override
    protected void onPause() {
        super.onPause();
        errorHandler.dispose();
    }

    @Override
    public void onThumbClick(View thumbView, String url, String title) {
        if (!url.isEmpty()) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.KEY_EXTRA_URL, url);
            intent.putExtra(DetailActivity.KEY_EXTRA_TITLE, title);
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this, thumbView, "image");
            startActivity(intent, options.toBundle());
        }
    }

    /**
     * Renew dataset by executing new network request
     */
    @OnClick(R.id.fab)
    public void forceLoadRedditNews() {
        newsViewModel.clearData();
        redditScrollListener.clear();
        showProgressbar();
        newsViewModel.loadNews();
    }

    /**
     * Display progressbar
     */
    private void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hide progressbar
     */
    private void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Show error message as {@link Snackbar}
     */
    private void notifyError(String message) {
        Log.e(getClass().getName(), message);
        hideProgressbar();
        Snackbar.make(toolbar, message, Snackbar.LENGTH_LONG).show();
    }
}
