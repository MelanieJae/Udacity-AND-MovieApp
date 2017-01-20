package com.example.melanieh.movieapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import android.view.View;
import com.example.melanieh.movieapp.data.MovieContract.FavoriteEntry;
import com.example.melanieh.movieapp.R;

/**
 * Created by melanieh on 1/19/17.
 */

public class MovieProvider extends ContentProvider {

    public static final String favoritesTable = FavoriteEntry.TABLE_NAME;
    // This cursor will hold the result of the query
    Cursor cursor;

    /** {@link MovieProvider} */

    /** Tag for the log messages */
    public final String LOG_TAG = MovieProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the favorites table */
    private static final int FAVORITES = 100;

    /** URI matcher code for a single favorite's content URI */
    private static final int FAVORITE_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, FAVORITES);

        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "/#", FAVORITE_ID);
    }

    SQLiteDatabase db;
    /** Database helper object */
    private MovieDBHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        db = mDbHelper.getReadableDatabase();

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                cursor = db.query(FavoriteEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FAVORITE_ID:
                selection = FavoriteEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                // Cursor containing that row of the table.
                cursor = db.query(FavoriteEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                // updating for the favorites table is only supported for the entire table so the selection
                // and selectionArgs values are set to null here
                return saveFavorite(uri, contentValues, null, null);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /*** helper method for updating the favorites  */
    private Uri saveFavorite(Uri uri, ContentValues contentValues, String selection, String selectionArgs) {
        db = mDbHelper.getWritableDatabase();

        long newRowId = db.insert(FavoriteEntry.TABLE_NAME, null, contentValues);

//        if (newRowId == -1) {
//
////            Toast.makeText(getContext(), getContext().getString(R.string.error_inserting_favorite),
////                    Toast.LENGTH_SHORT).show();
////        } else {
////            Toast.makeText(getContext(), getContext().getString(R.string.insert_favorite_successful),
////                    Toast.LENGTH_SHORT).show();
////        }
//            getContext().getContentResolver().notifyChange(uri, null);
//        }

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(FavoriteEntry.CONTENT_URI, newRowId);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        // prevents calls to this method for individual favorites;
        // favorites table supports only updates to the entire table, i.e. by either insertion of a new
        // favorite (handled by insert method here) or deletion of existing favorite(s) (handled by
        // delete method here)
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        db = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                // Delete all rows that match the selection and selection args
                deleteFavorites();
            case FAVORITE_ID:
                // Delete a single row given by the ID in the URI
                selection = FavoriteEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return db.delete(FavoriteEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    private void deleteFavorites() {
        db = mDbHelper.getWritableDatabase();
        int numRowsDeleted = db.delete(favoritesTable, null, null);

        if (numRowsDeleted == 0) {
            Toast.makeText(getContext(), getContext().getString(R.string.error_deleting_favorite),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.favorite_deletion_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                return FavoriteEntry.CONTENT_LIST_TYPE;
            case FAVORITE_ID:
                return FavoriteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
