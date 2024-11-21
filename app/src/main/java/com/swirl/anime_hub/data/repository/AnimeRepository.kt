package com.swirl.anime_hub.data.repository

import android.util.Log
import com.google.gson.Gson
import com.swirl.anime_hub.data.model.AnimeDetails
import com.swirl.anime_hub.data.remote.JikanApiService
import com.swirl.anime_hub.data.response.AnimeListResponse
import com.swirl.anime_hub.data.response.ErrorResponse
import com.swirl.anime_hub.data.response.Pagination
import retrofit2.Response
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val apiService: JikanApiService
) {
    private var currentEtag: String? = null

    suspend fun fetchAnimeList(page: Int): AnimeListResponse {
        try {
            val response = apiService.fetchAnimeList(page, currentEtag)

            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    currentEtag = response.headers()["ETag"]
                    return apiResponse
                }
            } else if (response.code() == 304) {
                Log.d("AnimeRepository", "Data not modified, using cache")
            } else {
                throw Exception("Failed to fetch data: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            throw Exception("Error fetching anime list: ${e.message}")
        }

        return AnimeListResponse(emptyList(), Pagination(1, false, 1))
    }

    suspend fun fetchAnimeDetails(animeId: Int): AnimeDetails? {
        return try {
            val response = apiService.fetchAnimeDetails(animeId)
            if (response.isSuccessful) {
                response.body()?.data
            } else {
                val errorResponse = parseErrorResponse(response)
                Log.e("Error:", errorResponse.message)
                null
            }
        } catch (e: Exception) {
            Log.e("Error: ", e.message ?: "Unknown error")
            null
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

