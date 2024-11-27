package com.swirl.anime_hub.ui.navigation

import androidx.navigation.NavController

/**
 * Navigation actions for the app.
 * */
object NavigationActions {
    fun navigateToAnimeDetail(navController: NavController, animeId: Int) {
        navController.navigate(Screens.AnimeDetail.createRoute(animeId))
    }

    fun navigateToAnimeList(navController: NavController) {
        navController.navigate(Screens.AnimeList.route) {
            popUpTo(Screens.AnimeList.route) { inclusive = true }
        }
    }
}