package com.rocdev.android.popularmovies;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rocdev.android.popularmovies.databinding.ActivityDetailBinding;
import com.rocdev.android.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String BASE_URL_POSTER = "http://image.tmdb.org/t/p";
    // you can request different img width in tmdb. Uncomment the one needed
//    private static final String WIDTH_POSTER_W92 = "/w92";
//    private static final String WIDTH_POSTER_W154 = "/w154";
    public static final String WIDTH_POSTER_W185 = "/w185";
//    private static final String WIDTH_POSTER_W342 = "/w342";
//    private static final String WIDTH_POSTER_W500 = "/w500";
//    private static final String WIDTH_POSTER_W780 = "/w780";
//    private static final String WIDTH_POSTER_ORIG = "/original";

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

}
