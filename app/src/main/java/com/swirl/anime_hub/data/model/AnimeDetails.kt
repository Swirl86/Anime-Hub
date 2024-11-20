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
) {
    fun toAnimeDetails(): AnimeDetails {
        return AnimeDetails(
            malId = malId,
            title = title,
            images = images,
            synopsis = synopsis,
            episodes = episodes,
            score = score,
            aired = aired,
            url = url,
            trailer = trailer,
            startDate = aired?.from,
            endDate = aired?.to,
            background = background,
            genres = genres?.map { Genre(it.name) }
        )
    }
}

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
    @SerializedName("image_url") val imageUrl: String?
)

data class Aired(
    @SerializedName("from") val from: String?,
    @SerializedName("to") val to: String?
)
