package tech.eduardosolano.movietime.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

import okhttp3.internal.Util;
import tech.eduardosolano.movietime.Api.Response.Result;
import tech.eduardosolano.movietime.R;
import tech.eduardosolano.movietime.Utils.Constants;
import tech.eduardosolano.movietime.Utils.Utils;

public class DetailMovieFragment extends Fragment {
    private Result movieItem;
    private FloatingActionButton fab;
    private ImageView imageViewLike;

    public DetailMovieFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieItem = (Result) getArguments().getSerializable("item");
        }
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_movie, container, false);
        ImageView imageViewPost = (ImageView) view.findViewById(R.id.imageView2);
        imageViewLike = (ImageView) view.findViewById(R.id.imageViewLike);
        TextView textviewTitle = (TextView) view.findViewById(R.id.textviewTitle);
        TextView textViewOverview = (TextView) view.findViewById(R.id.textViewOverview);
        TextView textViewGenres = (TextView) view.findViewById(R.id.textViewGenres);
        TextView textViewRelease = (TextView) view.findViewById(R.id.textViewRelease);
        TextView textViewAvg = (TextView) view.findViewById(R.id.textViewAvg);

        if (Utils.getInstance().getFavoriteList().contains(Integer.valueOf(movieItem.getId()))) {
            imageViewLike.setImageDrawable(getResources().getDrawable(R.drawable.like_filled));
        } else {
            imageViewLike.setImageDrawable(getResources().getDrawable(R.drawable.like));
        }
        textViewRelease.setText(Utils.getInstance().transformDate(movieItem.getReleaseDate()));
        textviewTitle.setText(movieItem.getTitle().toUpperCase());
        textViewOverview.setText(movieItem.getOverview());
        textViewAvg.setText(movieItem.getVoteAverage() + "/10");
        textViewGenres.setText(TextUtils.join(" · ", Utils.getInstance().getGenreList(movieItem.getGenresArrayList())));
        Picasso.with(getContext()).load(Constants.URL_IMAGE + movieItem.getPosterPath()).into(imageViewPost);

        imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageViewLike.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.like).getConstantState()) {
                    Utils.getInstance().getFavoriteList().add(Integer.valueOf(movieItem.getId()));
                    imageViewLike.setImageDrawable(getResources().getDrawable(R.drawable.like_filled));
                } else {
                    Utils.getInstance().getFavoriteList().remove(Integer.valueOf(movieItem.getId()));
                    imageViewLike.setImageDrawable(getResources().getDrawable(R.drawable.like));
                }

            }
        });
        return view;
    }
}