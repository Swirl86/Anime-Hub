package com.swirl.anime_hub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirl.anime_hub.data.model.Anime
import com.swirl.anime_hub.data.repository.AnimeRepository
import com.swirl.anime_hub.data.response.ErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _animeList = MutableStateFlow<List<Anime>>(emptyList())
    val animeList: StateFlow<List<Anime>> = _animeList

    private val _errorState = MutableStateFlow<ErrorResponse?>(null)
    val errorState: StateFlow<ErrorResponse?> = _errorState

    fun fetchAnimeList(page: Int = 1, etag: String? = null) {
        viewModelScope.launch {
            try {
                repository.fetchAndSaveAnime(page, etag)

                repository.animeList.collect { list ->
                    _animeList.value = list
                }
            } catch (e: Exception) {
                _errorState.value = ErrorResponse(
                    status = 500,
                    type = "Network Error",
                    message = "Failed to fetch anime list.",
                    error = e.message ?: "Unknown error",
                    reportUrl = ""
                )
                Log.e("Error: ", e.message ?: "Unknown error")
            }
        }
    }
}
