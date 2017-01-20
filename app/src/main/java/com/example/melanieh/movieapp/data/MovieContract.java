package com.example.melanieh.movieapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by melanieh on 1/19/17.
 */

public class MovieContract {

    /** log tag */
    private static final String LOG_TAG = MovieContract.class.getSimpleName();

    private MovieContract() {
        // left blank to prevent instantiation
    }

    public static final String CONTENT_AUTHORITY = "com.example.melanieh.movieapp";

    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static class FavoriteEntry implements BaseColumns {

        public static final String TABLE_NAME = "favorites";

        /** content URI */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH_MOVIES);

        /*** The MIME type of the {@link #CONTENT_URI} for a list of movies. */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        /*** The MIME type of the {@link #CONTENT_URI} for a single product. */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        /** table column names */
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_MOVIE_ID = "MovieId";
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_POSTER_PATH_STRING = "PosterUrlPathString";
        public static final String COLUMN_PLOT_SYNOPSIS = "PlotSynopsis";
        public static final String COLUMN_VOTE_AVERAGE = "VoteAverage";
        public static final String COLUMN_RELEASE_DATE = "ReleaseDate";
    }

    @Override
    public String toString() {
        return "ProductContract content URIs {" +
                "BASE_URI" + BASE_URI.toString() +
                "PRODUCTS_CONTENT_URI" + FavoriteEntry.CONTENT_URI.toString() + "}";
    }
}
