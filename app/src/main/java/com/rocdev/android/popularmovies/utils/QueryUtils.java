package com.rocdev.android.popularmovies.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.rocdev.android.popularmovies.R;
import com.rocdev.android.popularmovies.database.MoviesContract;
import com.rocdev.android.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piet on 09-09-17.
 *
 */

public class QueryUtils {


    public static long insertMovie(Movie movie, Context context) {
        ContentValues cv = new ContentValues();
        cv.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
        cv.put(MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getOriginalTitle());
        cv.put(MoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverView());
        cv.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        cv.put(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        cv.put(MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        Uri uri = context.getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, cv);
        long id = ContentUris.parseId(uri);
        if (id < 1) {
            Toast.makeText(context, R.string.save_movie_error, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.save_movie_success, Toast.LENGTH_SHORT).show();
        }
        return id;
    }

    public static int deleteMovie(Movie movie, Context context) {
        Uri uri = Uri.withAppendedPath(MoviesContract.MovieEntry.CONTENT_URI,
                String.valueOf(movie.getMovieId()));
        int deletedRows = context.getContentResolver().delete(uri, null, null);
        if (deletedRows < 1) {
            Toast.makeText(context, R.string.delete_movie_error, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.delete_movie_success, Toast.LENGTH_SHORT).show();
        }
        return deletedRows;
    }


    public static List<Movie> getSavedMovies(Context context) {
        Cursor cursor = context.getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,
                null, null, null, null);
        return makeListFromCursor(cursor);
    }

    private static ArrayList<Movie> makeListFromCursor(Cursor cursor) {
        ArrayList<Movie> movies = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int movieId = cursor.getInt(cursor.getColumnIndex(MoviesContract
                        .MovieEntry.COLUMN_MOVIE_ID));
                String title = cursor.getString(cursor.getColumnIndex(MoviesContract
                        .MovieEntry.COLUMN_MOVIE_TITLE));
                String posterPath = cursor.getString(cursor.getColumnIndex(MoviesContract
                        .MovieEntry.COLUMN_MOVIE_POSTER_PATH));
                String overview = cursor.getString(cursor.getColumnIndex(MoviesContract
                        .MovieEntry.COLUMN_MOVIE_OVERVIEW));
                double voteAverage = cursor.getDouble(cursor.getColumnIndex(MoviesContract
                        .MovieEntry.COLUMN_VOTE_AVERAGE));
                String releaseDate = cursor.getString(cursor.getColumnIndex(MoviesContract
                        .MovieEntry.COLUMN_RELEASE_DATE));
                movies.add(new Movie(movieId, title, posterPath, overview, voteAverage, releaseDate));
            }
            cursor.close();
        }
        return movies;
    }

    public static boolean isSaved(int movieId, Context context) {
        Uri uri = Uri.withAppendedPath(MoviesContract.MovieEntry.CONTENT_URI, String.valueOf(movieId));
        Cursor cursor = context.getContentResolver().query(uri, null, null, null,null);
        boolean isSaved = false;
        if (cursor != null && cursor.moveToFirst()) {
            isSaved = true;
        }
        if (cursor != null) {
            cursor.close();
        }
        return isSaved;
    }


}
