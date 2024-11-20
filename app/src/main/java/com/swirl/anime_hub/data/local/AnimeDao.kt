package com.swirl.anime_hub.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.swirl.anime_hub.data.model.Anime
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(animeList: List<Anime>)

    @Query("SELECT * FROM anime_table")
    fun getAllAnime(): Flow<List<Anime>>

}