package com.rocdev.android.popularmovies.utils;

import com.rocdev.android.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piet on 26-08-17.
 *
 */

public class JsonUtils {

    private static final String KEY_RESULTS = "results";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_ID = "id";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_VOTE_AVERAGE = "vote_average";


    public static List<Movie> extractMoviesFromJson(String jsonString) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray results = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJSON = results.getJSONObject(i);
                String posterPath = movieJSON.getString(KEY_POSTER_PATH);
                String overview = movieJSON.getString(KEY_OVERVIEW);
                String releaseDate = movieJSON.getString(KEY_RELEASE_DATE);
                int id = movieJSON.getInt(KEY_ID);
                String originalTitle = movieJSON.getString(KEY_ORIGINAL_TITLE);
                double voteAverage = movieJSON.getDouble(KEY_VOTE_AVERAGE);
                movies.add(new Movie(
                        originalTitle,
                        posterPath,
                        overview,
                        voteAverage,
                        releaseDate)
                );
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

}
