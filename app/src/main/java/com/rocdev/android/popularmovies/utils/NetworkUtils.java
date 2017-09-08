package com.rocdev.android.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import com.rocdev.android.popularmovies.MainActivity;
import com.rocdev.android.popularmovies.secret.ApiKey;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


/**
 * Created by piet on 25-08-17.
 *
 */

public class NetworkUtils {

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String PATH_POPULAR = "popular";
    private static final String PATH_TOP_RATED = "top_rated";
    private static final String PATH_VIDEOS = "/videos";
    private static final String PATH_REVIEWS = "/reviews";
    private static final String PARAM_NAME_API_KEY = "api_key";
    //TODO replace with your api-key
    private static final String API_KEY = ApiKey.getApiKey();

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();




    // no instanciation
    private NetworkUtils() {
    }

    public static String getMoviesJson(int sortOrder) throws IOException {
        String baseUrl = "";
        if (sortOrder == MainActivity.POPULAR_MOVIES) {
            baseUrl = BASE_URL + PATH_POPULAR;
        } else if (sortOrder == MainActivity.TOP_RATED_MOVIES) {
            baseUrl = BASE_URL + PATH_TOP_RATED;
        }
        return getNetworkResponse(baseUrl);
    }


    public static String getTrailersJson(int movieId) throws IOException {
        String baseUrl = BASE_URL + movieId + PATH_VIDEOS;
        return getNetworkResponse(baseUrl);
    }

    public static String getReviewsJson(int movieId) throws IOException {
        String baseUrl = BASE_URL + movieId + PATH_REVIEWS;

        return getNetworkResponse(baseUrl);
    }


    private static String getNetworkResponse(String baseUrl) throws IOException {
        Uri builtUri = Uri.parse(baseUrl)
                .buildUpon()
                .appendQueryParameter(PARAM_NAME_API_KEY, API_KEY)
                .build();
        Log.i(LOG_TAG, builtUri.toString());
        URL url = new URL(builtUri.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            String response = null;
            if (scanner.hasNext()) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }





}
