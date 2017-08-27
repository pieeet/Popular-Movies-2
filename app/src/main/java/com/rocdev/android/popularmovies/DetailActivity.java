package com.rocdev.android.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.rocdev.android.popularmovies.databinding.ActivityDetailBinding;
import com.rocdev.android.popularmovies.models.Movie;

public class DetailActivity extends AppCompatActivity {

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
        mMovie = getIntent().getParcelableExtra(getString(R.string.key_intent_movie));
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        initNavigation();
        bindData();

    }



    private void initNavigation() {
        setSupportActionBar(mDetailBinding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindData() {
        mDetailBinding.movieDetailContent.tvTitle.setText(mMovie.getOriginalTitle());
    }




}
