package com.swirl.anime_hub.ui.screens.favorite

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swirl.anime_hub.viewmodel.FavoriteAnimeViewModel

@Composable
fun FavoriteAnimeScreen(
    viewModel: FavoriteAnimeViewModel = hiltViewModel(),
    onAnimeClick: (Int) -> Unit
) {
    val favoriteAnimeList by viewModel.favoriteAnimeList.collectAsState()
    val gridState = rememberLazyGridState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }

    errorState?.let {
        Text(text = "Error: $it", color = Color.Red)
    }

    if (favoriteAnimeList.isEmpty()) {
        EmptyFavorite()
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            state = gridState
        ) {
            items(favoriteAnimeList) { anime ->
                FavoriteAnimeCard(
                    anime = anime,
                    onClick = { onAnimeClick(anime.malId) },
                    onRemove = { viewModel.removeFavorite(anime) }
                )
            }
        }
    }
}