package com.swirl.anime_hub.utils

import com.google.gson.Gson
import com.swirl.anime_hub.data.remote.response.ErrorResponse
import retrofit2.Response

fun parseErrorResponse(response: Response<*>): ErrorResponse {
    val statusCode = response.code()
    val errorBody = response.errorBody()?.string() ?: "No error message available"

    return try {
        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
        errorResponse.copy(status = statusCode)
    } catch (e: Exception) {
        ErrorResponse(
            status = statusCode,
            error = e.message
        )
    }
}