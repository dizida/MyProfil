package com.example.myprofile2

import java.text.SimpleDateFormat
import java.util.Locale

// Fonction utilitaire pour formater une date de sortie de film
fun formatReleaseDate(dateString: String): String {
    // Définition du format d'entrée de la date (format original)
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    // Définition du format de sortie de la date (format désiré)
    val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    // Conversion de la chaîne de caractères en objet Date
    val date = inputFormat.parse(dateString)
    // Retourne la date formatée ou la chaîne d'origine si la conversion échoue
    return date?.let { outputFormat.format(it) } ?: dateString
}