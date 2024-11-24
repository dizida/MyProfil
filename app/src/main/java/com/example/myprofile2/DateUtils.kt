
package com.example.myprofile2

import java.text.SimpleDateFormat
import java.util.Locale

fun formatReleaseDate(dateString: String?): String {
    if (dateString.isNullOrEmpty()) {
        return "Date inconnue"
    }
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date)
    } catch (e: Exception) {
        "Date inconnue"
    }
}