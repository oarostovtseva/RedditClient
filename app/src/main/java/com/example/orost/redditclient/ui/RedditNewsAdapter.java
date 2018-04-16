package com.example.orost.redditclient.ui;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.orost.redditclient.R;
import com.example.orost.redditclient.common.NewsDiffCallback;
import com.example.orost.redditclient.common.Utils;
import com.example.orost.redditclient.rest.ApiManager;
import com.example.orost.redditclient.rest.model.RedditNewsData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Trivial Adapter for {@link RecyclerView}
 * Created by o.rostovtseva on 4/11/18.
 */

public class RedditNewsAdapter extends RecyclerView.Adapter<RedditNewsAdapter.NewsBaseVH> {

    private static final int VH_TYPE_ITEM = 0;
    private static final int VH_TYPE_PROGRESS = 1;

    private List<RedditNewsData> redditNewsList = new ArrayList<>();
    private ItemHelper itemHelper;

    @Override
    public NewsBaseVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VH_TYPE_ITEM:
                return new NewsVH(inflater.inflate(R.layout.item_news, parent, false));
            case VH_TYPE_PROGRESS:
                return new ProgressVH(inflater.inflate(R.layout.item_progress, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(NewsBaseVH holder, int position) {
        holder.onBindViewHolder(position);
    }

    @Override
    public int getItemCount() {
        return redditNewsList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position < redditNewsList.size() ? VH_TYPE_ITEM : VH_TYPE_PROGRESS;
    }

    /**
     * Update dataset and notify Recycler about changes
     */
    void setRedditNewsList(List<RedditNewsData> redditNewsData) {
        final NewsDiffCallback diffCallback = new NewsDiffCallback(this.redditNewsList, redditNewsData);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        redditNewsList.clear();
        redditNewsList.addAll(redditNewsData);
        diffResult.dispatchUpdatesTo(this);
    }

    void setItemHelper(ItemHelper ItemHelper) {
        this.itemHelper = ItemHelper;
    }

    public interface ItemHelper {
        void onThumbClick(View thumbView, String url, String title);
    }

    abstract class NewsBaseVH extends RecyclerView.ViewHolder {
        NewsBaseVH(View itemView) {
            super(itemView);
        }

        abstract void onBindViewHolder(int position);
    }

    class NewsVH extends NewsBaseVH {
        ImageView thumbView;
        TextView titleView;
        TextView timeAuthorView;
        TextView commentsView;

        public NewsVH(View itemView) {
            super(itemView);
            thumbView = itemView.findViewById(R.id.itemThumb);
            titleView = itemView.findViewById(R.id.itemTitle);
            timeAuthorView = itemView.findViewById(R.id.itemTimeAuthor);
            commentsView = itemView.findViewById(R.id.itemComments);
        }

        @Override
        void onBindViewHolder(int position) {
            RedditNewsData redditNewsItem = redditNewsList.get(position);
            Context context = itemView.getContext();

            String thumbUrl = redditNewsItem.thumbnail;
            String timeAuthorText = context.getString(R.string.time_author_text,
                    Utils.convertTimeCreated(redditNewsItem.created), redditNewsItem.author);
            String formattedNumber = context.getString(R.string.comments_text, redditNewsItem.numComments);

            Picasso.with(context)
                    .load(thumbUrl)
                    .error(R.drawable.ic_empty_picture)
                    .into(thumbView);
            titleView.setText(redditNewsItem.title);
            timeAuthorView.setText(formattedNumber);
            commentsView.setText(timeAuthorText);

            thumbView.setOnClickListener((v) -> itemHelper.onThumbClick(v, redditNewsItem.url, redditNewsItem.title));
            //Log.d(getClass().getName(), "Item was updated: " + position);
        }
    }

    class ProgressVH extends NewsBaseVH {
        ProgressBar progress;

        ProgressVH(View itemView) {
            super(itemView);
            progress = itemView.findViewById(R.id.progressBar);
        }

        @Override
        void onBindViewHolder(int position) {
            progress.setVisibility(redditNewsList.isEmpty() || redditNewsList.size() >= ApiManager.MAX_ITEMS ? View.GONE : View.VISIBLE);
        }
    }

}
