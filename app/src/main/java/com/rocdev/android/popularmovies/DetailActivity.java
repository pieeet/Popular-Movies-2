package com.rocdev.android.popularmovies;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.rocdev.android.popularmovies.databinding.ActivityDetailBinding;
import com.rocdev.android.popularmovies.models.Movie;
import com.rocdev.android.popularmovies.models.Review;
import com.rocdev.android.popularmovies.models.Trailer;
import com.rocdev.android.popularmovies.utils.JsonUtils;
import com.rocdev.android.popularmovies.utils.NetworkUtils;
import com.rocdev.android.popularmovies.utils.QueryUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private static final int LOADER_ID_TRAILERS = 4343;
    private static final int LOADER_ID_REVIEWS = 4545;


    private Movie mMovie;
    private ArrayList<Trailer> mTrailers;
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
        getLoaderManager().initLoader(LOADER_ID_TRAILERS, null, trailerLoader);
        getLoaderManager().initLoader(LOADER_ID_REVIEWS, null, reviewLoader);
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
                getString(R.string.avg_vote_prefix), " ", mMovie.getVoteAverage()));
        String url = BASE_URL_POSTER + WIDTH_POSTER_W185 + mMovie.getPosterPath();
        Picasso.with(this).load(url).into(mDetailBinding.movieDetailContent.ivPoster);

        // without a thread it also seems to work, but better safe than sorry...
        // see https://android-developers.googleblog.com/2009/05/painless-threading.html
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean isSaved = QueryUtils.isSaved(mMovie.getMovieId(), DetailActivity.this);
                mDetailBinding.movieDetailContent.cbSaveMovie.post(new Runnable() {
                    @Override
                    public void run() {
                        mDetailBinding.movieDetailContent.cbSaveMovie.setChecked(isSaved);
                    }
                });
            }
        }).start();
        mDetailBinding.movieDetailContent.cbSaveMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) QueryUtils.insertMovie(mMovie, DetailActivity.this);
                else QueryUtils.deleteMovie(mMovie, DetailActivity.this);


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.menu_item_share:
                shareMovie();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareMovie() {
        if (mTrailers != null && !mTrailers.isEmpty()) {
            Trailer trailer = mTrailers.get(0);
            String mimeType = "text/plain";
            String title = getString(R.string.share_intent_title);
            String message = mMovie.getOriginalTitle() + "\n----------\n" +
                    trailer.getYouTubeUrl();
            ShareCompat.IntentBuilder
                    .from(this)
                    .setType(mimeType)
                    .setChooserTitle(title)
                    .setText(message)
                    .startChooser();
        } else {
            Toast.makeText(this, R.string.toast_no_trailers, Toast.LENGTH_SHORT).show();
        }


    }

    private LoaderManager.LoaderCallbacks<List<Trailer>> trailerLoader = new LoaderManager
            .LoaderCallbacks<List<Trailer>>() {

        @Override
        public Loader<List<Trailer>> onCreateLoader(int i, Bundle bundle) {
            return new AsyncTaskLoader<List<Trailer>>(DetailActivity.this) {
                ArrayList<Trailer> trailers;

                @Override
                protected void onStartLoading() {
                    mDetailBinding.movieDetailContent.pbTrailerLoader.setVisibility(View.VISIBLE);
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
                    mDetailBinding.movieDetailContent.pbTrailerLoader.setVisibility(View.GONE);
                    trailers = (ArrayList<Trailer>) data;
                    mTrailers = trailers;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> trailers) {
            LayoutInflater inflater = LayoutInflater.from(DetailActivity.this);
            if (trailers != null) {
                for (final Trailer trailer : trailers) {
                    @SuppressLint("InflateParams")
                    View item = inflater.inflate(R.layout.trailer_item, null);
                    TextView trailerTitle = item.findViewById(R.id.tv_trailer_title);
                    trailerTitle.setText(trailer.getName());
                    mDetailBinding.movieDetailContent.llTrailers.addView(item);
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri trailerUri = Uri.parse(trailer.getYouTubeUrl());
                            startActivity(new Intent(Intent.ACTION_VIEW, trailerUri));
                        }
                    });
                }
            }
            if (trailers == null || trailers.isEmpty()) {
                mDetailBinding.movieDetailContent.tvNoTrailers.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {

            if (mDetailBinding.movieDetailContent.llTrailers.getChildCount() > 0) {
                mDetailBinding.movieDetailContent.llTrailers.removeAllViews();
            }
        }
    };

    private LoaderManager.LoaderCallbacks<List<Review>> reviewLoader = new LoaderManager
            .LoaderCallbacks<List<Review>>() {
        @Override
        public Loader<List<Review>> onCreateLoader(int i, Bundle bundle) {
            return new AsyncTaskLoader<List<Review>>(DetailActivity.this) {
                ArrayList<Review> reviews;

                @Override
                protected void onStartLoading() {
                    mDetailBinding.movieDetailContent.pbReviewLoader.setVisibility(View.VISIBLE);
                    if (reviews == null) {
                        forceLoad();
                    } else {
                        deliverResult(reviews);
                    }
                }

                @Override
                public List<Review> loadInBackground() {
                    try {
                        String reviewsJsonString = NetworkUtils.getReviewsJson(mMovie.getMovieId());
                        return (JsonUtils.getReviewsFromJson(reviewsJsonString));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public void deliverResult(List<Review> data) {
                    mDetailBinding.movieDetailContent.pbReviewLoader.setVisibility(View.GONE);
                    reviews = (ArrayList<Review>) data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> reviews) {
            LayoutInflater inflater = LayoutInflater.from(DetailActivity.this);
            if (reviews != null) {
                for (int i = 0; i < reviews.size(); i++) {
                    Review review = reviews.get(i);
                    @SuppressLint("InflateParams")
                    View item = inflater.inflate(R.layout.review_item, null);
                    TextView author = item.findViewById(R.id.tv_author);
                    author.setText(review.getAuthor());
                    TextView content = item.findViewById(R.id.tv_content);
                    content.setText(review.getContent());
                    mDetailBinding.movieDetailContent.llReviews.addView(item);
                }
            }
            if (reviews == null || reviews.isEmpty()) {
                mDetailBinding.movieDetailContent.tvNoReviews.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {
            if (mDetailBinding.movieDetailContent.llReviews.getChildCount() > 0) {
                mDetailBinding.movieDetailContent.llReviews.removeAllViews();
            }
        }
    };


}
