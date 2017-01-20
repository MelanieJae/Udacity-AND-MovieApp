package com.example.melanieh.movieapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.melanieh.movieapp.data.MovieContract.FavoriteEntry;

/**
 * Created by melanieh on 1/19/17.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;

    /** data type strings for SQL command strings */
    private static final String INT_PRIMARY_KEY_AUTOINC = " INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String INTEGER_NOT_NULL = " INTEGER NOT NULL";
    private static final String VARCHAR = " VARCHAR(50) NOT NULL DEFAULT 'Not Available'";
    private static final String DECIMAL_3_1 = " DECIMAL(3,1)";

    private static final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE" + FavoriteEntry.TABLE_NAME
            + "(" + FavoriteEntry.COLUMN_ID + INT_PRIMARY_KEY_AUTOINC + ","
            + FavoriteEntry.COLUMN_MOVIE_ID + INTEGER_NOT_NULL + ","
            + FavoriteEntry.COLUMN_TITLE + VARCHAR + ","
            + FavoriteEntry.COLUMN_POSTER_PATH_STRING + VARCHAR + ","
            + FavoriteEntry.COLUMN_PLOT_SYNOPSIS + VARCHAR + ","
            + FavoriteEntry.COLUMN_VOTE_AVERAGE + DECIMAL_3_1 + ","
            + FavoriteEntry.COLUMN_RELEASE_DATE + VARCHAR + ")";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //no new version anticipated
    }
}
