package com.example.myprofile2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
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
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
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
                    Log.d("navigationSeries","Navigating to series")
                    navController.navigate("series")
                },
                onNavigateToActors = {
                    navController.navigate("actors")
                },
                onNavigateToFilm = {
                    Log.v("navigationFilm","Navigating to film")
                    navController.navigate("film")
                },

                viewModel = viewModel,

                windowSizeClass = windowSizeClass,
                onNavigateToPlaylist = {
                    navController.navigate("playlist")
                },
            )
        }

        composable("series") {
            SerieScreen(
                navController = navController,
                onNavigateToFilm = {
                    Log.v("navigationFilm","Navigating to film")
                    navController.navigate("film")
                },
                onNavigateToProfilScreen = {
                    Log.v("navigationProfil","Navigating to profil")
                    navController.navigate("profil")
                },
                onNavigateToSeries = {
                    navController.navigate("series")
                },
                onNavigateToActors = {
                    navController.navigate("actors")
                },

                viewModel = viewModel,
                windowSizeClass = windowSizeClass,
                onNavigateToPlaylist = {
                    navController.navigate("playlist")
                },
            )
        }

        composable("actors") {
            ActorsScreen(
                navController = navController,
                onNavigateToFilm = {
                    Log.v("navigationFilm","Navigating to film")
                    navController.navigate("film")
                    Log.v("navigationFilm","After navigating to film")
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


                viewModel = viewModel,
                windowSizeClass = windowSizeClass,
                onNavigateToPlaylist = {
                    navController.navigate("playlist")
                },
            )
        }

        composable("playlist") {
            PlaylistScreen(
                navController = navController,
                onNavigateToPlaylist = {
                    navController.navigate("playlist")
                },
                onNavigateToFilm = {
                    Log.v("navigationFilm","Navigating to film")
                    navController.navigate("film")
                    Log.v("navigationFilm","After navigating to film")
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

                viewModel = viewModel,
            )
        }

        composable("filmDetail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            if (movieId != null) {
                FilmDetailScreen(movieId = movieId, viewModel = viewModel, navController = navController)
            } else {
                Text("Erreur : ID du film manquant.")
            }
        }

        composable("serieDetail/{serieId}") { backStackEntry ->
            val serieId = backStackEntry.arguments?.getString("serieId")?.toIntOrNull()
            if (serieId != null) {
                SerieDetailScreen(serieId = serieId, viewModel = viewModel, navController = navController)
            } else {
                Text("Erreur : ID de la série manquant.")
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
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    MyProfile2Theme {
        FilmScreen(
            navController = navController,
            onNavigateToProfilScreen = {},
            onNavigateToSeries = {},
            onNavigateToActors = {},
            viewModel = MainViewModel(),
            windowSizeClass = windowSizeClass,
            onNavigateToFilm = {},
            onNavigateToPlaylist = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SerieScreenPreview() {
    val navController = rememberNavController()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    MyProfile2Theme {
        SerieScreen(
            navController = navController,
            onNavigateToProfilScreen = {},
            onNavigateToSeries = {},
            onNavigateToFilm = {},
            onNavigateToActors = {},
            viewModel = MainViewModel(),
            windowSizeClass = windowSizeClass,
            onNavigateToPlaylist = {}
        )
    }
}


