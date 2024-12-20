package com.swirl.anime_hub.ui.screens.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swirl.anime_hub.data.model.FetchType
import com.swirl.anime_hub.ui.screens.common.LoadingScreen
import com.swirl.anime_hub.viewmodel.AnimeViewModel

@Composable
fun AnimeListScreen(
    viewModel: AnimeViewModel = hiltViewModel(),
    fetchType: FetchType,
    onAnimeSelected: (Int) -> Unit
) {
    val animeList by viewModel.getAnimeList(fetchType).collectAsState()
    val favoriteAnimeList by viewModel.favoriteAnimeList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorState by viewModel.errorState.collectAsState()
    val hasNextPage by viewModel.hasNextPage.collectAsState()
    val listState = rememberLazyListState()

    // Fetch anime list on initial load or when fetchType changes
    LaunchedEffect(fetchType) {
        viewModel.fetchAnimeList(fetchType)
    }
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { newIndex ->
                val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                // Check if we're at the bottom of the list
                if (lastVisibleItem != null && lastVisibleItem.index == animeList.size - 1 && hasNextPage) {
                    // Trigger loading the next page
                    viewModel.fetchAnimeList(fetchType)
                }
            }
    }

    when {
        animeList.isNotEmpty() -> {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp).padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(animeList) { anime ->
                    val isFavorite = favoriteAnimeList.any { it.malId == anime.malId }
                    AnimeItem(
                        anime = anime,
                        isFavorite = isFavorite,
                        onClick = { onAnimeSelected(anime.malId) },
                        onAddToFavorite = { viewModel.addToFavorites(it.malId) },
                        onRemoveFromFavorite = { viewModel.removeFromFavorites(it.malId) }
                    )
                }

                // Show loading indicator at the end if loading
                if (isLoading) {
                    item {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
        errorState != null -> {
            Text(text = "Error: ${errorState!!.message}", color = Color.Red)
        }
        else -> {
            LoadingScreen()
        }
    }
}
