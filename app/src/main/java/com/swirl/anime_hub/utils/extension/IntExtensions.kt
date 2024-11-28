package com.swirl.anime_hub.utils.extension

fun Int.getStatusMessage(): String {
    return when (this) {
        404 -> "Not Found"
        401 -> "Unauthorized"
        403 -> "Forbidden"
        500 -> "Internal Server Error"
        else -> "Network Error"
    }
}