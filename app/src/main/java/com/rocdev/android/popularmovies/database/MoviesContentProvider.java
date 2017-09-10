package com.rocdev.android.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by piet on 09-09-17.
 *
 */

public class MoviesContentProvider extends ContentProvider {

    private MoviesDbHelper mDbHelper;
    private static final int MOVIES = 100;
    private static final int MOVIE_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String LOG_TAG = MoviesContentProvider.class.getSimpleName();

    static {
        //initialize UriMatcher
        sUriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        sUriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES
                + "/#", MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                cursor = db.query(MoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_ID:
                String[] selectionArguments = {uri.getLastPathSegment()};
                cursor = db.query(MoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Cannot query unknown URI: " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                long rowId = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, contentValues);
                Log.d(LOG_TAG, "" + rowId);
                if (getContext() != null) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return ContentUris.withAppendedId(uri, rowId);
            default:
                throw new UnsupportedOperationException("Cannot insert unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE_ID:
                String selection = MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " = ?";
                String[] selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                int rowsDeleted = db.delete(MoviesContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                if (rowsDeleted > 0 && getContext() != null) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new UnsupportedOperationException("Cannot delete unknown URI: " + uri);
        }
    }

    // not used
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
