package com.example.myprofile2

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActorsScreen(
    navController: NavController,
    onNavigateToProfilScreen: () -> Unit,
    onNavigateToFilm: () -> Unit,
    onNavigateToSeries: () -> Unit,
    onNavigateToActors: () -> Unit,
    viewModel: MainViewModel,
    windowSizeClass: WindowSizeClass
) {
    val acteurs by viewModel.acteurs.collectAsState()
    val actorGridState = rememberLazyGridState()
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

// Utilisez LaunchedEffect pour charger les données
    LaunchedEffect(actorGridState) {
        viewModel.getTrendingPerson()
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
                    onNavigateToProfil= onNavigateToProfilScreen
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
        )
        {
            EnhancedSearchBar(
            isSearchVisible = isSearchVisible,
            onSearchVisibilityChanged = { isSearchVisible = it },
            searchText = searchText,
            onSearchTextChange = { newQuery ->
                Log.v("newQuery","Search Text Changed:: $newQuery")
                searchText = newQuery
            },
            onSearch = { query ->
                Log.v("query","Search Query Submitted: $query")
                if (query.isEmpty()) {
                    viewModel.getTrendingPerson()
                } else {
                    viewModel.searchActors(query)
                }
            }

        )
            when (windowSizeClass.windowWidthSizeClass) {
                WindowWidthSizeClass.COMPACT -> {
                    CompactPortraitScreenActors(
                        acteurs = acteurs,
                        gridState = actorGridState,
                        onSerieClick = { serieId: Int -> navController.navigate("serieDetail/$serieId") },
                        viewModel = viewModel,
                        navController = navController
                    )
                }

                else -> {
                    CompactLandscapeScreenActors(
                        acteurs = acteurs,
                        gridState = actorGridState,
                        onSerieClick = { serieId: Int -> navController.navigate("serieDetail/$serieId") },
                        viewModel = viewModel,
                        onNavigateToFilm = onNavigateToFilm, // Passez les fonctions de navigation appropriées ici
                        onNavigateToSeries = onNavigateToSeries,
                        onNavigateToActors = onNavigateToActors,
                        navController = navController
                    )
                }
            }
            LazyVerticalGrid(
                state = actorGridState,
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (acteurs.isEmpty()) {
                    item {
                        Text(text = "Aucuns acteurs trouvés.")
                    }
                } else {
                    items(acteurs) { actor ->
                        ActorItem(actor)
                    }
                }
            }

        }
    }

}

@Composable
fun ActorItem(actors: Acteur) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ){
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${actors.profile_path}",
            contentDescription = actors.original_name,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Fit
        )
        Text(text = actors.original_name)

    }
}

@Composable
fun CompactPortraitScreenActors(
    acteurs: List<Acteur>,
    gridState: LazyGridState,
    onSerieClick: (Int) -> Unit,
    viewModel: MainViewModel,
    navController: NavController,

    ) {
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
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
                Log.v("querySerie", "Search Query Submitted: $query")
                if (query.isEmpty()) {
                    viewModel.getTrendingPerson()
                } else {
                    viewModel.searchActors(query)
                }
            }
        )

        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(acteurs) { actor ->
                ActorItem(actor)
            }
        }
    }
}

@Composable
fun CompactLandscapeScreenActors(
    navController: NavController,
    acteurs:List<Acteur>,
    gridState: LazyGridState,
    onSerieClick: (Int) -> Unit,
    onNavigateToFilm: () -> Unit,
    onNavigateToSeries: () -> Unit,
    onNavigateToActors: () -> Unit,
    viewModel: MainViewModel
) {
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

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
            onNavigateToProfil = { navController.navigate("profil") },
            navController = navController
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
                items(acteurs) { actor ->
                    ActorItem(actor)
                }
            }
        }
    }
}
