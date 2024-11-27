package com.example.myprofile2


import android.util.Log
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
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
import androidx.compose.material3.Button
import androidx.compose.material3.SearchBar
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
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
    onNavigateToFilm: () -> Unit,
    viewModel: MainViewModel,
    windowSizeClass: WindowSizeClass
) {

    val movies by viewModel.movies.collectAsState()
    val gridState = rememberLazyGridState()  // Utilise rememberLazyGridState pour la grille
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    LaunchedEffect(gridState) {
        viewModel.getTrendingMovies()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Fav'App") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { isSearchVisible = true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Rechercher",
                            tint = Color.White
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF5722),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            // Condition pour afficher la barre uniquement en mode portrait
            if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
                BottomBar(
                    navController = navController,
                    onNavigateToFilm = {navController.navigate("film")},
                    onNavigateToSeries = {navController.navigate("series")},
                    onNavigateToActors = {navController.navigate("actors")},
                    onNavigateToProfil = onNavigateToProfilScreen
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
            EnhancedSearchBar(
                isSearchVisible = isSearchVisible,
                onSearchVisibilityChanged = { isSearchVisible = it },
                searchText = searchText,
                onSearchTextChange = { newQuery ->
                    searchText = newQuery
                },
                onSearch = { query ->
                    if (query.isEmpty()) {
                        viewModel.getTrendingMovies()
                    } else {
                        viewModel.searchFilms(query)
                    }
                },
            )
                when (windowSizeClass.windowWidthSizeClass) {
                    WindowWidthSizeClass.COMPACT -> {
                        CompactPortraitScreen(
                            movies = movies,
                            gridState = gridState,
                            onMovieClick = { movieId: Int -> navController.navigate("filmDetail/$movieId") },

                        )
                    }

                    else -> {
                        CompactLandscapeScreen(
                            movies = movies,
                            gridState = gridState,
                            onMovieClick = { movieId: Int -> navController.navigate("filmDetail/$movieId") },
                            onNavigateToFilm = onNavigateToFilm,
                            onNavigateToSeries = onNavigateToSeries,
                            onNavigateToActors = onNavigateToActors,
                            navController = navController
                        )
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
fun CompactPortraitScreen(
    movies: List<Movie>,
    gridState: LazyGridState,
    onMovieClick: (Int) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(movies) { movie ->
                MovieItem(movie = movie, onClick = onMovieClick)
            }
        }
    }
}

@Composable
fun CompactLandscapeScreen(
    navController: NavController,
    movies: List<Movie>,
    gridState: LazyGridState,
    onMovieClick: (Int) -> Unit,
    onNavigateToFilm: () -> Unit,
    onNavigateToSeries: () -> Unit,
    onNavigateToActors: () -> Unit,
) {

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Start, // Permet à la Sidebar d'être positionnée à gauche
        verticalAlignment = Alignment.Top // Alignement vertical du contenu
    ) {
        // Affichage de la Sidebar à gauche
        Sidebar(
            navController = navController,
            onNavigateToFilm = onNavigateToFilm,
            onNavigateToSeries = onNavigateToSeries,
            onNavigateToActors = onNavigateToActors,
            onNavigateToProfil = { navController.navigate("profil") }
        )

        // Section principale avec barre de recherche et grille
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f) // La grille occupe tout l'espace restant
                .padding(16.dp)
        ) {

            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(3), // Augmente le nombre de colonnes en mode paysage
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(movies) { movie ->
                    MovieItem(movie = movie, onClick = onMovieClick)
                }
            }
        }
    }
}



