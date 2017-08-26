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
 */

public class NetworkUtils {

    private static final String BASE_URL_POPULAR = "http://api.themoviedb.org/3/movie/popular";
    private static final String BASE_URL_TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated";
    private static final String PARAM_NAME_API_KEY = "api_key";
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();




    // no instanciation
    private NetworkUtils() {
    }

    public static String getMoviesJson(int sortOrder) throws IOException {
        String baseUrl = "";
        if (sortOrder == MainActivity.POPULAR_MOVIES) {
            baseUrl = BASE_URL_POPULAR;
        } else if (sortOrder == MainActivity.TOP_RATED_MOVIES) {
            baseUrl = BASE_URL_TOP_RATED;
        }
        Uri builtUri = Uri.parse(baseUrl)
                .buildUpon()
                .appendQueryParameter(PARAM_NAME_API_KEY, ApiKey.getApiKey())
                .build();
        Log.i(LOG_TAG, builtUri.toString());
        URL url = new URL(builtUri.toString());

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }





}
