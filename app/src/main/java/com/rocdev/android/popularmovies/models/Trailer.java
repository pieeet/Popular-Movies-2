package com.rocdev.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by piet on 02-09-17.
 *
 */

public class Trailer implements Parcelable {

    private String key;
    private String site;
    private String type;
    private String name;


    public Trailer(String key, String site, String type, String name) {
        this.key = key;
        this.site = site;
        this.type = type;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getYouTubeUrl() {
        return "https://www.youtube.com/watch?v=" + key;
    }

    protected Trailer(Parcel in) {
        key = in.readString();
        site = in.readString();
        type = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(site);
        dest.writeString(type);
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}