package com.example.orost.redditclient.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * POJO represent Reddit children item JSON
 * Created by o.rostovtseva on 4/11/18.
 */

public class RedditNewsData {
    @SerializedName("author")
    public String author;

    @SerializedName("title")
    public String title;

    @SerializedName("num_comments")
    public int numComments;

    @SerializedName("created")
    public long created;

    @SerializedName("thumbnail")
    public String thumbnail;

    @SerializedName("url")
    public String url;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RedditNewsData that = (RedditNewsData) o;

        if (numComments != that.numComments) return false;
        if (created != that.created) return false;
        if (!author.equals(that.author)) return false;
        if (!title.equals(that.title)) return false;
        if (thumbnail != null ? !thumbnail.equals(that.thumbnail) : that.thumbnail != null)
            return false;
        return url != null ? url.equals(that.url) : that.url == null;
    }

    @Override
    public int hashCode() {
        int result = author.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + numComments;
        result = 31 * result + (int) (created ^ (created >>> 32));
        result = 31 * result + (thumbnail != null ? thumbnail.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
