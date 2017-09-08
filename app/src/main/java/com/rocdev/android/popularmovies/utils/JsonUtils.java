package com.rocdev.android.popularmovies.utils;

import com.rocdev.android.popularmovies.models.Movie;
import com.rocdev.android.popularmovies.models.Review;
import com.rocdev.android.popularmovies.models.Trailer;

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
    private static final String KEY_ID = "id";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_TRAILER_KEY = "key";
    private static final String KEY_TRAILER_SITE = "site";
    private static final String KEY_TRAILER_TYPE = "type";
    private static final String KEY_TRAILER_NAME = "name";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";


    public static List<Movie> extractMoviesFromJson(String jsonString) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray results = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJSON = results.getJSONObject(i);
                int movieId = movieJSON.getInt(KEY_ID);
                String posterPath = movieJSON.getString(KEY_POSTER_PATH);
                String overview = movieJSON.getString(KEY_OVERVIEW);
                String releaseDate = movieJSON.getString(KEY_RELEASE_DATE);
                String originalTitle = movieJSON.getString(KEY_ORIGINAL_TITLE);
                double voteAverage = movieJSON.getDouble(KEY_VOTE_AVERAGE);
                movies.add(new Movie(
                        movieId,
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

    public static List<Trailer> getTrailersFromJson(final String jsonString) {
        List<Trailer> trailers = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray results = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject trailer = results.getJSONObject(i);
                String key = trailer.getString(KEY_TRAILER_KEY);
                String site = trailer.getString(KEY_TRAILER_SITE);
                String type = trailer.getString(KEY_TRAILER_TYPE);
                String name = trailer.getString(KEY_TRAILER_NAME);
                // For now only trailers and YouTube videos
                if (type.equals("Trailer") && site.equals("YouTube")) {
                    trailers.add(new Trailer(
                            key,
                            site,
                            type,
                            name)
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  trailers;
    }

    public static List<Review> getReviewsFromJson(final String jsonString) {
        List<Review> reviews = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray results = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject review = results.getJSONObject(i);
                String author = review.getString(KEY_AUTHOR);
                String content = review.getString(KEY_CONTENT);
                reviews.add(new Review(author, content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }

}
