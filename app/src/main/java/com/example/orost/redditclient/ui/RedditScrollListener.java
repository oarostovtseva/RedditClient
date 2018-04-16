package com.example.orost.redditclient.ui;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Scroll listener for pagination support when scrolling to the bottom
 * Created by o.rostovtseva on 4/16/18.
 */

public class RedditScrollListener extends RecyclerView.OnScrollListener {
    private Runnable loadDataRunnable;
    private LinearLayoutManager layoutManager;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 2;
    private int firstVisibleItem = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;

    public RedditScrollListener(Runnable loadDataRunnable, LinearLayoutManager layoutManager) {
        this.loadDataRunnable = loadDataRunnable;
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy > 0) {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = layoutManager.getItemCount();
            firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount)
                    <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached
                Log.d(getClass().getName(), "End reached");
                loadDataRunnable.run();
                loading = true;
            }
        }
    }

    public void clear() {
        previousTotal = 0;
    }
}
