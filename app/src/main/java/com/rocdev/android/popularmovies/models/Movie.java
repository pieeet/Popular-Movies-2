package com.rocdev.android.popularmovies.models;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by piet on 25-08-17.
 *
 */

public class Movie implements Parcelable {

    private final int movieId;
    private final String originalTitle;
    private final String posterPath;
    private final String overView;
    private final double voteAverage;
    private final String releaseDate;

    public Movie(
            int movieId,
            String originalTitle,
            String posterPath,
            String overView,
            double voteAverage,
            String releaseDate) {
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.overView = overView;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverView() {
        return overView;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }


    protected Movie(Parcel in) {
        movieId = in.readInt();
        originalTitle = in.readString();
        posterPath = in.readString();
        overView = in.readString();
        voteAverage = in.readDouble();
        releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(overView);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}