package com.example.android.popularmoviesapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

public class AppLoader extends AsyncTaskLoader<List<Movie>> {

    //Define static variables for connecting to the API
    private static final String SCHEME = "https";
    private static final String AUTHORITY = "api.themoviedb.org";
    private static final String VERSION = "3";
    private static final String PATH1 = "movie";

    //This value is used for the SortBy Preference
    private static final int VALUE_POPULARITY = 0;

    //SortBy values for the URL
    private static final String POPULARITY = "popular";
    private static final String MOST_RATED = "top_rated";

    //API KEYS
    private static final String API_PARAMETER = "api_key";
    private static final String API_KEY = "c3c23e18d8525db148ccbc4eeef28941";

    public AppLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Movie> loadInBackground() {
        //Load the SortBy Preference
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int sortPref = Integer.parseInt(preferences.getString(getContext().getString(R.string.pref_sort_key), getContext().getString(R.string.pref_sort_default_value)));

        String sortby;

        if (sortPref == VALUE_POPULARITY) {
            sortby = POPULARITY;
        } else {
            sortby = MOST_RATED;
        }

        //Build the URI to connect to the API
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(VERSION)
                .appendPath(PATH1)
                .appendPath(sortby)
                .appendQueryParameter(API_PARAMETER, API_KEY);

        // Perform the network request, parse the response, and extract the JSON.
        List<Movie> movies = QueryUtils.fetchData(builder.build().toString());
        return movies;
    }
}
