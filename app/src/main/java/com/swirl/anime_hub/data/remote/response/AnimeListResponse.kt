package com.swirl.anime_hub.data.remote.response

import com.swirl.anime_hub.data.model.Anime
import com.swirl.anime_hub.data.model.Pagination

data class AnimeListResponse(
    val data: List<Anime>,
    val pagination: Pagination
)

