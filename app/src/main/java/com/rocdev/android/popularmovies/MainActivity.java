package com.rocdev.android.popularmovies;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rocdev.android.popularmovies.adapters.MoviesAdapter;
import com.rocdev.android.popularmovies.models.Movie;
import com.rocdev.android.popularmovies.utils.JsonUtils;
import com.rocdev.android.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<List<Movie>>,
        MoviesAdapter.MoviesAdapterListener {

    private static final int LOADER_ID = 4242;

//    private final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final int POPULAR_MOVIES = 0;
    public static final int TOP_RATED_MOVIES = 1;

    private MoviesAdapter mMoviesAdapter;
//    private ProgressBar mLoadingIndicator;
    private ArrayList<Movie> mMovies;
    private int sortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigation();
        getLoaderManager().initLoader(LOADER_ID, null, this);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_gridview);
//        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mRecyclerView.setHasFixedSize(true);
        mMoviesAdapter = new MoviesAdapter(this, this);
        mRecyclerView.setAdapter(mMoviesAdapter);
        //TODO make preference
        sortOrder = POPULAR_MOVIES;
        setTitleActionBar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movies", mMovies);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMovies = savedInstanceState.getParcelableArrayList("movies");
    }

    private void initNavigation() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(sortOrder).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_popular) {
            if (sortOrder != POPULAR_MOVIES) {
                sortOrder = POPULAR_MOVIES;
                mMoviesAdapter.notifyMoviesChanged(null);
                getLoaderManager().restartLoader(LOADER_ID, null, this);
            }
        } else if (id == R.id.nav_top) {
            if (sortOrder != TOP_RATED_MOVIES) {
                sortOrder = TOP_RATED_MOVIES;
                mMoviesAdapter.notifyMoviesChanged(null);
                getLoaderManager().restartLoader(LOADER_ID, null, this);
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        setTitleActionBar();
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    private void setTitleActionBar() {

        if (sortOrder == POPULAR_MOVIES) {
            getSupportActionBar().setTitle(getString(R.string.nav_bar_popular));
        }
        else if (sortOrder == TOP_RATED_MOVIES) {
            getSupportActionBar().setTitle(getString(R.string.nav_bar_top_rated));
        }
    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<List<Movie>>(this) {

            ArrayList<Movie> movies;

            @Override
            public List<Movie> loadInBackground() {
                try {
                    String moviesJsonString = NetworkUtils.getMoviesJson(sortOrder);
                    return JsonUtils.extractMoviesFromJson(moviesJsonString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onStartLoading() {
                if (movies == null) {
                    forceLoad();
                } else {
                    deliverResult(movies);
                }
            }

            @Override
            public void deliverResult(List<Movie> data) {
                movies = (ArrayList<Movie>) data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Movie>> loader, List<Movie> movies) {
        mMovies = (ArrayList<Movie>) movies;
        mMoviesAdapter.notifyMoviesChanged(mMovies);
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Movie>> loader) {
        mMoviesAdapter.notifyMoviesChanged(null);
    }


    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(getString(R.string.key_intent_movie), movie);
        startActivity(intent);
    }
}
