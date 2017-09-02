package com.rocdev.android.popularmovies;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rocdev.android.popularmovies.databinding.ActivityDetailBinding;
import com.rocdev.android.popularmovies.models.Movie;
import com.rocdev.android.popularmovies.models.Trailer;
import com.rocdev.android.popularmovies.utils.JsonUtils;
import com.rocdev.android.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Trailer>> {

    public static final String BASE_URL_POSTER = "http://image.tmdb.org/t/p";
    // you can request different img width in tmdb. Uncomment the one needed
//    private static final String WIDTH_POSTER_W92 = "/w92";
//    private static final String WIDTH_POSTER_W154 = "/w154";
    public static final String WIDTH_POSTER_W185 = "/w185";
//    private static final String WIDTH_POSTER_W342 = "/w342";
//    private static final String WIDTH_POSTER_W500 = "/w500";
//    private static final String WIDTH_POSTER_W780 = "/w780";
//    private static final String WIDTH_POSTER_ORIG = "/original";

    private static final int TRAILERS_LOADER_ID = 4343;

    private Movie mMovie;
    /*
     * From Udacity course:
     * This field is used for data binding. Normally, we would have to call findViewById many
     * times to get references to the Views in this Activity. With data binding however, we only
     * need to call DataBindingUtil.setContentView and pass in a Context and a layout, as we do
     * in onCreate of this class. Then, we can access all of the Views in our layout
     * programmatically without cluttering up the code with findViewById.
     */
    private ActivityDetailBinding mDetailBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        mMovie = getIntent().getParcelableExtra(getString(R.string.key_intent_movie));
        initNavigation();
        bindMovieData();
        getLoaderManager().initLoader(TRAILERS_LOADER_ID, null, this);

    }

    private void initNavigation() {
        setSupportActionBar(mDetailBinding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindMovieData() {
        mDetailBinding.movieDetailContent.tvTitle.setText(mMovie.getOriginalTitle());
        mDetailBinding.movieDetailContent.tvSynopsis.setText(mMovie.getOverView());
        mDetailBinding.movieDetailContent.tvYear.setText(mMovie.getReleaseDate());
        mDetailBinding.movieDetailContent.tvVoteAverage.setText(String.format("%s%s%s",
                getString(R.string.avg_vote_prefix)," ", mMovie.getVoteAverage()));
        String url = BASE_URL_POSTER + WIDTH_POSTER_W185 + mMovie.getPosterPath();
        Picasso.with(this).load(url).into(mDetailBinding.movieDetailContent.ivPoster);
    }


    @Override
    public Loader<List<Trailer>> onCreateLoader(int i, Bundle bundle) {

        return new AsyncTaskLoader<List<Trailer>>(this) {
            ArrayList<Trailer> trailers;

            @Override
            protected void onStartLoading() {
                if (trailers == null) {
                    forceLoad();
                } else {
                    deliverResult(trailers);
                }
            }

            @Override
            public List<Trailer> loadInBackground() {
                try {
                    String trailersJsonString = NetworkUtils.getTrailersJson(mMovie.getMovieId());
                    return JsonUtils.getTrailersFromJson(trailersJsonString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void deliverResult(List<Trailer> data) {
                trailers = (ArrayList<Trailer>) data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> trailers) {
        for (Trailer trailer: trailers) {
            LayoutInflater inflater = LayoutInflater.from(this);
            @SuppressLint("InflateParams")
            View item = inflater.inflate(R.layout.trailer_item, null);
            TextView trailerTitle = item.findViewById(R.id.tv_trailer_title);
            trailerTitle.setText(trailer.getName());
            mDetailBinding.movieDetailContent.llTrailers.addView(item);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Trailer>> loader) {

        if (mDetailBinding.movieDetailContent.llTrailers.getChildCount() > 0) {
            mDetailBinding.movieDetailContent.llTrailers.removeAllViews();
        }
    }
}
