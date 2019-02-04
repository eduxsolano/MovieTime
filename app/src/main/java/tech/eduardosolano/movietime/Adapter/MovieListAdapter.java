package tech.eduardosolano.movietime.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import tech.eduardosolano.movietime.Api.Response.Result;
import tech.eduardosolano.movietime.Fragment.MovieListFragment.OnListFragmentInteractionListener;
import tech.eduardosolano.movietime.R;
import tech.eduardosolano.movietime.Utils.Constants;
import tech.eduardosolano.movietime.Utils.EmptyRecyclerView;


import java.util.List;

public class MovieListAdapter extends EmptyRecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private final List<Result> mMovieItems;
    private final OnListFragmentInteractionListener mListener;
    private final Context mContext;

    public MovieListAdapter(List<Result> items, OnListFragmentInteractionListener listener, Context context) {
        mMovieItems = items;
        mListener = listener;
        mContext =context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mMovieItems.get(position);
        holder.textViewTitle.setText(mMovieItems.get(position).getTitle());
        holder.textViewVotes.setText("Rating: "+String.valueOf( mMovieItems.get(position).getVoteAverage())+"/10");
        Picasso.with(mContext)
                .load( Constants.URL_IMAGE + mMovieItems.get(position).getPosterPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imageViewMovie);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(mMovieItems.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovieItems != null ?  mMovieItems.size() : 0 ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewTitle;
        public final TextView textViewVotes;
        public  final ImageView imageViewMovie;
        public Result mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
            textViewVotes = (TextView) view.findViewById(R.id.textViewVotes);
            imageViewMovie = (ImageView) view.findViewById(R.id.imageViewMovie);
        }
    }
}
