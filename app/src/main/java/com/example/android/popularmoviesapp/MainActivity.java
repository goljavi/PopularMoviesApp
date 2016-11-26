package com.example.android.popularmoviesapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.key;
import static android.view.View.GONE;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {
    public static final String LOG_TAG = MainActivity.class.getName();
    private AppAdapter mAppAdapter;

    //Sort preference can be 0 or 1 so i initialize
    //mSortCriteria on a value that is not 0 nor 1
    private int mSortCriteria = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checks if there is an internet connection to begin with the data load
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            mAppAdapter = new AppAdapter(MainActivity.this, new ArrayList<Movie>());
            GridView listView = (GridView) findViewById(R.id.list);
            listView.setAdapter(mAppAdapter);

            //ListView Automatic Handles when there's nothing to display
            listView.setEmptyView(findViewById(R.id.emptyquery));

            //Open DetailActivity on item click
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);

                    //Serialise the Movie object to a JSON String for an easier handle of data
                    intent.putExtra("obj", mAppAdapter.getItem(position).jsonify());
                    startActivity(intent);

                }


            });

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(1, null, this);

        } else {
            //Else if there's no internet connection, display a textview that says it
            TextView textView = (TextView) findViewById(R.id.emptyquery);
            textView.setText(getResources().getString(R.string.no_internet));

            //Hide the progressBar if there's no internet connection is a good idea
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);
            progressBar.setVisibility(GONE);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        // Create a new loader
        return new AppLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int sortCriteria = Integer.parseInt(preferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default_value)));

        if(mSortCriteria != sortCriteria) {
            // Clear the adapter of previous data
            mAppAdapter.clear();

            // If there is valid data, then add it to the adapter's
            // data set. This will trigger the ListView to update.
            if (movies != null && !movies.isEmpty()) {
                mAppAdapter.addAll(movies);
            }

            mSortCriteria = sortCriteria;
        }

        //If there's no data, the ListView or Gridview controller
        //automatically manages to display this textview
        TextView textView = (TextView) findViewById(R.id.emptyquery);
        textView.setText(getResources().getString(R.string.not_found));

        //It's a good idea to hide the progressBar when nothing was found
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(GONE);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        // AppLoader reset, so we can clear out our existing data.
        mAppAdapter.clear();
    }

}



