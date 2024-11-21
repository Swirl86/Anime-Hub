package com.swirl.anime_hub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirl.anime_hub.data.model.Anime
import com.swirl.anime_hub.data.model.AnimeDetails
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
    private var currentPage = 1

    private var _hasNextPage = MutableStateFlow(true)
    val hasNextPage: StateFlow<Boolean> = _hasNextPage

    private val _animeList = MutableStateFlow<List<Anime>>(emptyList())
    val animeList: StateFlow<List<Anime>> = _animeList

    private val _animeDetails = MutableStateFlow<AnimeDetails?>(null)
    val animeDetails: StateFlow<AnimeDetails?> get() = _animeDetails

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorState = MutableStateFlow<ErrorResponse?>(null)
    val errorState: StateFlow<ErrorResponse?> = _errorState

    fun fetchAnimeList() {
        // Prevent multiple API calls if already loading or no more pages
        if (_isLoading.value || !_hasNextPage.value) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val apiResponse = repository.fetchAnimeList(currentPage)

                val newAnimeList = apiResponse.data.map { it }
                val currentList = _animeList.value.toMutableList()
                currentList.addAll(newAnimeList)
                _animeList.value = currentList

                // Update page number and whether there are more pages
                currentPage = apiResponse.pagination.currentPage + 1
                _hasNextPage.value = apiResponse.pagination.hasNextPage

            } catch (e: Exception) {
                _errorState.value = ErrorResponse(
                    status = 500,
                    type = "Network Error",
                    message = "Failed to fetch anime list.",
                    error = e.message ?: "Unknown error",
                    reportUrl = ""
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getAnimeDetails(id: Int) {
        viewModelScope.launch {
            try {
                val animeDetails  = repository.fetchAnimeDetails(id)
                if (animeDetails  != null) {
                    _animeDetails.value = animeDetails
                } else {
                    _errorState.value = ErrorResponse(
                        status = 500,
                        type = "API Error",
                        message = "Failed to fetch anime details.",
                        error = "No data returned from the API",
                        reportUrl = ""
                    )
                }
            } catch (e: Exception) {
                _errorState.value = ErrorResponse(
                    status = 500,
                    type = "Network Error",
                    message = "Failed to fetch anime details.",
                    error = e.message ?: "Unknown error",
                    reportUrl = ""
                )
                Log.e("Error: ", e.message ?: "Unknown error")
            }
        }
    }
}
