package com.example.hiren_pc_hp.popularmoviesapp1.utils;

public class ApiUtils {
    public static final String BASE_URL="http://api.themoviedb.org/3/";

    public static MovieService getMovieService(){
        return RetroFitClient.getClient(BASE_URL).create(MovieService.class);
    }

}
