package com.example.myprofile2

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

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
){}