package com.example.melanieh.movieapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by melanieh on 12/3/16.
 */

public class GridLoader extends AsyncTaskLoader<List<Movie>> {

    String mUrlString;
    ArrayList<Movie> movies;
    Integer movieId;
    String idQueryURLString;
    String movieTitle;
    String releaseDate;
    String posterPathUrlString;
    String voteAverageString;
    String plotSynopsis;

    private static final String LOG_TAG = GridLoader.class.getName();

    public GridLoader(Context context, String urlString) {
        super(context);
        this.mUrlString = urlString;
    }

    @Override
    public List<Movie> loadInBackground() {
        String jsonResponse = "";
        String urlString = this.mUrlString;
        Log.v(LOG_TAG, "loadInBackground called..");

        // initialize JSON Response and url variables
        URL url = createURL(mUrlString);
        Log.v(LOG_TAG, "urlString= " + mUrlString);

        jsonResponse = buildJSONString(url);
        Log.v(LOG_TAG, "url= " + url);

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
        HttpsURLConnection connection = null;

        try {
            connection = (HttpsURLConnection) url.openConnection();
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
        Log.v(LOG_TAG, "extractJSONData:");

        movies = new ArrayList<Movie>();

        if (baseJsonResponse == null) {
            return null;
        }

        try{
            JSONObject baseJSONObject = new JSONObject(baseJsonResponse);
            JSONArray resultsArray = baseJSONObject.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultJSONObject = resultsArray.getJSONObject(i);

                movieId = resultJSONObject.getInt("id");

                // now that the id has been extracted the second query string (idQuery) to receive details,
                // video and review data can be constructed
                String BASE_URL = "https://api.themoviedb.org/3/movie";
                StringBuilder queryBuilder = new StringBuilder(BASE_URL);
                queryBuilder.append("/" + movieId);
                queryBuilder.append("?api_key=" + BuildConfig.TMDB_API_KEY).toString();
                idQueryURLString = queryBuilder.toString();

                Log.v(LOG_TAG, "idQuery= " + idQueryURLString);

                if (idQueryURLString == null) {
                    Log.e(LOG_TAG, "Movie idQuery invalid");
                }

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


                // movie poster path final segment to construct poster path url for Picasso
                String posterPathLastSegment = resultJSONObject.getString("poster_path");
                Log.v(LOG_TAG, "posterPathLastSegment= " + posterPathLastSegment);

                if (posterPathLastSegment != null) {
                    posterPathUrlString = "http://image.tmdb.org/t/p/w185" + posterPathLastSegment;
                    Log.v(LOG_TAG, "posterPathString= " + posterPathUrlString);
                }

                Movie movie = new Movie(movieId, null, posterPathUrlString, null, null, null, null, null, null);
                movies.add(movie);
            }
        } catch (JSONException e){
            Log.e(LOG_TAG, "", e);
        }
        return movies;
    }

}
