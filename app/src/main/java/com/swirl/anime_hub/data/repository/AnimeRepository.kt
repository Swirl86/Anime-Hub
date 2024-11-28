package com.swirl.anime_hub.data.repository

import android.util.Log
import com.swirl.anime_hub.data.model.AnimeDetails
import com.swirl.anime_hub.data.model.FetchType
import com.swirl.anime_hub.data.model.Pagination
import com.swirl.anime_hub.data.remote.JikanApiService
import com.swirl.anime_hub.data.remote.response.AnimeListResponse
import com.swirl.anime_hub.utils.Resource
import com.swirl.anime_hub.utils.parseErrorResponse
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val apiService: JikanApiService
) {
    private var currentEtag: String? = null

    suspend fun fetchAnimeList(page: Int, fetchType: FetchType = FetchType.AnimeList): Resource<AnimeListResponse> {
        return try {
            val response = when (fetchType) {
                FetchType.AnimeList -> {
                    apiService.fetchAnimeList(page, currentEtag)
                }
                FetchType.TopAnime -> {
                    apiService.fetchTopAnime(page, currentEtag)
                }
            }

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
                val errorResponse = parseErrorResponse(response, "Failed to fetch anime details")
                Resource.Error(
                    message = errorResponse.message,
                    code = response.code()
                )
            }
        } catch (e: Exception) {
            Resource.Error("Error fetching anime details: ${e.message}")
        }
    }
}

