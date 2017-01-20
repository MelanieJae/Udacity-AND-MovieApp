package com.example.melanieh.movieapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by melanieh on 11/12/16.
 */

public class SearchResultsFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Movie>>{

    GridView gridView;
    View emptyView;
    TextView emptyTextView;
    ImageView emptyImageView;
    ArrayList<Movie> movies;
    GridAdapter adapter;
    String TMDB_QUERY;
    String activityTitle;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;

    private static final String LOG_TAG = SearchResultsFragment.class.getName();

    public SearchResultsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "fragment onCreate called..");
        super.onCreate(savedInstanceState);

        activityTitle = getActivity().getIntent().getStringExtra("activity title");
        getActivity().setTitle(activityTitle);
        TMDB_QUERY = getActivity().getIntent().getStringExtra("queryUrl");
        setHasOptionsMenu(true);

        getLoaderManager().initLoader(1, null, this).forceLoad();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView called..");

        View view = inflater.from(getActivity()).inflate(R.layout.fragment_search_result, container, false);

        emptyView = view.findViewById(R.id.emptyview);
        emptyTextView = (TextView) view.findViewById(R.id.empty_view_text);
        emptyImageView = (ImageView) view.findViewById(R.id.empty_view_image);
        recyclerView = (RecyclerView) view.findViewById(R.id.grid_recyclerview);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        gridView.setEmptyView(emptyView);
        adapter = new GridAdapter(getActivity(), new ArrayList<Movie>());

        // checks internet connection first; if none, show
        // "no internet connection" message
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            gridView.setAdapter(adapter);

        } else {
            emptyView.setVisibility(View.VISIBLE);
            emptyImageView.setImageResource(R.drawable.ic_wifi_off_white_48dp);
            emptyTextView.setText(R.string.no_connection_error);

        }
            return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent openSettings = new Intent(getActivity(), SettingsActivity.class);
                startActivity(openSettings);
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
        }
        return true;
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "onCreateLoader called...");
        return new GridLoader(getActivity(), TMDB_QUERY);

    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        Log.v(LOG_TAG, "onLoadFinished called...");
        adapter.clear();

        if (movies != null) {
            adapter.addAll(movies);

        } else {
            emptyView.setVisibility(View.VISIBLE);
            emptyImageView.setImageResource(R.drawable.filmreel_white);
            emptyTextView.setText(R.string.empty_view_text);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        Log.v(LOG_TAG, "onLoaderReset called..");
        adapter.clear();
        getLoaderManager().restartLoader(1, null, this).forceLoad();
    }


}
