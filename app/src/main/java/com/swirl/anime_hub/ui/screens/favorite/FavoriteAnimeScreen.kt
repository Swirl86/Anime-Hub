package com.swirl.anime_hub.ui.screens.favorite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
    val listState = rememberLazyListState()
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
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(favoriteAnimeList) { anime ->
                FavoriteAnimeItem(
                    anime = anime,
                    onClick = { onAnimeClick(anime.malId) },
                    onRemove = { viewModel.removeFavorite(anime) }
                )
            }
        }
    }
}