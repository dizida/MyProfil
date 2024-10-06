package com.example.myprofile2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myprofile2.ui.theme.MyProfile2Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewmodel : MainViewModel by viewModels()
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
            ProfilScreen(onNavigateToFilmScreen = {
                navController.navigate("film")
            })
        }

        composable("film") {
            FilmScreen(navController = navController, onNavigateToProfilScreen = {
                navController.navigate("profil")
            }, viewModel = viewModel)
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
        FilmScreen(navController = navController, onNavigateToProfilScreen = {}, viewModel = MainViewModel())
    }
}


