package com.swirl.anime_hub.data.remote.response

import com.google.gson.annotations.SerializedName
import com.swirl.anime_hub.data.model.Anime

data class AnimeListResponse(
    val data: List<Anime>,
    val pagination: Pagination
)

data class Pagination(
    @SerializedName("last_visible_page") val lastVisiblePage: Int,
    @SerializedName("has_next_page") val hasNextPage: Boolean,
    @SerializedName("current_page") val currentPage: Int
)
