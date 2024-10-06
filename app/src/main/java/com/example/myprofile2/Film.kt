package com.example.myprofile2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items


@Composable
fun FilmScreen(navController: NavController, onNavigateToProfilScreen: () -> Unit, viewModel: MainViewModel ) {
    // contenu pour l'écran des films
    val movies by viewModel.movies.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.getTrendingMovies()
    }

    Column {
        Button(onClick = onNavigateToProfilScreen) {
            Text("Retour au profil")
        }
        LazyColumn {
                if (movies.isEmpty()) {
                    item {
                        Text(text = "Aucun film trouvé.")
                    }
                } else {
                    items(movies) { movie ->
                        MovieItem(movie)
                    }
                }
            }
    }


 //   Text(text = "Bienvenue dans l'écran des films")
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    ScreenFilm(windowSizeClass, navController, onNavigateToProfilScreen)
}

@Composable
fun MovieItem(movie: Movie) {
    Column {
        Text(text = movie.title)
        Text(text = movie.overview)
    }
}
@Composable
fun ScreenFilm(classes: WindowSizeClass, navController: NavController, onNavigateToFilmScreen: () -> Unit, viewModel: MainViewModel) {
    val classeLargeur = classes.windowWidthSizeClass
    when (classeLargeur) {
        WindowWidthSizeClass.COMPACT -> {
            CompactLayout2(navController, onNavigateToFilmScreen, viewModel)
        }
        else ->  {
            MediumLayout(onNavigateToFilmScreen)
        }

    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactLayout2(navController: NavController, onNavigateToProfilScreen: () -> Unit, viewModel: MainViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xFF6200EE),
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
                // utilisation de `actions` pour placer les icônes à droite
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
                containerColor = Color(0xFF6200EE),
                contentColor = Color.White,
            ) {
                BottomNavigationBar(
                    navController = navController,
                    onNavigateToFilm = {},
                    onNavigateToSeries = {},
                    onNavigateToActors = {}
                )
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FilmScreen(navController, onNavigateToProfilScreen, viewModel)
        }
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
