package com.example.melanieh.movieapp;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by melanieh on 1/24/17.
 */

public class MovieDetailRecyclerAdapter extends RecyclerView.Adapter<MovieDetailRecyclerAdapter.DetailViewHolder> {

    ArrayList<Movie> movies;
    RecyclerViewItemClickListener listener;
    private static final String LOG_TAG = MovieDetailRecyclerAdapter.class.getSimpleName();
    TextView releaseYear;
    TextView runtime;
    TextView voteAverage;
    TextView addFavoriteText;
    TextView plotSynopsis;
    RecyclerView detailRecyclerView;
    RecyclerView videoRecyclerView;
    RecyclerView reviewRecyclerView;
    static boolean toggleSelectState;

    public MovieDetailRecyclerAdapter(ArrayList<Movie> movies, boolean toggleSelectState) {
        Log.v(LOG_TAG, "movies= {" + movies + "}");
        this.movies = movies;
        this.toggleSelectState = toggleSelectState;
    }

    @Override
    public int getItemCount() {
//        Log.v(LOG_TAG, "getItemCount: movies.size=" + movies.size());
        if (movies != null) {
            return movies.size();
        } else {
            return 0;
        }
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v(LOG_TAG, "onCreateViewHolder:");
        View inflatedView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.detail_recycler_view, parent, false);
        return new DetailViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {
        Log.v(LOG_TAG, "onBindViewHolder:");
        Movie itemMovie = movies.get(position);
        holder.bind(itemMovie);
    }

    /***
     * Created by melanieh on 1/24/17.
     */

    public static class DetailViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView moviePosterView;
        TextView runtimeView;
        TextView voteAverageView;
        ImageButton favoriteToggleBtn;
        TextView plotSynopsisView;
        TextView releaseYearView;
        Context context;
        int movieId;
        String posterPathString;
        String releaseDate;
        String plotSynopsis;
        String voteAvgString;
        String movieTitle;
        RecyclerView videoRecyclerView;
        RecyclerView reviewRecyclerView;
        LinearLayoutManager videoLLManager;
        LinearLayoutManager reviewLLManager;

        public DetailViewHolder(View itemView) {
            super(itemView);
            Log.v(LOG_TAG, "DetailViewHolder constructor:");

//           // introduce UI elements
            moviePosterView = (ImageView) itemView.findViewById(R.id.movie_poster);
            releaseYearView = (TextView) itemView.findViewById(R.id.release_year);
            runtimeView = (TextView) itemView.findViewById(R.id.runtime);
            voteAverageView = (TextView) itemView.findViewById(R.id.vote_average);
            favoriteToggleBtn = (ImageButton) itemView.findViewById(R.id.favorite_toggle);
            plotSynopsisView = (TextView) itemView.findViewById(R.id.plot_synopsis);
            videoRecyclerView = (RecyclerView) itemView.findViewById(R.id.videos_list_recyclerview);
            reviewRecyclerView = (RecyclerView) itemView.findViewById(R.id.reviews_list_recyclerview);
        }

        public void bind(final Movie movie) {
            Log.v(LOG_TAG, "bind: movie= " + movie);

            context = itemView.getContext();
//            movieId = movie.getMovieId();
            Log.v(LOG_TAG, "bind: movieId=" + movieId);
            movieTitle = movie.getTitle();
            posterPathString = movie.getPosterPathString();
            releaseDate = movie.getReleaseDate();
            plotSynopsis = movie.getPlotSynopsis();
            voteAvgString = movie.getVoteAverageString();

            videoLLManager = new LinearLayoutManager(context);
            videoRecyclerView.setLayoutManager(videoLLManager);

            VideoRecyclerAdapter videoRecyclerAdapter =
                    new VideoRecyclerAdapter(movie.getVideos());
            videoRecyclerView.setAdapter(videoRecyclerAdapter);
            reviewLLManager = new LinearLayoutManager(context);
            reviewRecyclerView.setLayoutManager(reviewLLManager);
            ReviewRecyclerAdapter reviewRecyclerAdapter =
                    new ReviewRecyclerAdapter(movie.getReviews());
            reviewRecyclerView.setAdapter(reviewRecyclerAdapter);

            videoRecyclerView.setNestedScrollingEnabled(false);
            reviewRecyclerView.setNestedScrollingEnabled(false);

            // null checks
            if (posterPathString == null) {
                Log.i(LOG_TAG, "No poster available");
            }

            if (releaseDate == null) {
                releaseYearView.setText("Release date not available");
            }

            if (plotSynopsis == null) {
                plotSynopsisView.setText("Plot Synopsis not available");
            }

            if (voteAvgString == null) {
                voteAverageView.setText("Rating not available");
            }

            if (favoriteToggleBtn.isSelected()) {
                favoriteToggleBtn.setImageResource(R.drawable.ic_star_black_48dp);
            } else {
                favoriteToggleBtn.setImageResource(R.drawable.ic_star_border_black_48dp);
            }

            // set content of UI elements
            Picasso.with(context)
                    .load(movie.getPosterPathString()).resize(150, 150).into(moviePosterView);

            releaseYearView.setText(movie.getReleaseDate().substring(0, 4));
            runtimeView.setText(movie.getRuntime().toString() + "mins");
            voteAverageView.setText(movie.getVoteAverageString());
            Log.v(LOG_TAG, "plot synopsis= " + movie.getPlotSynopsis());
            plotSynopsisView.setText(movie.getPlotSynopsis());
            favoriteToggleBtn.setSelected(toggleSelectState);
            if (favoriteToggleBtn.isSelected()) {
                favoriteToggleBtn.setImageResource(R.drawable.ic_star_black_48dp);
            } else {
                favoriteToggleBtn.setImageResource(R.drawable.ic_star_border_black_48dp);
            }
            favoriteToggleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // default selected state is false
                    if (favoriteToggleBtn.isSelected()) {
                        favoriteToggleBtn.setSelected(false);
                    }

                    favoriteToggleBtn.setSelected(true);
                }
            });
        }

        @Override
        public void onClick(View view) {
            // implemented in bind method above
        }
    }
}
