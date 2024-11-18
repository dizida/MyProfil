package com.example.myprofile2.ui.theme

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
import com.example.myprofile2.MainViewModel
import com.example.myprofile2.SerieBottomNavigationBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.core.app.Person
import coil.compose.AsyncImage
import com.example.myprofile2.Acteur

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActorsScreen(
    navController: NavController,
    onNavigateToProfilScreen: () -> Unit,
    onNavigateToFilm: () -> Unit,
    onNavigateToSeries: () -> Unit,
    onNavigateToActors: () -> Unit,
    viewModel: MainViewModel
) {
    val acteurs by viewModel.acteurs.collectAsState()
    val actorGridState = rememberLazyGridState()

// Utilisez LaunchedEffect pour charger les données
    LaunchedEffect(actorGridState) {
        viewModel.getTrendingPerson()
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
                    onNavigateToFilm = {
                        println("Nagigation to film screen")
                        navController.navigate("film")
                    },
                    onNavigateToSeries = {
                        println("Navigating to Series Screen")
                        navController.navigate("series")
                    },
                    onNavigateToActors = {
                        println("Navigating to Actors Screen")
                        navController.navigate("actors")
                    }
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
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
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
