package com.swirl.anime_hub.data.repository

import android.util.Log
import com.google.gson.Gson
import com.swirl.anime_hub.data.local.AnimeDao
import com.swirl.anime_hub.data.model.Anime
import com.swirl.anime_hub.data.remote.JikanApiService
import com.swirl.anime_hub.data.response.ErrorResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val animeDao: AnimeDao,
    private val apiService: JikanApiService
) {
    val animeList: Flow<List<Anime>> = animeDao.getAllAnime()

    suspend fun fetchAndSaveAnime(page: Int = 1, etag: String? = null) {
        try {
            val response = apiService.fetchAnimeListWithHeaders(page, etag)
            if (response.isSuccessful && response.body() != null) {
                val animeList = response.body()!!.data.map { it.toAnime() }
                animeDao.insertAll(animeList)
            } else {
                val errorResponse = parseErrorResponse(response)
                Log.e("Error:", errorResponse.message)
            }
        } catch (e: Exception) {
            Log.e("Error: ", e.message ?: "Unknown error")
        }
    }

    private fun parseErrorResponse(response: Response<*>): ErrorResponse {
        val statusCode = response.code()
        val errorBody = response.errorBody()?.string() ?: "No error message available"

        return try {
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            errorResponse.copy(status = statusCode)
        } catch (e: Exception) {
            ErrorResponse(
                status = statusCode,
                type = "Unknown",
                message = "An unknown error occurred.",
                error = e.message ?: "No error details",
                reportUrl = ""
            )
        }
    }
}

