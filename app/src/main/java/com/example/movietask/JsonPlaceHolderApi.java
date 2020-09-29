package com.example.movietask;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {
    //@GET("3/search/movie?api_key=2696829a81b1b5827d515ff121700838&query=batman&page=1")
    //Call<MovieHelper> getHelperMovies();

    @GET("3/search/movie?api_key=2696829a81b1b5827d515ff121700838&page=1&") // ovdje
    Call<MovieHelper> getHelperMovies(@Query("query") String title, @Query("page") int page);
}
