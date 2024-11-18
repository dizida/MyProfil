package com.example.myprofile2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import com.example.myprofile2.formatReleaseDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SerieScreen(navController: NavController,
                onNavigateToProfilScreen: () -> Unit,
                onNavigateToFilm: () -> Unit,
                onNavigateToSeries: () -> Unit,
                onNavigateToActors: () -> Unit,
                viewModel: MainViewModel) {


    val series by viewModel.series.collectAsState()
    val serieGridState = rememberLazyGridState()  // Utilise rememberLazyGridState pour la grille

    LaunchedEffect(serieGridState) {
        viewModel.getPopularSeries()
    }

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
                SerieBottomNavigationBar(
                    navController = navController,
                    onNavigateToFilm = {println("Nagigation to film screen")
                        navController.navigate("film")},
                    onNavigateToSeries = {println("Navigating to Series Screen")
                        navController.navigate("series")},
                    onNavigateToActors = {println("Navigating to Actors Screen")
                        navController.navigate("actors")}
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
                state = serieGridState,
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (series.isEmpty()) {
                    item {
                        Text(text = "Aucunes séries trouvées.")
                    }
                } else {
                    items(series) { serie ->
                        SerieItem(serie)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun SerieItem(serie: Serie) {
    val formattedDate = formatReleaseDate(serie.first_air_date)
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ){
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${serie.poster_path}",
            contentDescription = serie.original_name,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Fit
        )
        Text(text = serie.original_name)
        Text(text = formattedDate)
    }
}


@Composable
fun SerieBottomNavigationBar(
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