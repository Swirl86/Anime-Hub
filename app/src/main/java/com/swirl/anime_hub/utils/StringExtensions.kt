package com.swirl.anime_hub.utils

import android.util.Log
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String?.toFormattedDate(): String {
    return try {
        if (this.isNullOrEmpty()) {
            return "N/A"
        }
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val date = LocalDate.parse(this, formatter)

        date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    } catch (e: Exception) {
        Log.e("Error: ", e.message ?: "Unknown error")
        "N/A"
    }
}