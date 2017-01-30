package com.example.melanieh.movieapp;

import android.view.View;

/**
 * Created by melanieh on 1/23/17.
 */

public interface RecyclerViewItemClickListener {
    public void onItemClick(Movie movie, boolean selectedState);
    // used by favorites cursor adapter, selected state is always true if it is in the favorites DB
    public void onItemClick(int movieId, boolean selectedState);
}
