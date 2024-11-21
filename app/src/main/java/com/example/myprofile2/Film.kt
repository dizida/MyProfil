package com.example.myprofile2


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage

import com.example.myprofile2.formatReleaseDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmScreen(
    navController: NavController,
    onNavigateToProfilScreen: () -> Unit,
    onNavigateToSeries: () -> Unit,
    onNavigateToActors: () -> Unit,
    viewModel: MainViewModel
) {

    val movies by viewModel.movies.collectAsState()
    val gridState = rememberLazyGridState()  // Utilise rememberLazyGridState pour la grille

    LaunchedEffect(gridState) {
        viewModel.getTrendingMovies()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xFFFF5722),
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("Fav'App")
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateToProfilScreen() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Action pour la recherche */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Rechercher",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFFFF5722),
                contentColor = Color.White,
            ) {
                BottomNavigationBar(
                    navController = navController,
                    onNavigateToFilm = {},
                    onNavigateToSeries = { navController.navigate("series") },
                    onNavigateToActors = { navController.navigate("actors") }
                )
            }
        }

    ) { innerPadding ->
        CustomBackgroundColor()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (movies.isEmpty()) {
                    item {
                        Text(text = "Aucun film trouvé.")
                    }
                } else {
                    items(movies) { movie ->
                        MovieItem(movie, onClick = { movieId: Int ->
                            navController.navigate("filmDetail/$movieId")
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun CustomBackgroundColor() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF5722).copy(alpha = 0.5f)) // couleur personnalisée avec une opacité de 50%
            .padding(16.dp)
    ) {
        // Votre contenu
        Text("Contenu avec un arrière-plan personnalisé")
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun MovieItem(movie: Movie, onClick: (Int) -> Unit) {
    val formattedDate = formatReleaseDate(movie.release_date)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable { onClick(movie.id) }
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Fit
        )
        Text(text = movie.title)
        Text(text = formattedDate)
    }
}


@Composable
fun BottomNavigationBar(
    navController: NavController,
    onNavigateToFilm: () -> Unit,
    onNavigateToSeries: () -> Unit,
    onNavigateToActors: () -> Unit
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val isFilm = currentDestination?.route == "film"
    val isSeries = currentDestination?.route == "series"
    val isActors = currentDestination?.route == "actors"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Bouton Film
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = onNavigateToFilm) {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = "Films",
                    tint = if (isFilm) Color.Yellow else Color.White
                )
            }
            Text(
                text = "Films",
                style = MaterialTheme.typography.bodySmall,
                color = if (isFilm) Color.Yellow else Color.White
            )
        }

        // Bouton Série
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = onNavigateToSeries) {
                Icon(
                    imageVector = Icons.Default.Tv,
                    contentDescription = "Séries",
                    tint = if (isSeries) Color.Yellow else Color.White
                )
            }
            Text(
                text = "Séries",
                style = MaterialTheme.typography.bodySmall,
                color = if (isSeries) Color.Yellow else Color.White
            )
        }

        // Bouton Acteurs
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = onNavigateToActors) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Acteurs",
                    tint = if (isActors) Color.Yellow else Color.White
                )
            }
            Text(
                text = "Acteurs",
                style = MaterialTheme.typography.bodySmall,
                color = if (isActors) Color.Yellow else Color.White
            )
        }
    }
}

