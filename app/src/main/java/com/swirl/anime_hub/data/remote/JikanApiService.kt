package com.swirl.anime_hub.data.remote

import com.swirl.anime_hub.data.response.AnimeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface JikanApiService {

    @GET("anime")
    suspend fun fetchAnimeListWithHeaders(
        @Query("page") page: Int = 1,
        @Header("If-None-Match") etag: String? = null
    ): Response<AnimeResponse>

}
