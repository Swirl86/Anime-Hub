package com.swirl.anime_hub.ui.screens.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
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
        LazyColumn {
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
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    }
}

