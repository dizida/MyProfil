package com.example.myprofile2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
fun SerieDetailScreen(serieId: Int, viewModel: MainViewModel, navController: NavController) {
    val serie by viewModel.serieDetails.collectAsState()

    LaunchedEffect(serieId) {
        viewModel.getSerieById(serieId)
    }

    serie?.let {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Backdrop
            item(span = { GridItemSpan(2) }) {
                SerieBackdrop(backdropPath = it.backdrop_path, title = it.name)
            }

            // Poster, date, et genre
            item(span = { GridItemSpan(2) }) { SerieInfoSection(it) }

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
                CastSerieCard(actor)
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
        Text("Chargement des détails de la série...")
    }
}

@Composable
fun SerieBackdrop(backdropPath: String?, title: String) {
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
fun SerieInfoSection(serie: DetailsDeLaSerie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${serie.poster_path}",
            contentDescription = serie.name,
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
            Text(text = serie.name, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "Date de première diffusion: ${serie.first_air_date}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Genre: ${serie.genres.joinToString { it.name }}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
fun CastSerieCard(cast: Cast) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${cast.profile_path}",
            contentDescription = cast.name,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Crop
        )
        Text(text = cast.name, style = MaterialTheme.typography.bodyMedium)
    }
}