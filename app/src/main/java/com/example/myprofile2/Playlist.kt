package com.example.myprofile2

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import coil.compose.AsyncImage
import com.example.myapplicationtest.playlistjson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    navController: NavController,
    onNavigateToProfilScreen: () -> Unit,
    onNavigateToSeries: () -> Unit,
    onNavigateToActors: () -> Unit,
    onNavigateToFilm: () -> Unit,
    onNavigateToPlaylist: () -> Unit,
    viewModel: MainViewModel,
    windowSizeClass: WindowSizeClass

){
    val playlist by viewModel.playlist.collectAsState()
    val gridState = rememberLazyGridState()
    LaunchedEffect(gridState) {
        viewModel.getPlaylist()
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
                    IconButton(onClick = { }) {
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
                    onNavigateToProfil = onNavigateToProfilScreen,
                    onNavigateToPlaylist = onNavigateToPlaylist
                )
            }
        }

    ){
            innerPadding ->
        CustomBackgroundColor()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            when (windowSizeClass.windowWidthSizeClass) {
                WindowWidthSizeClass.COMPACT -> {
                    playlist?.let {
                        CompactPortraitScreen(
                            playlist = it,
                            gridState = gridState,
                            viewModel = viewModel
                        )
                    }
                }

                else -> {
                    playlist?.let {
                        CompactLandscapeScreen(
                            playlist= it,
                            gridState = gridState,
                            onNavigateToFilm = onNavigateToFilm,
                            onNavigateToSeries = onNavigateToSeries,
                            onNavigateToActors = onNavigateToActors,
                            navController = navController,
                            onNavigateToPlaylist = onNavigateToPlaylist
                        )
                    }
                }
            }
        }

    }


}

@Composable
fun CompactPortraitScreen(
    playlist: Playlist,
    gridState: LazyGridState,
    viewModel: MainViewModel,

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
            items(playlist.tracks.data) { track ->
                PlaylistItem(track)
            }
        }
    }
}

@Composable
fun PlaylistItem(playlist: Data) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)

    ) {
        AsyncImage(
            model = "file:///android_asset/images/2.jpg",
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun CompactLandscapeScreen(
    navController: NavController,
    playlist: Playlist,
    gridState: LazyGridState,
    onNavigateToFilm: () -> Unit,
    onNavigateToSeries: () -> Unit,
    onNavigateToActors: () -> Unit,
    onNavigateToPlaylist: () -> Unit
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
            onNavigateToProfil = { navController.navigate("profil") },
            navController = navController,
            onNavigateToPlaylist = onNavigateToPlaylist
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
                items(playlist.tracks.data) { track ->
                    PlaylistItem(track)
                }
            }
        }
    }
}

