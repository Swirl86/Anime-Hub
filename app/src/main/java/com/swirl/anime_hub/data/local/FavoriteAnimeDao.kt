package com.swirl.anime_hub.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.swirl.anime_hub.data.model.FavoriteAnime
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteAnime: List<FavoriteAnime>)

    @Query("SELECT * FROM favorite_anime_table")
    fun getAllFavoriteAnime(): Flow<List<FavoriteAnime>>

}