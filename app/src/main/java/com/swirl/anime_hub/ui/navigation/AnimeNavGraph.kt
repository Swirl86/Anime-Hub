package com.swirl.anime_hub.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.swirl.anime_hub.ui.screens.details.AnimeDetailScreen
import com.swirl.anime_hub.ui.screens.list.AnimeListScreen

sealed class Screen(val route: String) {
    data object AnimeList : Screen("anime_list")
    data object AnimeDetail : Screen("anime_detail/{animeId}") {
        fun createRoute(animeId: Int) = "anime_detail/$animeId"
    }
}

@Composable
fun AnimeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.AnimeList.route
    ) {
        composable(Screen.AnimeList.route) {
            AnimeListScreen(
                viewModel = hiltViewModel(),
                onAnimeSelected = { animeId ->
                    // Navigate to the AnimeDetail screen when an anime item is clicked
                    navController.navigate(Screen.AnimeDetail.createRoute(animeId))
                }
            )
        }

        composable(
            route = Screen.AnimeDetail.route,
            arguments = listOf(navArgument("animeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt("animeId") ?: return@composable
            AnimeDetailScreen(animeId = animeId)
        }
    }
}

