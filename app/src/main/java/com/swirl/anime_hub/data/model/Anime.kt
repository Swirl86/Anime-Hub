package com.swirl.anime_hub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_table")
data class Anime(
    @PrimaryKey val malId: Int,
    val title: String,
    val synopsis: String?,
    val imageUrl: String,
    val episodes: Int?,
    val score: Double?,
    val airing: Boolean,
    val url: String
)
