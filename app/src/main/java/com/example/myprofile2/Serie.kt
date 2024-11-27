package com.example.myprofile2


import android.util.Log
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import com.example.myprofile2.formatReleaseDate
import kotlin.math.log


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SerieScreen(navController: NavController,
                onNavigateToProfilScreen: () -> Unit,
                onNavigateToFilm: () -> Unit,
                onNavigateToSeries: () -> Unit,
                onNavigateToActors: () -> Unit,
                viewModel: MainViewModel,
                windowSizeClass: WindowSizeClass
) {


    val series by viewModel.series.collectAsState()
    val serieGridState = rememberLazyGridState()  // Utilise rememberLazyGridState pour la grille
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(serieGridState) {
        viewModel.getPopularSeries()
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
                    IconButton(onClick = {navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description",
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
                }
            )
        },
        bottomBar = {
            // Condition pour afficher la barre uniquement en mode portrait
            if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
                BottomBar(
                    navController = navController,
                    onNavigateToFilm = {},
                    onNavigateToSeries = onNavigateToSeries,
                    onNavigateToActors = onNavigateToActors,
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
                        viewModel.getPopularSeries()
                    } else {
                        viewModel.searchSeries(query)
                    }
                }
            )
            when (windowSizeClass.windowWidthSizeClass) {
                WindowWidthSizeClass.COMPACT -> {
                    CompactPortraitScreenSeries(
                        series = series,
                        gridState = serieGridState,
                        onSerieClick = { serieId: Int -> navController.navigate("serieDetail/$serieId") },

                    )
                }

                else -> {
                    CompactLandscapeScreenSeries(
                        series = series,
                        gridState = serieGridState,
                        onSerieClick = { serieId: Int -> navController.navigate("serieDetail/$serieId") },
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

@OptIn(ExperimentalCoilApi::class)
@Composable
fun SerieItem(serie: Serie, onClick: (Int) -> Unit) {
    val formattedDate = formatReleaseDate(serie.first_air_date)
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable { onClick(serie.id) }
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
fun CompactPortraitScreenSeries(
    series: List<Serie>,
    gridState: LazyGridState,
    onSerieClick: (Int) -> Unit,

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
            items(series) { serie ->
                SerieItem(serie = serie, onClick = onSerieClick)
            }
        }
    }
}

@Composable
fun CompactLandscapeScreenSeries(
    navController: NavController,
    series: List<Serie>,
    gridState: LazyGridState,
    onSerieClick: (Int) -> Unit,
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
            onNavigateToFilm = onNavigateToFilm,
            onNavigateToSeries = onNavigateToSeries,
            onNavigateToActors = onNavigateToActors,
            navController = navController,
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
                items(series) { serie ->
                    SerieItem(serie = serie, onClick = onSerieClick)
                }
            }
        }
    }
}