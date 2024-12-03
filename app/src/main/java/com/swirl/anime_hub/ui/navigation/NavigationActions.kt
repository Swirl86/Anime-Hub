package com.swirl.anime_hub.ui.navigation

import androidx.navigation.NavController

/**
 * Navigation actions for the app.
 * */
object NavigationActions {
    fun navigateToAnimeDetail(navController: NavController, animeId: Int) {
        navController.navigate(Screens.AnimeDetail.createRoute(animeId))
    }

    fun navigateToAnimeList(navController: NavController, screen: Screens) {
        if (screen == Screens.AnimeList) {
            navController.navigate(screen.route) {
                popUpTo(Screens.AnimeList.route) { inclusive = false }
                launchSingleTop = true
            }
        } else {
            navController.navigate(screen.route)
        }
    }

    fun navigateToFavorites(navController: NavController) {
        navController.navigate(Screens.FavoriteAnime.route) {
            launchSingleTop = true
        }
    }

    /**
     * Navigate to another screen based on the provided route and arguments.
     */
    /*fun navigateToScreen(navController: NavController, screen: Screens, vararg args: Pair<String, String>) {
        val route = if (args.isNotEmpty()) {
            screen.createRoute(*args)
        } else {
            screen.route
        }

        navController.navigate(route) {
            launchSingleTop = true
        }
    }*/
}