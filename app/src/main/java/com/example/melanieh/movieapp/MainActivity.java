package com.example.melanieh.movieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;
import com.facebook.stetho.Stetho;

/**
 * Created by melanieh on 11/16/16.
 */

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    public static final String BASE_URL = "https://api.themoviedb.org/3/movie";

    String TMDB_QUERY;
    String activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_most_popular:
                viewMostPopular();
                break;
            case R.id.search_top_rated:
               viewTopRated();
                break;
            case R.id.action_settings:
                Intent openSettings = new Intent(this, SettingsActivity.class);
                startActivity(openSettings);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /*** displays the movies whose vote average is at or above the user's minimum rating choice in the
     * settings **/
    private void viewTopRated() {
        // build query string
        StringBuilder queryBuilder = new StringBuilder(BASE_URL);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String newMinRating = sharedPref.getString("minRating", getString(R.string.pref_min_rating_default));

        queryBuilder.append("/top_rated?");
        queryBuilder.append("include_adult=false");
        queryBuilder.append("&api_key=" + BuildConfig.TMDB_API_KEY);
        queryBuilder.append("&vote_average.gte=" + newMinRating);
        // for videos and reviews
        Log.v(LOG_TAG, newMinRating);

        getSearchResults(queryBuilder);

    }

    /*** displays the movies listed returned by the 'popular' query tag from the TMDB API**/
    private void viewMostPopular() {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String newMinRating = sharedPref.getString("minRating", getString(R.string.pref_min_rating_default));

        // build query string
        StringBuilder queryBuilder = new StringBuilder(BASE_URL);

        queryBuilder.append("/popular?");
        queryBuilder.append("include_adult=false");
        queryBuilder.append("&api_key=" + BuildConfig.TMDB_API_KEY);
        queryBuilder.append("&sort_by=popularity.desc");
        queryBuilder.append("&vote_average.gte=" + newMinRating);
        // for videos and reviews
        Log.v(LOG_TAG, newMinRating);

        getSearchResults(queryBuilder);

    }

    public void getSearchResults(StringBuilder queryStringBuilder) {

        if (queryStringBuilder.toString().contains("popular")) {
            activityTitle = getString(R.string.appbar_most_popular);
        } else {
            activityTitle = getString(R.string.appbar_top_rated);

        }
        Log.v(LOG_TAG, "activityTitle=" + activityTitle);

        TMDB_QUERY = queryStringBuilder.toString();
        Log.v(LOG_TAG, "TMDB_QUERY=" + TMDB_QUERY);
        Intent searchResults = new Intent(getApplicationContext(), SearchResultActivity.class);
        searchResults.putExtra("activity title", activityTitle);
        searchResults.putExtra("queryUrl", TMDB_QUERY);
        startActivity(searchResults);

    }

}
