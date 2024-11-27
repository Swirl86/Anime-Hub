package com.swirl.anime_hub.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.swirl.anime_hub.ui.screens.details.AnimeDetailScreen
import com.swirl.anime_hub.ui.screens.list.AnimeListScreen

@Composable
fun NavGraph(navController: NavHostController, paddingValues: PaddingValues) {

    NavHost(
        navController = navController,
        startDestination = Screens.AnimeList.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screens.AnimeList.route) {
            AnimeListScreen(
                onAnimeSelected = { animeId ->
                    NavigationActions.navigateToAnimeDetail(navController, animeId)
                }
            )
        }
        composable(
            route = Screens.AnimeDetail.route,
            arguments = listOf(navArgument("animeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt("animeId") ?: return@composable
            AnimeDetailScreen(animeId = animeId)
        }
    }
}