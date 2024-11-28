package com.swirl.anime_hub.data.model

import com.google.gson.annotations.SerializedName

data class AnimeDetails(
    @SerializedName("mal_id") val malId: Int,
    val title: String,
    @SerializedName("images") val images: AnimeImages,
    val synopsis: String?,
    val episodes: Int?,
    val score: Double?,
    @SerializedName("aired") val aired: Aired?,
    val url: String,
    @SerializedName("trailer") val trailer: Trailer?,
    val startDate: String?,
    val endDate: String?,
    val background: String?,
    val genres: List<Genre>?
)

data class Genre(
    val name: String
)

data class Trailer(
    @SerializedName("youtube_id") val youtubeId: String?,
    val url: String?,
    val embedUrl: String?,
    val images: TrailerImages?
)

data class TrailerImages(
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("small_image_url") val smallImageUrl: String?,
    @SerializedName("medium_image_url") val mediumImageUrl: String?,
    @SerializedName("large_image_url") val largeImageUrl: String?,
    @SerializedName("maximum_image_url") val maximumImageUrl: String?
)

data class Aired(
    @SerializedName("from") val from: String?,
    @SerializedName("to") val to: String?
)
