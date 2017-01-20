package com.example.melanieh.movieapp;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by melanieh on 11/6/16.
 */


public class MovieLoader extends AsyncTaskLoader<ArrayList<Movie>>{

    ArrayList<Movie> movies;
    String movieTitle;
    String releaseDate;
    String voteAverageString;
    String plotSynopsis;
    String mUrlString;
    String posterPathUrlString;
    Integer movieId;
    Integer runtime;
    String videoTitle;
    String videoUrlString;
    String reviewAuthor;
    String reviewText;
    String idQueryURLString;
    Context context;

    /** log tag */
    private static final String LOG_TAG = MovieLoader.class.getName();

    public MovieLoader(Context context, String idQueryURLString) {
        super(context);
        Log.v(LOG_TAG, "MovieLoader constructor");
        this.idQueryURLString = idQueryURLString;
    }

    @Override
    public ArrayList<Movie> loadInBackground() {
        Log.v(LOG_TAG, "MovieLoader: loadInBackground");
        String jsonResponse = "";
        if (idQueryURLString == null) {
            Log.e(LOG_TAG, "Movie idQuery invalid");
        }

        // initialize JSON Response and url variables
        URL url = createURL(idQueryURLString);
        Log.v(LOG_TAG, "idQueryURLString= " + idQueryURLString);
        // now that the id has been extracted the second query string (idQuery) to receive details,
        // video and review data can be constructed

        jsonResponse = buildJSONString(url);
        Log.v(LOG_TAG, "url= " + url);

        Log.v(LOG_TAG, "jsonResponse= " + jsonResponse);

        ArrayList<Movie> movies = extractMoviesJSONData(jsonResponse);

        return movies;

    }

    private URL createURL(String urlString) {
        URL url = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "", e);
        }

        return url;
    }

    private String buildJSONString (URL url) {
        Log.v(LOG_TAG, "buildJsonString called...");
        String jsonResponse = "";
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                jsonResponse = convertInputStream(inputStream);

            }
            Log.v(LOG_TAG, "response code= " + connection.getResponseCode());

            if (connection != null) {
                connection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "", e);
        }

        return jsonResponse;
    }

    private String convertInputStream(InputStream inputStream) throws IOException {

            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader
                        (inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

    // for videos, extract: results [{name, key}, {name, key}, etc...]

    // for reviews, extract: results object{ author, content}

    private ArrayList<Movie> extractMoviesJSONData(String baseJsonResponse) {
        Log.v(LOG_TAG, "extractJSONData called...");

        movies = new ArrayList<Movie>();
        ArrayList<Video> videos = new ArrayList<Video>();
        ArrayList<Review> reviews = new ArrayList<Review>();

        try{
            JSONObject baseJSONObject = new JSONObject(baseJsonResponse);
            JSONArray resultsArray = baseJSONObject.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultJSONObject = resultsArray.getJSONObject(i);

                movieId = resultJSONObject.getInt("id");

                // movie title
                movieTitle = resultJSONObject.getString("title");
                if (resultJSONObject.has("title") == false) {
                    movieTitle = "Title Not Available";
                }

                // release date
                releaseDate = resultJSONObject.getString("release_date");
                if (resultJSONObject.has("release_date") == false) {
                    releaseDate = "Release Date Not Available";
                }

                // movie poster path final segment to construct poster path url for Picasso
                String posterPathLastSegment = resultJSONObject.getString("poster_path");
                Log.v(LOG_TAG, "posterPathLastSegment= " + posterPathLastSegment);

                if (posterPathLastSegment != null) {
                    posterPathUrlString = "http://image.tmdb.org/t/p/w185" + posterPathLastSegment;
                    Log.v(LOG_TAG, "posterPathString= " + posterPathUrlString);
                }

                // rating
                Double voteAverage = resultJSONObject.getDouble("vote_average");
                if (resultJSONObject.has("vote_average") == false) {
                    voteAverageString = "No voter average available";
                } else {
                    voteAverageString = "" + voteAverage;
                }
                Log.v(LOG_TAG, voteAverageString);

                // plot synopsis
                plotSynopsis = resultJSONObject.getString("overview");
                if (resultJSONObject.has("overview") == false) {
                    plotSynopsis = ("Plot synopsis Not Available");
                }

                // videos
                JSONObject videosJSONObject = baseJSONObject.getJSONObject("videos");
                JSONArray videoResultsJSONArray = videosJSONObject.getJSONArray("results");
                if (resultJSONObject.has("videos") == false) {
                    videoResultsJSONArray = null;
                } else {
                    for (i = 0; i < videoResultsJSONArray.length(); i++) {
                        JSONObject videoResultJSONObject = videoResultsJSONArray.getJSONObject(i);
                        videoTitle = videoResultJSONObject.getString("name");
                        String videoYTKey = videoResultJSONObject.getString("key");

                        if (videoYTKey != null) {
                            videoUrlString = "http://www.youtube.com/" + videoYTKey;
                            Log.v(LOG_TAG, "videoUrlString= " + videoUrlString);
                        }

                        Video video = new Video(videoTitle, videoUrlString);
                        videos.add(video);
                    }
                }

                // reviews
                JSONObject reviewsJSONObject = baseJSONObject.getJSONObject("reviews");
                JSONArray resultsJSONArray = reviewsJSONObject.getJSONArray("results");
                if (resultJSONObject.has("reviews") == false) {
                    resultsJSONArray = null;
                } else {
                    for (i = 0; i < resultsJSONArray.length(); i++) {
                        JSONObject reviewJSONObject = resultsJSONArray.getJSONObject(i);
                        reviewAuthor = reviewJSONObject.getString("author");
                        reviewText = reviewJSONObject.getString("content");

                        Review review = new Review(reviewAuthor, reviewText);
                        reviews.add(review);
                    }

                }

                Movie movie = new Movie(movieId, plotSynopsis, posterPathUrlString, releaseDate, reviews,
                        runtime, movieTitle, videos, voteAverageString);
                Log.v(LOG_TAG, "movie=" + movie);
                movies.add(movie);
            }
        } catch (JSONException e){
            Log.e(LOG_TAG, "", e);
        }
        return movies;
    }

}



