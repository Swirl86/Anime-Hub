package com.swirl.anime_hub.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(var route: String, var icon: ImageVector, var title: String) {
    data object AnimeList : Screens("anime_list", Icons.Default.Home, "Anime Hub")
    data object TopAnime : Screens("top_anime", Icons.Default.Star, "Top Anime")
    data object FavoriteAnime : Screens("favorite_anime", Icons.Default.Favorite, "Favorite Anime")
    data object AnimeDetail : Screens("anime_detail/{animeId}", Icons.Default.Home, "Anime Details") {
        fun createRoute(animeId: Int) = "anime_detail/$animeId"
    }
}