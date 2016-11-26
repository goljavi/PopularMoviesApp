package com.example.android.popularmoviesapp;

import android.net.Uri;

import com.google.gson.Gson;

/**
 * Movie object
 *
 * @mPosterPath URL to the movie image path
 * @mReleaseDate Release date of the movie in yyyy-MM-dd format
 * @mOverview Description or plot synopsis of the movie
 * @mTitle Movie title
 * @mVoteAverage Number average from 0 to 10 of qualifications
 */

public class Movie {
    private String mPosterPath;
    private String mReleaseDate;
    private String mOverview;
    private String mTitle;
    private long mVoteAverage;

    public Movie(String posterPath, String releaseDate, String overview, String title, long voteAverage) {
        mPosterPath = posterPath;
        mReleaseDate = releaseDate;
        mOverview = overview;
        mTitle = title;
        mVoteAverage = voteAverage;
    }

    /**
     * @return URL to the poster image of the movie object
     */
    public String getPosterPath() {
        final String SCHEME = "https";
        final String AUTHORITY = "image.tmdb.org";
        final String PATH1 = "t";
        final String PATH2 = "p";
        final String PATH3 = "w300_and_h450_bestv2";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(PATH1)
                .appendPath(PATH2)
                .appendPath(PATH3);
        return builder.build().toString() + mPosterPath;
    }

    /**
     * @return JSON String to serialize the object and transfer it easily between activities
     */
    public String jsonify() {
        return new Gson().toJson(new Movie(mPosterPath, mReleaseDate, mOverview, mTitle, mVoteAverage));
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getTitle() {
        return mTitle;
    }

    public long getVoteAverage() {
        return mVoteAverage;
    }
}
