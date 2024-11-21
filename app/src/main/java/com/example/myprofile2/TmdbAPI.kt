package com.example.myprofile2


import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbAPI {
    @GET("search/movie")
    suspend fun getFilmsParMotCle(@Query("api_key") apiKey: String, @Query("query") motcle: String): TmdbResult

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(@Query("api_key") apiKey: String): TmdbResult

    @GET("trending/tv/week")
    suspend fun discoverSeries(@Query("api_key") apiKey: String, @Query("sort_by") sortBy: String = "popularity.desc"): TmdbResultSerie

    @GET("trending/person/week")
    suspend fun getTrendingPerson(@Query("api_key") apiKey: String): TmdbResultActors

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("append_to_response") appendToResponse: String = "credits",
        @Query("language") language: String ="fr"
    ): DetailsDuFilm

    @GET("movie/{movie_id}/credits")
    suspend fun getPersonMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Movie

    @GET("tv/{tv_id}")
    suspend fun getSerieDetails(
        @Path("tv_id") serieId: Int,
        @Query("api_key") apiKey: String,
        @Query("append_to_response") appendToResponse: String = "credits",
        @Query("language") language: String ="fr"
    ): DetailsDeLaSerie



}

