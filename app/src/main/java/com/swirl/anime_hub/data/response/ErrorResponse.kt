package com.swirl.anime_hub.data.response

data class ErrorResponse(
    val status: Int,
    val type: String,
    val message: String,
    val error: String,
    val reportUrl: String
)