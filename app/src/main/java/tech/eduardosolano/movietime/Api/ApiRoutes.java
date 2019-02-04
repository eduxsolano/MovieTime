package tech.eduardosolano.movietime.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tech.eduardosolano.movietime.Api.Response.DiscoverMovieResponse;

public interface ApiRoutes {

    @GET("discover/movie?")
    Call<DiscoverMovieResponse> getDiscoveryMovies(@Query("api_key") String apiKey, @Query("sort_by") String sort);



}
