package com.example.myprofile2

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplicationtest.playlistjson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit





// Déclaration de la classe MainViewModel qui hérite de ViewModel

class MainViewModel : ViewModel() {

    // --------- Movie Section --------- //

    // Déclaration d'un MutableStateFlow pour stocker la liste des films
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())

    // Exposition de l'état des films en tant que StateFlow
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    // --------- Serie Section --------- //
    private val _series = MutableStateFlow<List<Serie>>(emptyList())
    val series: StateFlow<List<Serie>> = _series.asStateFlow()

    // --------- Person Section --------- //
    private val _acteurs = MutableStateFlow<List<Acteur>>(emptyList())
    val acteurs: StateFlow<List<Acteur>> = _acteurs.asStateFlow()

    private val _movieDetails = MutableStateFlow<DetailsDuFilm?>(null)
    val movieDetails: StateFlow<DetailsDuFilm?> = _movieDetails.asStateFlow()

    private val _serieDetails = MutableStateFlow<DetailsDeLaSerie?>(null)
    val serieDetails: StateFlow<DetailsDeLaSerie?> = _serieDetails.asStateFlow()

    private val _playlist = MutableStateFlow<Playlist?>(null)
    val playlist: StateFlow<Playlist?> = _playlist.asStateFlow()

    private val apiKey = "b6ca979d56d43e776f609f4858c2c1dd"

    init {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val httpClient = OkHttpClient.Builder().addInterceptor(logging).build()
    }

    // Instance de Retrofit pour effectuer des requêtes HTTP
    val service = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(TmdbAPI::class.java)


    fun getTrendingMovies() {
        viewModelScope.launch {
            try {
                _movies.value = service.getTrendingMovies(apiKey).results
                Log.v("movie", "Films réupérés : ${_movies.value}")
            } catch (e: Exception) {
                // Affichage de l'erreur en cas d'échec de la récupération
                Log.v("movie1", "Erreur lors de la récupération des films : ${e.message}")
            }
        }
    }

    fun getPopularSeries() {
        viewModelScope.launch {
            try {
                val seriesResult = service.discoverSeries(apiKey)
                _series.value = seriesResult.results
                Log.v("zzzzz", "Réponse brute de l'API : $seriesResult")
            } catch (e: Exception) {
                Log.v("zzzzz", "Erreur lors de la récupération des séries : ${e.message}")
            }
        }
    }

    fun getTrendingPerson() {
        viewModelScope.launch {
            try {
                val actorsResult = service.getTrendingPerson(apiKey)
                _acteurs.value = actorsResult.results
                Log.v("actors", "Acteurs récupérés : ${actorsResult}")
            } catch (e: Exception) {
                Log.v("actors1", "Erreur lors de la récupération des acteurs : ${e.message}")
            }
        }
    }

    fun getMovieById(movieId: Int) {
        viewModelScope.launch {
            try {
                val movie = service.getMovieDetails(movieId, apiKey)
                _movieDetails.value = movie
                Log.v("movieDetails", "Détails du film récupérés : $movie")
            } catch (e: Exception) {
                Log.v(
                    "movieDetailsError",
                    "Erreur lors de la récupération des détails du film : ${e.message}"
                )
            }
        }
    }

    fun getSerieById(serieId: Int) {
        viewModelScope.launch {
            try {
                val serie = service.getSerieDetails(serieId, apiKey)
                _serieDetails.value = serie
                Log.v("serieDetails", "Détails de la série récupérés : $serie")
            } catch (e: Exception) {
                Log.v(
                    "serieDetailsError",
                    "Erreur lors de la récupération des détails de la série : ${e.message}"
                )
            }
        }
    }

    fun searchFilms(query: String) {
        viewModelScope.launch {
            try {
                _movies.value = service.getFilmsParMotCle(apiKey, query).results
            } catch (e: Exception) {
                Log.v("MainViewModel", "Erreur lors de la recherche des films, ${e.message}")
            }
        }
    }

    fun searchSeries(query: String) {
        viewModelScope.launch {
            try {
                _series.value = service.getSeriesParMotCle(apiKey, query).results
            } catch (e: Exception) {
                Log.v("MainViewModel", "Erreur lors de la recherche des séries, ${e.message}")
            }
        }
    }

    fun searchActors(query: String) {
        viewModelScope.launch {
            try {
                _acteurs.value = service.getActorsParMotCle(apiKey, query).results
            } catch (e: Exception) {
                Log.v("MainViewModel", "Erreur lors de la recherche des acteurs, ${e.message}")
            }
        }
    }

    fun fetchPlaylist(): Playlist {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(Playlist::class.java).fromJson(playlistjson)!!
    }

    fun getPlaylist() {
        viewModelScope.launch {
            _playlist.value = fetchPlaylist()
        }
    }


}

