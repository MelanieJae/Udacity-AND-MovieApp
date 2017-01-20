package com.example.melanieh.movieapp;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import com.example.melanieh.movieapp.data.MovieContract.FavoriteEntry;
import com.example.melanieh.movieapp.data.MovieDBHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by melanieh on 1/17/17.
 */

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie>{

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    String runtime;
    String idQueryURLString;
    Integer movieId;
    ArrayList<Video> videos;
    ArrayList<Review> reviews;
    SQLiteDatabase db;
    Uri favoriteUri;
    TextView releaseYearView;
    TextView runtimeView;
    TextView voteAverageView;
    TextView plotSynopsisView;
    ImageView posterView;
    ListView videosListView;
    ListView reviewsListView;
    String releaseYear;
    private RecyclerView videoRecyclerView;
    private RecyclerView reviewRecyclerView;
    private LinearLayoutManager linearLayoutManager;

    // loader ID
    private static final int MOVIE_LOADER_ID = 2;

    // cursor loader projection
    String[] projection = {FavoriteEntry.COLUMN_ID,
            FavoriteEntry.COLUMN_MOVIE_ID,
            FavoriteEntry.COLUMN_TITLE,
            FavoriteEntry.COLUMN_POSTER_PATH_STRING,
            FavoriteEntry.COLUMN_PLOT_SYNOPSIS,
            FavoriteEntry.COLUMN_VOTE_AVERAGE,
            FavoriteEntry.COLUMN_RELEASE_DATE,
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(LOG_TAG, "DetailFragment onCreate:");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        videoRecyclerView = (RecyclerView) findViewById(R.id.videos_list_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        videoRecyclerView.setLayoutManager(linearLayoutManager);

        reviewRecyclerView = (RecyclerView) findViewById(R.id.reviews_list_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        reviewRecyclerView.setLayoutManager(linearLayoutManager);

        videoRecyclerView.setNestedScrollingEnabled(false);
        reviewRecyclerView.setNestedScrollingEnabled(false);

        // Set a toolbar to replace the action bar.
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        Intent incoming = getIntent();
        movieId = incoming.getIntExtra("movieID", -1);
        favoriteUri = incoming.getData();
        // now that the id has been extracted the second query string (idQuery) to receive details,
        // video and review data can be constructed
        StringBuilder queryBuilder = new StringBuilder(MainActivity.BASE_URL);
        queryBuilder.append("/" + movieId + "?");
        queryBuilder.append("&api-key=" + BuildConfig.TMDB_API_KEY);
        queryBuilder.append("&append_to_response=videos,reviews");
        idQueryURLString = queryBuilder.toString();
        Log.v(LOG_TAG, "idQueryString= " + idQueryURLString);

        if (idQueryURLString == null) {
            Log.e(LOG_TAG, "Movie idQuery invalid");
        }

        View videosEmptyView = findViewById(R.id.video_list_emptyview);
        View reviewsEmptyView = findViewById(R.id.review_list_emptyview);

        videos = new ArrayList<Video>();
        reviews = new ArrayList<Review>();

        videosListView = (ListView) findViewById(R.id.videos_listview);
        reviewsListView = (ListView) findViewById(R.id.reviews_listview);

        videosListView.setEmptyView(videosEmptyView);
        reviewsListView.setEmptyView(reviewsEmptyView);

        releaseYearView = (TextView) findViewById(R.id.release_year);
        runtimeView = (TextView) findViewById(R.id.runtime);
        voteAverageView = (TextView) findViewById(R.id.vote_average);
        plotSynopsisView = (TextView) findViewById(R.id.plot_synopsis);
        posterView = (ImageView) findViewById(R.id.movie_poster);

        if (favoriteUri == null) {
            getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            displayMovieDetails(favoriteUri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent openSettings = new Intent(this, SettingsActivity.class);
                startActivity(openSettings);
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
    }

    private void displayMovieDetails(Uri uri) {
        MovieDBHelper dbHelper = new MovieDBHelper(this);
        db = dbHelper.getReadableDatabase();
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        int movieIdIndex = cursor.getColumnIndexOrThrow(FavoriteEntry.COLUMN_MOVIE_ID);
        int movieId = cursor.getInt(movieIdIndex);
        int titleIndex = cursor.getColumnIndexOrThrow(FavoriteEntry.COLUMN_TITLE);
        String title = cursor.getString(titleIndex);
        int ppsIndex = cursor.getColumnIndexOrThrow(FavoriteEntry.COLUMN_POSTER_PATH_STRING);
        String posterPathString = cursor.getString(ppsIndex);
        int plotSynopsisIndex = cursor.getColumnIndexOrThrow(FavoriteEntry.COLUMN_PLOT_SYNOPSIS);
        String plotSynopsis = cursor.getString(plotSynopsisIndex);
        int voteAverageIndex = cursor.getColumnIndexOrThrow(FavoriteEntry.COLUMN_MOVIE_ID);
        Double voteAverage = cursor.getDouble(voteAverageIndex);
        int releaseDateIndex = cursor.getColumnIndexOrThrow(FavoriteEntry.COLUMN_RELEASE_DATE);
        String releaseDate = cursor.getString(releaseDateIndex);

        releaseYear = releaseDate.substring(0,4);
        releaseYearView.setText(releaseYear);
        runtimeView.setText(runtime);
        voteAverageView.setText(voteAverage.toString());
        plotSynopsisView.setText(plotSynopsis);
        Picasso.with(this)
                .load(posterPathString).resize(250,350).into(posterView);
    }


    private void addToFavorites(Movie movie) {
        MovieDBHelper dbHelper = new MovieDBHelper(this);
        db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteEntry.COLUMN_MOVIE_ID, movie.getMovieId());
        contentValues.put(FavoriteEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(FavoriteEntry.COLUMN_POSTER_PATH_STRING, movie.getPosterPathString());
        contentValues.put(FavoriteEntry.COLUMN_PLOT_SYNOPSIS, movie.getPlotSynopsis());
        contentValues.put(FavoriteEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverageString());

        Uri favoriteUri = getContentResolver().insert(FavoriteEntry.CONTENT_URI, contentValues);
        Intent goToFavoritesList = new Intent(this, FavoritesResultsActivity.class);
        goToFavoritesList.setData(favoriteUri);
        startActivity(goToFavoritesList);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        return new MovieLoader(this, idQueryURLString);
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, final Movie movie) {
        ToggleButton addFavoriteToggleBtn = (ToggleButton) findViewById(R.id.favorite_toggle);
        addFavoriteToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToFavorites(movie);
            }
        });

        releaseYearView.setText(movie.getReleaseDate().substring(0,4));
        runtimeView.setText(movie.getRuntime());
        voteAverageView.setText(movie.getVoteAverageString());
        plotSynopsisView.setText(movie.getPlotSynopsis());
        Picasso.with(this)
                .load(movie.getPosterPathString()).resize(250,350).into(posterView);
        VideoAdapter videoAdapter = new VideoAdapter(this, movie.getVideos());
        videosListView.setAdapter(videoAdapter);
        ReviewAdapter reviewAdapter = new ReviewAdapter(this, reviews);
        reviewsListView.setAdapter(reviewAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {
        getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }
}
