package com.swirl.anime_hub.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.swirl.anime_hub.data.model.FavoriteAnime

@Dao
interface FavoriteAnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(anime: FavoriteAnime)

    @Update
    suspend fun updateFavorite(anime: FavoriteAnime)

    @Delete
    suspend fun deleteFavorite(anime: FavoriteAnime)

    @Query("SELECT * FROM favorite_anime")
    suspend fun getAllFavorites(): List<FavoriteAnime>

    @Query("SELECT * FROM favorite_anime WHERE malId = :malId LIMIT 1")
    suspend fun getFavoriteById(malId: Int): FavoriteAnime?
}
