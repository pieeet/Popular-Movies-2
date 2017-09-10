package com.rocdev.android.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by piet on 09-09-17.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "popularmovies.db";
    private static final int DATABASE_VERSION = 1;

    MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesContract.MovieEntry.TABLE_NAME + " ("
                + MoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + MoviesContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "
                + MoviesContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, "
                + MoviesContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, "
                + MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL, "
                + MoviesContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
