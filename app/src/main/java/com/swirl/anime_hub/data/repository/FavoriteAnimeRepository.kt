package com.swirl.anime_hub.data.repository

import com.swirl.anime_hub.data.local.FavoriteAnimeDao
import com.swirl.anime_hub.data.model.FavoriteAnime
import javax.inject.Inject

class FavoriteAnimeRepository @Inject constructor(
    private val favoriteAnimeDao: FavoriteAnimeDao
) {

    suspend fun getAllFavorites(): List<FavoriteAnime> {
        return favoriteAnimeDao.getAllFavorites()
    }

    suspend fun getFavoriteById(malId: Int): FavoriteAnime? {
        return favoriteAnimeDao.getFavoriteById(malId)
    }

    suspend fun insertFavorite(anime: FavoriteAnime) {
        favoriteAnimeDao.insertFavorite(anime)
    }

    suspend fun updateFavorite(anime: FavoriteAnime) {
        favoriteAnimeDao.updateFavorite(anime)
    }

    suspend fun deleteFavorite(anime: FavoriteAnime) {
        favoriteAnimeDao.deleteFavorite(anime)
    }
}
