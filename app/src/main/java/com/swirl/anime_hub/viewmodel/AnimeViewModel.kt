package com.swirl.anime_hub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirl.anime_hub.data.model.Anime
import com.swirl.anime_hub.data.model.AnimeDetails
import com.swirl.anime_hub.data.model.FetchType
import com.swirl.anime_hub.data.remote.response.ErrorResponse
import com.swirl.anime_hub.data.repository.AnimeRepository
import com.swirl.anime_hub.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository
) : ViewModel() {
    private val currentPages = mutableMapOf<FetchType, Int>(
        FetchType.AnimeList to 1,
        FetchType.TopAnime to 1
    )

    private val _hasNextPage = MutableStateFlow(true)
    val hasNextPage: StateFlow<Boolean> = _hasNextPage

    private val _animeList = MutableStateFlow<List<Anime>>(emptyList())
    val animeList: StateFlow<List<Anime>> = _animeList

    private val _animeDetails = MutableStateFlow<AnimeDetails?>(null)
    val animeDetails: StateFlow<AnimeDetails?> get() = _animeDetails

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorState = MutableStateFlow<ErrorResponse?>(null)
    val errorState: StateFlow<ErrorResponse?> = _errorState

    // Reusable method to handle Resource.Error cases
    private fun <T> handleError(error: Resource.Error<T>) {
        val statusCode = error.code ?: 500
        _errorState.value = ErrorResponse(
            status = statusCode,
            type = error.errorType,
            message = error.message ?: "Unknown error occurred.",
            error = error.message ?: "Unknown error"
        )
    }

    fun fetchAnimeList(fetchType: FetchType) {
        // Prevent multiple API calls if already loading or no more pages
        if (_isLoading.value || !_hasNextPage.value) return

        val page = currentPages.getOrDefault(fetchType, 1)

        if (_animeList.value.isEmpty()) {
            currentPages[fetchType] = 1
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.fetchAnimeList(page, fetchType)
                when (result) {
                    is Resource.Success -> {
                        val newAnimeList = result.data?.data ?: emptyList()
                        val currentList = _animeList.value.toMutableList()
                        currentList.addAll(newAnimeList)
                        _animeList.value = currentList

                        result.data?.pagination?.let { pagination ->
                            currentPages[fetchType] = pagination.currentPage + 1
                            _hasNextPage.value = pagination.hasNextPage
                        }
                    }
                    is Resource.Error -> handleError(result)
                }
            } catch (e: Exception) {
                _errorState.value = ErrorResponse(error = e.message ?: "Unknown error")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getAnimeDetails(id: Int) {
        viewModelScope.launch {
            try {
                when (val result = repository.fetchAnimeDetails(id)) {
                    is Resource.Success -> {
                        result.data?.let { animeDetails ->
                            _animeDetails.value = animeDetails
                        } ?: run {
                            _errorState.value = ErrorResponse(
                                status = 500,
                                type = "Data Error",
                                message = "Failed to fetch anime details.",
                                error = "No data returned from the API"
                            )
                        }
                    }
                    is Resource.Error -> handleError(result)
                }
            } catch (e: Exception) {
                _errorState.value = ErrorResponse(error = e.message ?: "Unknown error")
            }
        }
    }
}
