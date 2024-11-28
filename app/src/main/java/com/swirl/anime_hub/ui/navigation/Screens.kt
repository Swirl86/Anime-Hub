package com.swirl.anime_hub.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.swirl.anime_hub.data.model.FetchType

sealed class Screens(var route: String, var icon: ImageVector, var title: String, val fetchType: FetchType? = null) {
    data object AnimeList : Screens("anime_list", Icons.Default.Home, "Anime Hub", FetchType.AnimeList)
    data object TopAnime : Screens("top_anime", Icons.Default.Star, "Top Anime", FetchType.TopAnime)
    data object AnimeDetail : Screens("anime_detail/{animeId}", Icons.Default.Home, "Anime Details") {
        fun createRoute(animeId: Int) = "anime_detail/$animeId"
    }
}