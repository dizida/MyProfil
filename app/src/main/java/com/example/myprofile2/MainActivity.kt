package com.example.myprofile2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myprofile2.ui.theme.ActorsScreen
import com.example.myprofile2.ui.theme.MyProfile2Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewmodel: MainViewModel by viewModels()
        setContent {
            MyProfile2Theme {
                AppNavHost(viewmodel)

            }
        }
    }
}

@Composable
fun AppNavHost(viewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "profil") {

        composable("profil") {
            ProfilScreen(
                onNavigateToFilmScreen = {
                    navController.navigate("film")
                },
            )
        }

        composable("film") {
            FilmScreen(
                navController = navController,
                onNavigateToProfilScreen = {
                    navController.navigate("profil")
                },
                onNavigateToSeries = {
                    navController.navigate("series")
                },
                onNavigateToActors = {
                    navController.navigate("actors")
                },
                viewModel = viewModel
            )
        }

        composable("series") {
            SerieScreen(
                navController = navController,
                onNavigateToFilm = {
                    navController.navigate("film")
                },
                onNavigateToProfilScreen = {
                    navController.navigate("profil")
                },
                onNavigateToSeries = {
                    navController.navigate("series")
                },
                onNavigateToActors = {
                    navController.navigate("actors")
                },

                viewModel = viewModel
            )
        }

        composable("actors") {
            ActorsScreen(
                navController = navController,
                onNavigateToFilm = {
                    navController.navigate("film")
                },
                onNavigateToProfilScreen = {
                    navController.navigate("profil")
                },
                onNavigateToSeries = {
                    navController.navigate("series")
                },
                onNavigateToActors = {
                    navController.navigate("actors")
                },

                viewModel = viewModel
            )
        }

        composable("filmDetail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            if (movieId != null) {
                FilmDetailScreen(movieId = movieId, viewModel = viewModel)
            } else {
                Text("Erreur : ID du film manquant.")
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun ProfilScreenPreview() {
    MyProfile2Theme {
        ProfilScreen(onNavigateToFilmScreen = {})
    }
}

@Preview(showBackground = true)
@Composable
fun FilmScreenPreview() {
    val navController = rememberNavController()
    MyProfile2Theme {
        FilmScreen(
            navController = navController,
            onNavigateToProfilScreen = {},
            onNavigateToSeries = {},
            onNavigateToActors = {},
            viewModel = MainViewModel()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SerieScreenPreview() {
    val navController = rememberNavController()
    MyProfile2Theme {
        SerieScreen(
            navController = navController,
            onNavigateToProfilScreen = {},
            onNavigateToSeries = {},
            onNavigateToFilm = {},
            onNavigateToActors = {},
            viewModel = MainViewModel()
        )
    }
}


