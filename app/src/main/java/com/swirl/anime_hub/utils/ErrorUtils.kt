package com.swirl.anime_hub.utils

import com.swirl.anime_hub.data.remote.response.ErrorResponse

fun <T> getErrorResponse(error: Resource.Error<T>): ErrorResponse {
    val statusCode = error.code ?: 500
    return ErrorResponse(
        status = statusCode,
        type = error.errorType,
        message = error.message ?: "Unknown error occurred.",
        error = error.message ?: "Unknown error"
    )
}