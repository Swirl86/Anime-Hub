package com.swirl.anime_hub.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.swirl.anime_hub.data.model.FetchType
import com.swirl.anime_hub.ui.screens.details.AnimeDetailScreen
import com.swirl.anime_hub.ui.screens.favorite.FavoriteAnimeScreen
import com.swirl.anime_hub.ui.screens.list.AnimeListScreen
import com.swirl.anime_hub.viewmodel.AnimeViewModel
import com.swirl.anime_hub.viewmodel.FavoriteAnimeViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val animeViewModel: AnimeViewModel = hiltViewModel()
    val favViewModel: FavoriteAnimeViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screens.AnimeList.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screens.AnimeList.route) {
            AnimeListScreen(
                viewModel = animeViewModel,
                fetchType = FetchType.AnimeList,
                onAnimeSelected = { animeId ->
                    NavigationActions.navigateToAnimeDetail(navController, animeId)
                }
            )
        }

        composable(Screens.TopAnime.route) {
            AnimeListScreen(
                viewModel = animeViewModel,
                fetchType = FetchType.TopAnime,
                onAnimeSelected = { animeId ->
                    NavigationActions.navigateToAnimeDetail(navController, animeId)
                }
            )
        }

        composable(Screens.FavoriteAnime.route) {
            FavoriteAnimeScreen(
                viewModel = favViewModel,
                onAnimeClick = { animeId ->
                    NavigationActions.navigateToAnimeDetail(navController, animeId)
                }
            )
        }

        composable(
            route = Screens.AnimeDetail.route,
            arguments = listOf(navArgument("animeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt("animeId") ?: return@composable
            AnimeDetailScreen(
                viewModel = animeViewModel,
                animeId = animeId
            )
        }
    }
}