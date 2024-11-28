package com.swirl.anime_hub.data.remote.response

data class ErrorResponse(
    val status: Int = 500,
    val type: String = getDefaultType(status),
    val message: String = "An unknown error occurred.",
    val error: String? ="Unknown error"
) {
    // Dynamically determine the type based on the status code
    companion object {
        fun getDefaultType(statusCode: Int): String {
            return when (statusCode) {
                in 400..499 -> "Client Error"
                in 500..599 -> "Server Error"
                else -> "Unknown Error"
            }
        }
    }
}