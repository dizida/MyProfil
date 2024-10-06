package com.example.myprofile2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MainViewModel : ViewModel() {
    val movies = MutableStateFlow<List<Movie>>(listOf())

    val apiKey = "b6ca979d56d43e776f609f4858c2c1dd"

    // instance de retrofit
    val service = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(TmdbAPI::class.java)

    //
    fun searchMovies(motcle : String) {
        viewModelScope.launch {
            movies.value = service.getFilmsParMotCle(apiKey, motcle).results
            }
        }

    fun getTrendingMovies() {
        viewModelScope.launch {
            try {
                movies.value = service.getTrendingMovies(apiKey).results
            } catch (e: Exception) {
                // affichage de l'erreur
                println("Erreur lors de la récupération des films : ${e.message}")
            }
        }
    }
}

