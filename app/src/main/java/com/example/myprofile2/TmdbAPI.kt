package com.example.myprofile2

import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbAPI {
    @GET("search/movie")
    suspend fun getFilmsParMotCle(@Query("api_key") apiKey: String, @Query("query") motcle: String): TmdbResult

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(@Query("api_key") apiKey: String): TmdbResult

}