package com.swirl.anime_hub.data.model

import com.google.gson.annotations.SerializedName

data class Anime(
    @SerializedName("mal_id") val malId: Int,
    val title: String,
    val synopsis: String?,
    @SerializedName("images") val images: AnimeImages,
    val episodes: Int?,
    val score: Double?,
    val airing: Boolean,
    val url: String
)
