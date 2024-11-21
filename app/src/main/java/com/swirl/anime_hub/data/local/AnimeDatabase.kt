package com.swirl.anime_hub.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.swirl.anime_hub.data.model.FavoriteAnime

@Database(entities = [FavoriteAnime::class], version = 1, exportSchema = false)
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun favoriteAnimeDao(): FavoriteAnimeDao
}