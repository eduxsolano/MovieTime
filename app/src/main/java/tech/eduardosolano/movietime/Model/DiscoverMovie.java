package tech.eduardosolano.movietime.Model;

import java.io.Serializable;
import java.util.ArrayList;

import tech.eduardosolano.movietime.Api.Response.Result;

public class DiscoverMovie implements Serializable{


    private float total_results;
    private float total_pages;
    private ArrayList<Result>  movies= new ArrayList<Result>();

    public float getTotal_results() {
        return total_results;
    }

    public void setTotal_results(float total_results) {
        this.total_results = total_results;
    }

    public float getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(float total_pages) {
        this.total_pages = total_pages;
    }

    public ArrayList<Result> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Result> movies) {
        this.movies = movies;
    }
}
