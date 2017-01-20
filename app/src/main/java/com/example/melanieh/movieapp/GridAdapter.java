package com.example.melanieh.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melanieh on 12/3/16.
 */

public class GridAdapter extends ArrayAdapter {

    private ArrayList<Movie> movies;
    ImageView imageView;
    TextView textView;
    String titleUI;
    String releaseDateUI;
    String voteAverageUI;
    String plotSynopsisUI;
    String runtimeUI;
    String videoUI;
    String reviewsUI;

    /**
     * log tag
     */
    private static final String LOG_TAG = GridAdapter.class.getName();

    public GridAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Movie currentMovie = (Movie) getItem(position);
        View gridView = convertView;
        if (gridView == null) {
            gridView = LayoutInflater.from(getContext()).inflate(R.layout.grid_recyclerview_item, parent, false);

        }

        //title view
        ImageView imageView = (ImageView) gridView.findViewById(R.id.poster_image);
        gridView.setLayoutParams(new GridView.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (currentMovie.getPosterPathString() == null) {
            imageView.setImageResource(R.drawable.ic_image_broken_black_48dp);
        } else {
            String posterUrl = currentMovie.getPosterPathString();
            Picasso.with(getContext())
                    .load(posterUrl).resize(550,650).into(imageView);
        }

        if (currentMovie.getTitle() == null) {
            titleUI = getContext().getString(R.string.no_title);
        } else {
            titleUI = currentMovie.getTitle();
        }

        if (currentMovie.getReleaseDate() == null) {
            releaseDateUI = getContext().getString(R.string.no_release_date);
        } else {
            releaseDateUI = currentMovie.getReleaseDate();
        }

        if (currentMovie.getRuntime() == null) {
            runtimeUI = getContext().getString(R.string.no_runtime);
        } else {
            runtimeUI = currentMovie.getRuntime().toString();
        }

        if (currentMovie.getVoteAverageString() == null) {
            voteAverageUI = getContext().getString(R.string.no_rating);
        } else {
            voteAverageUI = currentMovie.getVoteAverageString();
        }

        if (currentMovie.getPlotSynopsis() == null) {
            plotSynopsisUI = getContext().getString(R.string.no_plot_synopsis);
        } else {
            plotSynopsisUI = currentMovie.getPlotSynopsis();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent openDetailActivity = new Intent(getContext(), DetailActivity.class);
            openDetailActivity.putExtra("movieID", currentMovie.getMovieId());
            getContext().startActivity(openDetailActivity);
            }
        });

        return gridView;
    }
}


