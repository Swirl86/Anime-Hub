package com.swirl.anime_hub.data.model

import com.google.gson.annotations.SerializedName


data class AnimeImages(
    @SerializedName("jpg") val jpg: AnimeImageUrls
)

data class AnimeImageUrls(
    @SerializedName("image_url") val imageUrl: String
)