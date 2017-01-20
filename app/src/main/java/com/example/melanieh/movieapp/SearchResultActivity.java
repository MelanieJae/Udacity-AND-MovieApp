package com.example.melanieh.movieapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {

    GridView gridView;
    TextView emptyView;
    TextView instructionsView;
    ImageView tmdbLogoView;
    TextView tmdbStringView;
    ArrayList<Movie> movies;
    String TMDB_QUERY;
    String searchParamSegment;

    /*** log tag */
    private static final String LOG_TAG = SearchResultActivity.class.getName();

    private static final String BASE_URL = "https://api.themoviedb.org/3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "activity onCreate called...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        getFragmentManager().beginTransaction().add(R.id.container, new SearchResultsFragment()).commit();

       }

    }





