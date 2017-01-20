package com.example.melanieh.movieapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by melanieh on 11/5/16.
 */

public class Movie {

    private String title;
    private String releaseDate;
    private String posterPathString;
    private String voteAverageString;
    private String plotSynopsis;
    private Integer movieId;
    private Integer runtime;
    private ArrayList<Video> videos;
    private ArrayList<Review> reviews;

    public Movie(Integer movieId, String plotSynopsis, String posterPathString, String releaseDate,
                 ArrayList<Review> reviews, Integer runtime, String title, ArrayList<Video> videos,
                 String voteAverageString) {
        this.movieId = movieId;
        this.plotSynopsis = plotSynopsis;
        this.posterPathString = posterPathString;
        this.releaseDate = releaseDate;
        this.reviews = reviews;
        this.runtime = runtime;
        this.title = title;
        this.videos = videos;
        this.voteAverageString = voteAverageString;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getPosterPathString() {
        return posterPathString;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public String getVoteAverageString() {
        return voteAverageString;
    }
}



