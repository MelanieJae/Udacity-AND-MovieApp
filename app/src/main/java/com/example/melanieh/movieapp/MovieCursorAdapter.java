package com.example.melanieh.movieapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by melanieh on 1/19/17.
 */

public class MovieCursorAdapter extends CursorAdapter {

    public MovieCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.favorites_recyclerview_item_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO: finish when DB is established
    }
}
