package tech.eduardosolano.movietime.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import tech.eduardosolano.movietime.Api.ApiRoutes;
import tech.eduardosolano.movietime.Api.Response.DiscoverMovieResponse;
import tech.eduardosolano.movietime.Api.Response.Result;
import tech.eduardosolano.movietime.Model.DiscoverMovie;
import tech.eduardosolano.movietime.Adapter.MovieListAdapter;
import tech.eduardosolano.movietime.R;
import tech.eduardosolano.movietime.Utils.Constants;
import tech.eduardosolano.movietime.Utils.EmptyRecyclerView;
import tech.eduardosolano.movietime.Utils.Utils;


public class MovieListFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mCallback;
    private DiscoverMovie discoverMovieObject;
    private View mView;
    private EmptyRecyclerView recyclerView;
    private FloatingActionButton fab;

    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setHasOptionsMenu(true);
        setRetainInstance(true);
        discoverMovieObject= new DiscoverMovie();

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fab.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.fab_favorites).getConstantState())){
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.fab_list));
                    discoverMovieObject.setMovies(Utils.getInstance().getFavoriteItemsArrayList(getContext()));
                }else{
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.fab_favorites));
                    discoverMovieObject.setMovies(getGsonMovies());
                }
                setAdapter();
            }
        });

        showTutorial();
    }



    private ArrayList<Result> getGsonMovies(){
        Gson gson= new Gson();
        String jsonOutput = Utils.getInstance().getSharedPreference(getContext());
        Type listType = new TypeToken<ArrayList<Result>>(){}.getType();
        return (ArrayList<Result>) gson.fromJson(jsonOutput,listType);
    }
    public void setAdapter(){
        MovieListAdapter adapter = new MovieListAdapter(discoverMovieObject.getMovies(), mCallback,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(mView.findViewById(R.id.empty));

        adapter.notifyDataSetChanged();

    }
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Result item);
    }

    public void setOnListFragmentInteractionListener(Activity activity) {
        mCallback = (OnListFragmentInteractionListener) activity;
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.app_name));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        fab.setVisibility(View.VISIBLE);
        Context context = mView.getContext();
        recyclerView = (EmptyRecyclerView) mView.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            }else{
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        if (!Utils.getInstance().isInternetAvailable(getContext())){
            discoverMovieObject.setMovies(getGsonMovies());
            setAdapter();
        }else{
            if (discoverMovieObject.getMovies().size()>0){
                setAdapter();
            }else{
                getData();
            }
        }
        //  recyclerView.setAdapter(new MovieListAdapter(discoverMovieObject.getMovies(), mCallback,getActivity(),getResources().getConfiguration().orientation));
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof  OnListFragmentInteractionListener){
            mCallback = (OnListFragmentInteractionListener)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public void getData( ){
        Retrofit retrofit = Utils.getInstance().getRetrofit( getActivity(), mView);
        ApiRoutes apiRoutes = retrofit.create(ApiRoutes.class);
        Call<DiscoverMovieResponse> discoverMovieCall = apiRoutes.getDiscoveryMovies(Constants.MoveDataBaseAPIKey, "popularity.desc");
        discoverMovieCall.enqueue(new Callback<DiscoverMovieResponse>() {
            @Override
            public void onResponse(Call<DiscoverMovieResponse> call, Response<DiscoverMovieResponse> response) {
                if (response.code() == 200){
                    discoverMovieObject= new DiscoverMovie();
                    discoverMovieObject.setTotal_results(response.body().getTotal_results());
                    discoverMovieObject.setMovies(response.body().getMovieResult());
                    Utils.getInstance().setSharedPreferenceData(getContext(),response.body().getMovieResult(),Constants.MOVIES, false);
                    if (isAdded()){
                        setAdapter();
                    }
                }else{
                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DiscoverMovieResponse> call, Throwable t) {
            }
        });
    }

    private void showTutorial() {
        if (!Utils.getInstance().isTutorialDone(getContext())){
            Utils.getInstance().setSharedPreferenceData(getContext(),null,Constants.TUTORIAL, true);
            TapTargetView.showFor(getActivity(),
                    TapTarget.forView(fab, getString(R.string.tutorial_title), getString(R.string.tutorial_desc))
                            .outerCircleColor(R.color.colorBlack)
                            .outerCircleAlpha(0.96f)
                            .targetCircleColor(R.color.colorWhite)
                            .titleTextSize(24)
                            .titleTextColor(R.color.colorWhite)
                            .descriptionTextSize(18)
                            .descriptionTextColor(R.color.colorWhite)
                            .textColor(R.color.colorWhite)
                            .dimColor(R.color.colorBlack30)
                            .drawShadow(true)
                            .tintTarget(false)
                            .cancelable(true)
                            .targetRadius(60));
        }
    }
}
