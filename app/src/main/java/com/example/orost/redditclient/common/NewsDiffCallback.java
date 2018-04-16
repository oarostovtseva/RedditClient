package com.example.orost.redditclient.common;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.orost.redditclient.rest.model.RedditNewsData;

import java.util.List;

/**
 * Diff util callback for calculating differences between two lists
 * Created by o.rostovtseva on 16.04.2018.
 */
public class NewsDiffCallback extends DiffUtil.Callback {

    private final List<RedditNewsData> oldRedditNewsDataList;
    private final List<RedditNewsData> newRedditNewsDataList;

    public NewsDiffCallback(List<RedditNewsData> oldRedditNewsDataList, List<RedditNewsData> newRedditNewsDataList) {
        this.oldRedditNewsDataList = oldRedditNewsDataList;
        this.newRedditNewsDataList = newRedditNewsDataList;
    }

    @Override
    public int getOldListSize() {
        return oldRedditNewsDataList.size();
    }

    @Override
    public int getNewListSize() {
        return newRedditNewsDataList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldRedditNewsDataList.get(oldItemPosition).hashCode() == newRedditNewsDataList.get(
                newItemPosition).hashCode();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final RedditNewsData oldRedditNewsData = oldRedditNewsDataList.get(oldItemPosition);
        final RedditNewsData newRedditNewsData = newRedditNewsDataList.get(newItemPosition);

        return oldRedditNewsData.equals(newRedditNewsData);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
