package com.swirl.anime_hub.utils

import com.swirl.anime_hub.utils.extension.getStatusMessage

sealed class Resource<T>(val data: T? = null, val message: String? = null, val code: Int? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null, code: Int? = null) : Resource<T>(data, message, code) {
        val errorType: String
            get() = code?.getStatusMessage() ?: "Unknown Error"
    }
}
