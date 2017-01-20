package com.example.melanieh.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*** Created by melanieh on 1/19/17. */

public class PosterGridRecyclerAdapter extends RecyclerView.Adapter<PosterGridRecyclerAdapter.PosterGridHolder> {

    private ArrayList<Movie> movies;
    ImageView posterView;
    String posterPathString;

    public PosterGridRecyclerAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public PosterGridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_recyclerview_item, parent, false);
        return new PosterGridHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(PosterGridHolder holder, int position) {
        posterPathString = movies.get(position).getPosterPathString();
        holder.bindPoster(posterPathString);    }


    /*** Created by melanieh on 1/19/17. */

    public static class PosterGridHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private static final String LOG_TAG=PosterGridHolder.class.getSimpleName();
        Context context = itemView.getContext();
        ImageView posterView;

        public PosterGridHolder(View itemView) {
            super(itemView);
            posterView = (ImageView) itemView.findViewById(R.id.poster_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.v(LOG_TAG, "PosterGridHolder: onClick");
            Intent openDetailActivity = new Intent(context, DetailActivity.class);
            openDetailActivity.putExtra("movieID", context.getMovieId());
            itemView.getContext().startActivity(openDetailActivity);
        }

        public void bindPoster(String posterPathString) {
            Picasso.with(context)
                    .load(posterPathString).resize(550,650).into(posterView);
        }
    }
}
