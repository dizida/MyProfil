package com.example.myprofile2

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tv
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun Sidebar(
    navController: NavController,
    onNavigateToFilm: () -> Unit,
    onNavigateToSeries: () -> Unit,
    onNavigateToActors: () -> Unit,
    onNavigateToProfil: () -> Unit,
    onNavigateToHorror: () -> Unit

) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val isFilm = currentDestination?.route == "film"
    val isSeries = currentDestination?.route == "series"
    val isActors = currentDestination?.route == "actors"
    val isProfil = currentDestination?.route == "profil"
    val isHorror = currentDestination?.route == "horror"

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(80.dp)
            .background(Color(0xFFFF5722)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .clickable(onClick = { onNavigateToProfil() })
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(

                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profil",
                    tint = if (isProfil) Color.Yellow else Color.White
                )
                Text(
                    text = "Profil",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .clickable(onClick = {
                    Log.d("Sidebar", "Navigating to film")
                    onNavigateToFilm() })
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(

                    imageVector = Icons.Default.Movie,
                    contentDescription = "Films",
                    tint = if (isFilm) Color.Yellow else Color.White
                )
                Text(
                    text = "Films",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .clickable(onClick = {
                    Log.d("Sidebar", "Navigating to Series")
                    onNavigateToSeries() })
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Tv,
                    contentDescription = "Séries",
                    tint = if (isSeries) Color.Yellow else Color.White
                )
                Text(
                    text = "Séries",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .clickable(onClick = { onNavigateToActors() })
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Acteurs",
                    tint = if (isActors) Color.Yellow else Color.White
                )
                Text(
                    text = "Acteurs",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .clickable(onClick = { onNavigateToActors() })
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Horror",
                    tint = if (isHorror) Color.Yellow else Color.White
                )
                Text(
                    text = "Horror",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }
    }
}
