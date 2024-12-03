package com.swirl.anime_hub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirl.anime_hub.data.model.FavoriteAnime
import com.swirl.anime_hub.data.repository.FavoriteAnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteAnimeViewModel @Inject constructor(
    private val favoriteAnimeRepository: FavoriteAnimeRepository
) : ViewModel() {

    private val _favoriteAnimeList = MutableStateFlow<List<FavoriteAnime>>(emptyList())
    val favoriteAnimeList: StateFlow<List<FavoriteAnime>> = _favoriteAnimeList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _favoriteAnimeList.value = favoriteAnimeRepository.getAllFavorites()
            } catch (e: Exception) {
                _errorState.value = "Failed to load favorites: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addFavorite(anime: FavoriteAnime) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                favoriteAnimeRepository.insertFavorite(anime)
                loadFavorites() // Re-load favorites after adding one
            } catch (e: Exception) {
                _errorState.value = "Failed to add favorite: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeFavorite(anime: FavoriteAnime) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                favoriteAnimeRepository.deleteFavorite(anime)
                loadFavorites() // Re-load favorites after removing one
            } catch (e: Exception) {
                _errorState.value = "Failed to remove favorite: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
