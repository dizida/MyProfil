package com.example.myprofile2

import com.example.myprofile2.ImageResult
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

    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): ImageResult

    @GET("tv/{tv_id}/images")
    suspend fun getSerieImages(
        @Path("tv_id") serieId: Int,
        @Query("api_key") apiKey: String
    ): SerieImageResult

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Movie

}

