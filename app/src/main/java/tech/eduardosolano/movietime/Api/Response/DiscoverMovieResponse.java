package tech.eduardosolano.movietime.Api.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DiscoverMovieResponse implements Serializable {

    @SerializedName("total_results")
    @Expose
    private float total_results;
    @SerializedName("results")
    @Expose
    private ArrayList<Result> movieResult = new ArrayList<Result>();


    public float getTotal_results() {
        return total_results;
    }

    public void setTotal_results(float total_results) {
        this.total_results = total_results;
    }

    public ArrayList<Result> getMovieResult() {
        return movieResult;
    }

    public void setMovieResult(ArrayList<Result> movieResult) {
        this.movieResult = movieResult;
    }
}
