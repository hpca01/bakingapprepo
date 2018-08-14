package com.example.hiren_pc_hp.popularmoviesapp1.utils;
import com.example.hiren_pc_hp.popularmoviesapp1.data.Movie;
import com.example.hiren_pc_hp.popularmoviesapp1.data.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface MovieService {
    public String popular = "movie/popular?api_key=0dff3be5cf5aefa28a90cd35ef65288c";
    @GET(popular) Call<Movie> getPopularMovies();

    public String highestRated = "movie/top_rated?api_key=0dff3be5cf5aefa28a90cd35ef65288c";
    @GET(highestRated) Call<Movie> getHighestMovies();
}

