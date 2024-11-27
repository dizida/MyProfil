package com.example.myprofile2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.myprofile2.ui.theme.MyProfile2Theme
import com.example.myprofile2.R
import androidx.compose.ui.input.key.Key.Companion.R

@Composable
fun ProfilScreen(onNavigateToFilmScreen: () -> Unit) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    Screen(windowSizeClass, onNavigateToFilmScreen)
}


@Composable
fun Screen(classes: WindowSizeClass, onNavigateToFilmScreen: () -> Unit) {
    val classeLargeur = classes.windowWidthSizeClass
    when (classeLargeur) {
        WindowWidthSizeClass.COMPACT -> {
            CompactLayout(onNavigateToFilmScreen)
        }
        else ->  {
            MediumLayout(onNavigateToFilmScreen)
        }

    }
}



@Composable
fun CompactLayout(onNavigateToFilmScreen: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center // Centre le bouton sur le côté droit
            ) {
                StartButton(onClick = {
                    onNavigateToFilmScreen()
                })
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
            Greeting(name = "Android")
            Spacer(modifier = Modifier.height(16.dp))
            ContentBelowImage()
            Spacer(modifier = Modifier.height(50.dp))
            AllIcons()
        }
    }
}


@Composable
fun MediumLayout(onNavigateToFilmScreen: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Colonne de gauche : le reste du contenu
            Column(
                modifier = Modifier
                    .weight(1f) // Remplit l'espace à gauche
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Greeting(name = "Android")
                Spacer(modifier = Modifier.height(16.dp))
                ContentBelowImage()
            }

            // Colonne de droite : Profil, icônes et bouton
            Column(
                modifier = Modifier
                    .weight(1f) // Remplit l'espace à droite
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween, // Espace entre les éléments
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f), // Prend tout l'espace restant
                    verticalArrangement = Arrangement.Center, // Centre les icônes verticalement
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AllIcons()
                }
                // Afficher le bouton en bas
                StartButton(onClick = {
                    onNavigateToFilmScreen()                })
            }
        }
    }
}




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    CostumImage(
        imageResId = R.drawable.photo,
        contentDescription = "icon",
        modifier = Modifier.padding(bottom = 16.dp)
            .size(200.dp)
    )
}

@Composable
fun CostumImage(imageResId: Int, contentDescription: String, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = imageResId),
        contentDescription = contentDescription,
        modifier = modifier
            .size(100.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ContentBelowImage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Sara Sarkissian",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Etudiante Alternante E-Santé",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Ecole d'ingénieur ISIS",
            style = MaterialTheme.typography.bodyLarge
        )

    }
}
@Composable
fun AllIcons(){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InfoRow(
            icon = Icons.Default.Email,
            infoText = "dizida1@gmail.com"
        )
        InfoRow(
            painterResource = R.drawable.icon_linkedln,
            infoText = "www.linkedin.com/in"
        )
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector? = null, painterResource: Int? = null, infoText: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        } else if (painterResource != null) {
            Image(
                painter = painterResource(id = painterResource),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = infoText, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun StartButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB6C1))
    ) {
        Text("Démarrer", style = MaterialTheme.typography.titleMedium)
    }
}


