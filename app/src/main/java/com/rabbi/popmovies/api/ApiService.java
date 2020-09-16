package com.rabbi.popmovies.api;

import com.rabbi.popmovies.models.MovieDetailsResponse;
import com.rabbi.popmovies.models.MovieResponse;
import com.rabbi.popmovies.models.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("movie/{sort_criteria}")
    Call<MovieResponse> getAllMovie(
            @Path("sort_criteria") String sort_criteria,
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page

    );

    @GET("movie/{movie_id}")
    Call<MovieDetailsResponse> getMovieDetails(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key,
            @Query("language") String language);

    @GET("movie/{movie_id}/videos")
    Call<TrailersResponse> getMovieTrailers(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key,
            @Query("language") String language);
}
