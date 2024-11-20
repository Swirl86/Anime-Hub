package com.swirl.anime_hub.ui.screens.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swirl.anime_hub.ui.screens.common.LoadingScreen
import com.swirl.anime_hub.viewmodel.AnimeViewModel

@Composable
fun AnimeListScreen(
    viewModel: AnimeViewModel = hiltViewModel(),
    onAnimeSelected: (Int) -> Unit
) {
    val animeList by viewModel.animeList.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAnimeList()
    }

    if (animeList.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 12.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(animeList) { anime ->
                AnimeItem(
                    anime = anime,
                    onClick = { onAnimeSelected(anime.malId)  }
                )
            }
        }
    } else if (errorState != null) {
        Text(text = "Error: ${errorState!!.message}", color = Color.Red)
    } else {
        LoadingScreen()
    }
}

