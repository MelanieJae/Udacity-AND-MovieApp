package com.example.melanieh.movieapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.example.melanieh.movieapp.data.MovieContract.FavoriteEntry;

/*** Created by melanieh on 1/19/17. */

public class FavoritesResultsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    MovieCursorAdapter movieCursorAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_results);
        final ListView favoritesList = (ListView)findViewById(R.id.favorites_listview);
        View emptyView = findViewById(R.id.favorites_list_emptyview);
        movieCursorAdapter = new MovieCursorAdapter(this, null);
        favoritesList.setAdapter(movieCursorAdapter);
        favoritesList.setEmptyView(emptyView);

        final ToggleButton favoriteToggleBtn = (ToggleButton)findViewById(R.id.favorite_toggle);
        // listener for toggle button click event and state change
        favoriteToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: handle state change upon click; default is off
            }
        });

        favoritesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent openMovieDetail = new Intent(FavoritesResultsActivity.this, DetailActivity.class);
                Uri currentFavoriteUri = ContentUris.withAppendedId(FavoriteEntry.CONTENT_URI, id);
                openMovieDetail.setData(currentFavoriteUri);
                startActivity(openMovieDetail);
            }
        });
    }

    /** favorite toggle button methods */
    private boolean isChecked(View view) {
        // default is false
        return false;
    }

    private void setChecked() {
        //TODO: finish logic
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        // cursor loader projection
        String[] projection = {FavoriteEntry.COLUMN_ID,
                FavoriteEntry.COLUMN_MOVIE_ID,
                FavoriteEntry.COLUMN_TITLE,
                FavoriteEntry.COLUMN_POSTER_PATH_STRING,
                FavoriteEntry.COLUMN_PLOT_SYNOPSIS,
                FavoriteEntry.COLUMN_VOTE_AVERAGE,
                FavoriteEntry.COLUMN_RELEASE_DATE,
        };
        Uri contentUri = FavoriteEntry.CONTENT_URI;
        return new CursorLoader(this, contentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        movieCursorAdapter.swapCursor(cursor);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieCursorAdapter.swapCursor(null);
    }
}
