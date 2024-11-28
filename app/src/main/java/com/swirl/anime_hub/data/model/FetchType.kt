package com.swirl.anime_hub.data.model

sealed class FetchType {
    object AnimeList : FetchType()
    object TopAnime : FetchType()
    //object RecentAnimeRecommendations : FetchType()
   // object RecentAnimeReviews : FetchType()
}