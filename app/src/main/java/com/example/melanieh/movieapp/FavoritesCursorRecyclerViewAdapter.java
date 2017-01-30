package com.example.melanieh.movieapp;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.melanieh.movieapp.data.MovieContract;
import com.example.melanieh.movieapp.data.MovieDBHelper;
import com.squareup.picasso.Picasso;

/**
 * Created by melanieh on 1/19/17.
 */

public class FavoritesCursorRecyclerViewAdapter
        extends RecyclerView.Adapter<FavoritesCursorRecyclerViewAdapter.FavoriteViewHolder> {

    Context context;
    protected Cursor cursor;
    boolean mDataValid;
    int cursorRowIdColumn;
    DataSetObserver dataSetObserver;
    static Uri favoriteUri;

    String[] projection = {MovieContract.FavoriteEntry.COLUMN_ID,
            MovieContract.FavoriteEntry.COLUMN_MOVIE_ID, MovieContract.FavoriteEntry.COLUMN_POSTER_PATH_STRING
    };

    public FavoritesCursorRecyclerViewAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        mDataValid = cursor != null;
        cursorRowIdColumn = mDataValid ? cursor.getColumnIndex("_id") : -1;
        NotifyingDataSetObserver dataSetObserver = new NotifyingDataSetObserver();
        if (cursor != null) {
            cursor.registerDataSetObserver(dataSetObserver);
        }
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorites_grid_recyclerview_item, parent, false);
        return new FavoriteViewHolder(inflatedView);
    }

    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public int getItemCount() {
        if (mDataValid && cursor != null) {
            return cursor.getCount();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder viewHolder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException(context.getString(R.string.is_exception_cursor_invalid));
        }
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException(context.getString(R.string.is_exception_cannot_move_cursor + position));
        }

        // read cursor row; convert to movie item, then pass movie as itemView to bind method
        int rowId = cursor.getInt(cursor.getColumnIndexOrThrow(MovieContract.FavoriteEntry.COLUMN_ID));
        favoriteUri = ContentUris.withAppendedId(MovieContract.FavoriteEntry.CONTENT_URI, rowId);
        Cursor itemView = context.getContentResolver().query(favoriteUri, projection, null, null, null);

        viewHolder.bind(itemView);
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == cursor) {
            return null;
        }
        final Cursor oldCursor = cursor;
        if (oldCursor != null && dataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(dataSetObserver);
        }
        cursor = newCursor;
        if (cursor != null) {
            if (dataSetObserver != null) {
                cursor.registerDataSetObserver(dataSetObserver);
            }
            cursorRowIdColumn = newCursor.getColumnIndexOrThrow(MovieContract.FavoriteEntry.COLUMN_ID);
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            cursorRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    /**** Created by melanieh on 1/19/17 */

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private static final String LOG_TAG = FavoriteViewHolder.class.getSimpleName();
        ImageButton favoriteToggleBtn;
        ImageView favoritePosterView;
        int movieId;
        Context context = itemView.getContext();

        public FavoriteViewHolder(View itemView) {
            // itemview in this case is cursor row item
            super(itemView);
            favoritePosterView = (ImageView) itemView.findViewById(R.id.poster_image);
            favoriteToggleBtn = (ImageButton) itemView.findViewById(R.id.favorite_toggle);
        }

        public void bind(final Cursor cursor) {
            while (cursor.moveToNext()) {
                int movieIdColIndex = cursor.getColumnIndexOrThrow
                        (MovieContract.FavoriteEntry.COLUMN_MOVIE_ID);
                movieId = cursor.getInt(movieIdColIndex);
                int postercolIndex = cursor.getColumnIndexOrThrow
                        (MovieContract.FavoriteEntry.COLUMN_POSTER_PATH_STRING);
                String posterPathString = cursor.getString(postercolIndex);
                favoritePosterView.setOnClickListener(this);
                favoriteToggleBtn.setOnClickListener(this);
                favoriteToggleBtn.setSelected(true);
                if (favoriteToggleBtn.isSelected()) {
                    favoriteToggleBtn.setImageResource(R.drawable.ic_star_black_48dp);
                } else {
                    favoriteToggleBtn.setImageResource(R.drawable.ic_star_border_black_48dp);
                }

                Picasso.with(itemView.getContext())
                        .load(posterPathString).resize(250, 350).into(favoritePosterView);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.poster_image:
                    Intent openDetail = new Intent(context, DetailActivity.class);
                    openDetail.putExtra("movieID", movieId);
                    openDetail.putExtra("toggleSelectState", favoriteToggleBtn.isSelected());
                    context.startActivity(openDetail);
                    break;
                case R.id.favorite_toggle:
                    confirmRemoveFavoriteDialog(context);
            }
        }
    }

    private static void confirmRemoveFavoriteDialog(final Context context) {
        AlertDialog.Builder deleteConfADBuilder = new AlertDialog.Builder(context);
        deleteConfADBuilder.setMessage(context.getString(R.string.favorite_deleteConf_dialog_msg));

        // clicklisteners for buttons

        // positive button=yes, delete favorite
        DialogInterface.OnClickListener yesButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeFromFavorites(context, favoriteUri);
            }
        };

        String yesString = context.getString(R.string.delete_dialog_yes);
        String noString = context.getString(R.string.delete_dialog_no);
        deleteConfADBuilder.setPositiveButton(yesString, yesButtonListener);
        deleteConfADBuilder.setNegativeButton(noString, null);

        deleteConfADBuilder.create();
        deleteConfADBuilder.show();
    }

    private static void removeFromFavorites(Context context, Uri uri) {
        int rowsDeleted = context.getContentResolver().delete(uri, null, null);
        if (rowsDeleted == 0) {
            Toast.makeText(context, context.getString(R.string.error_deleting_favorite),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.favorite_deletion_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**** Created by melanieh on 1/26/17 */

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
        }
    }
}


