package com.rocdev.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rocdev.android.popularmovies.R;
import com.rocdev.android.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.rocdev.android.popularmovies.DetailActivity.BASE_URL_POSTER;
import static com.rocdev.android.popularmovies.DetailActivity.WIDTH_POSTER_W185;

/**
 * Created by piet on 25-08-17.
 *
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {


    private static final String LOG_TAG = MoviesAdapter.class.getSimpleName();

    private List<Movie> mMovies;
    private final Context mContext;
    private final MoviesAdapterListener mListener;

    public MoviesAdapter(Context context, MoviesAdapterListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View thumbView = inflater.inflate(R.layout.poster_thumb, parent, false);
        return new MoviesAdapterViewHolder(thumbView);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        final Movie movie = mMovies.get(position);
        String url = BASE_URL_POSTER + WIDTH_POSTER_W185 + movie.getPosterPath();
        Picasso.with(mContext).load(url).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) return 0;
        return mMovies.size();
    }

    public void notifyMoviesChanged(List<Movie> movies) {
        mMovies = movies;

        notifyDataSetChanged();
    }

    class MoviesAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final ImageView imageView;

        MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_poster_thumb);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Movie movie = mMovies.get(getAdapterPosition());
            mListener.onMovieClicked(movie);
        }
    }

    public interface MoviesAdapterListener {
        void onMovieClicked(Movie movie);
    }
}
