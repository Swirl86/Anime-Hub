package com.swirl.anime_hub.data.repository

import android.util.Log
import com.google.gson.Gson
import com.swirl.anime_hub.data.model.AnimeDetails
import com.swirl.anime_hub.data.remote.JikanApiService
import com.swirl.anime_hub.data.remote.response.AnimeListResponse
import com.swirl.anime_hub.data.remote.response.ErrorResponse
import com.swirl.anime_hub.data.remote.response.Pagination
import com.swirl.anime_hub.utils.Resource
import com.swirl.anime_hub.utils.parseErrorResponse
import retrofit2.Response
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val apiService: JikanApiService
) {
    private var currentEtag: String? = null

    suspend fun fetchAnimeList(page: Int): Resource<AnimeListResponse> {
        return try {
            val response = apiService.fetchAnimeList(page, currentEtag)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    currentEtag = response.headers()["ETag"]
                    Resource.Success(apiResponse)
                } ?: Resource.Error("API response body is null")
            } else if (response.code() == 304) {
                Log.d("AnimeRepository", "Data not modified, using cache")
                // TODO test this more
                Resource.Success(AnimeListResponse(emptyList(), Pagination(1, false, 1)))
            } else {
                Resource.Error(
                    message = "Failed to fetch data: ${response.errorBody()?.string()}",
                    code = response.code()
                )
            }
        } catch (e: Exception) {
            Resource.Error("Error fetching anime list: ${e.message}")
        }
    }

    suspend fun fetchAnimeDetails(animeId: Int): Resource<AnimeDetails> {
        return try {
            val response = apiService.fetchAnimeDetails(animeId)
            if (response.isSuccessful) {
                response.body()?.data?.let { details ->
                    Resource.Success(details)
                } ?: Resource.Error("API response body is null", code = response.code())
            } else {
                val errorResponse = parseErrorResponse(response)
                Resource.Error(
                    message = errorResponse.message ?: "Failed to fetch anime details",
                    code = response.code()
                )
            }
        } catch (e: Exception) {
            Resource.Error("Error fetching anime details: ${e.message}")
        }
    }
}

