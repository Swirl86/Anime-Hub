package com.swirl.anime_hub.data.remote

import com.swirl.anime_hub.data.remote.response.AnimeDetailsResponse
import com.swirl.anime_hub.data.remote.response.AnimeListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApiService {

    /**
     * All requests return a ETag header which is an MD5 hash of the response.
     * You can use this hash to verify if there's new or updated content by supplying it as the
     * value for the If-None-Match in your next request header.
     *
     * For more information, visit the Jikan API documentation:
     * https://docs.api.jikan.moe/#section/Information/Caching
     */
    @GET("anime")
    suspend fun fetchAnimeList(
        @Query("page") page: Int,
        @Header("If-None-Match") etag: String? = null
    ): Response<AnimeListResponse>

    @GET("anime/{id}")
    suspend fun fetchAnimeDetails(@Path("id") animeId: Int): Response<AnimeDetailsResponse>
}
