package com.example.melanieh.movieapp;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.melanieh.movieapp.data.MovieContract;

/**
 * Created by melanieh on 1/19/17.
 */

public class CursorRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    Cursor cursor;

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorites_recyclerview_item_row, parent, false);
        return new RecyclerView.ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Cursor cursorRow = cursor.get(position);
        holder.bindCursorRow(cursorRowId);
    }


    /**
     * Created by melanieh on 1/19/17.
     */

    public static class VH extends RecyclerView.ViewHolder implements View.OnClickListener{
        Context context = itemView.getContext();

        public VH(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent openMovieDetail = new Intent(context, DetailActivity.class);
            Uri currentFavoriteUri = ContentUris.withAppendedId(MovieContract.FavoriteEntry.CONTENT_URI, id);
            openMovieDetail.setData(currentFavoriteUri);
            context.startActivity(openMovieDetail);
        }
    }
}
