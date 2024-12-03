package com.swirl.anime_hub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirl.anime_hub.data.model.Anime
import com.swirl.anime_hub.data.model.AnimeDetails
import com.swirl.anime_hub.data.model.FavoriteAnime
import com.swirl.anime_hub.data.model.FetchType
import com.swirl.anime_hub.data.remote.response.ErrorResponse
import com.swirl.anime_hub.data.repository.AnimeRepository
import com.swirl.anime_hub.data.repository.FavoriteAnimeRepository
import com.swirl.anime_hub.utils.Resource
import com.swirl.anime_hub.utils.getErrorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository,
    private val favoriteAnimeRepository: FavoriteAnimeRepository
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

    private val _lastFetchType = MutableStateFlow<FetchType?>(null)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorState = MutableStateFlow<ErrorResponse?>(null)
    val errorState: StateFlow<ErrorResponse?> = _errorState

    private fun <T> handleError(error: Resource.Error<T>) {
        _errorState.value = getErrorResponse(error)
    }

    private val _favoriteAnimeList = MutableStateFlow<List<FavoriteAnime>>(emptyList())
    val favoriteAnimeList: StateFlow<List<FavoriteAnime>> = _favoriteAnimeList

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _favoriteAnimeList.value = favoriteAnimeRepository.getAllFavorites()
        }
    }

    fun fetchAnimeList(fetchType: FetchType) {
        // Prevent multiple API calls if already loading or no more pages
        if (fetchType == _lastFetchType.value || _isLoading.value || !_hasNextPage.value) return

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
                        _lastFetchType.value = fetchType
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

    fun addToFavorites(malId: Int) {
        viewModelScope.launch {
            val anime = _animeList.value.find { it.malId == malId }
            anime?.let {
                val favoriteAnime = FavoriteAnime(
                    malId = anime.malId,
                    title = anime.title,
                    imageUrl = anime.images.jpg.imageUrl
                )
                favoriteAnimeRepository.insertFavorite(favoriteAnime)
                loadFavorites()
            }
        }
    }

    fun removeFromFavorites(malId: Int) {
        viewModelScope.launch {
            val anime = _animeList.value.find { it.malId == malId }
            anime?.let {
                val favoriteAnime = FavoriteAnime(
                    malId = anime.malId,
                    title = anime.title,
                    imageUrl = anime.images.jpg.imageUrl
                )
                favoriteAnimeRepository.deleteFavorite(favoriteAnime)
                loadFavorites()
            }
        }
    }
}
