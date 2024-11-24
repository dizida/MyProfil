package com.example.myprofile2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun FilmDetailScreen(movieId: Int, viewModel: MainViewModel, navController: NavController) {
    val movie by viewModel.movieDetails.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.getMovieById(movieId)
    }

    movie?.let {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Backdrop
            item(span = { GridItemSpan(2) })
            { MovieBackdrop(backdropPath = it.backdrop_path, title = it.title) }

            // Poster, date, et genre
            item(span = { GridItemSpan(2) }) { MovieInfoSection(it) }

            item(span = { GridItemSpan(2) }) {
                SectionTitle(title = "Synopsis")
            }
            // Synopsis
            item(span = { GridItemSpan(2) }) {
                Text(text = it.overview, style = MaterialTheme.typography.bodyLarge)
            }

            // Tête d'affiche
            item(span = { GridItemSpan(2) }) {
                SectionTitle(title = "Tête d'affiche")
            }

            items(it.credits.cast) { actor ->
                CastFilmCard(actor)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
            }
        }

    } ?: run {
        Text("Chargement des détails du film...")
    }
}


@Composable
fun MovieBackdrop(backdropPath: String?, title: String) {
    AsyncImage(
        model = "https://image.tmdb.org/t/p/w500$backdropPath",
        contentDescription = title,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun MovieInfoSection(movie: DetailsDuFilm) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
            contentDescription = movie.title,
            modifier = Modifier
                .width(120.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
            Text(text = "Date de sortie: ${movie.release_date}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Genre: ${movie.genres.joinToString { it.name }}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}


@Composable
fun CastFilmCard(castFilm: CastFilm) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${castFilm.profile_path}",
            contentDescription = castFilm.name,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Crop
        )
        Text(text = castFilm.name, style = MaterialTheme.typography.bodyMedium)
    }
}

