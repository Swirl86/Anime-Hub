package com.swirl.anime_hub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_anime_table")
data class FavoriteAnime (
    @PrimaryKey val malId: Int,
    val title: String
)