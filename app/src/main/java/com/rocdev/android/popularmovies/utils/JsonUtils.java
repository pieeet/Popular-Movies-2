package com.rocdev.android.popularmovies.utils;

import com.rocdev.android.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piet on 26-08-17.
 */

public class JsonUtils {


    private static final String KEY_RESULTS = "results";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_ADULT = "adult";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_GENRE_IDS = "genre_ids";
    private static final String KEY_ID = "id";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_ORIGINAL_LANGUAGE = "original_language";
    private static final String KEY_TITLE = "title";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_POPULARITY = "popularity";
    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_VIDEO = "video";
    private static final String KEY_VOTE_AVERAGE = "vote_average";


    public static List<Movie> extractMoviesFromJson(String jsonString) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray results = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJSON = results.getJSONObject(i);
                String posterPath = movieJSON.getString(KEY_POSTER_PATH);
//                boolean adult = movieJSON.getBoolean(KEY_ADULT);
                String overview = movieJSON.getString(KEY_OVERVIEW);
                String releaseDate = movieJSON.getString(KEY_RELEASE_DATE);
                JSONArray genreIdsJSONArray = movieJSON.getJSONArray(KEY_GENRE_IDS);
//                List<Integer> genreIds = new ArrayList<>();
//                for (int j = 0; j < genreIdsJSONArray.length(); j++) {
//                    Integer genreId = genreIdsJSONArray.getInt(i);
//                    genreIds.add(genreId);
//                }

                int id = movieJSON.getInt(KEY_ID);
                String originalTitle = movieJSON.getString(KEY_ORIGINAL_TITLE);
//                String originalLanguage = jsonObject.getString(KEY_ORIGINAL_LANGUAGE);
//                String title = jsonObject.getString(KEY_TITLE);
//                String backdropPath = jsonObject.getString(KEY_BACKDROP_PATH);
//                double popularity = jsonObject.getDouble(KEY_POPULARITY);
//                int voteCount = jsonObject.getInt(KEY_VOTE_COUNT);
//                boolean video = jsonObject.getBoolean(KEY_VIDEO);
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
