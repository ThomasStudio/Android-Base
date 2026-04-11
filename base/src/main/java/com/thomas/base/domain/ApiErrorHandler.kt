package com.thomas.base.domain

/**
 * Created by thomas on 4/11/2026.
 */

object ApiErrorHandler : ErrorHandler {
    const val SYSTEM_ERROR = -1
    const val NETWORK_ERROR = -2
    const val NO_CONTENT_ERROR = 204

    const val BUSINESS_ERROR_STR = "Business Error"
    const val HTTP_ERROR_STR = "HTTP Error"
    const val SYSTEM_ERROR_STR = "System Error"
    const val NETWORK_ERROR_STR = "Network Error"
    const val NO_CONTENT_ERROR_STR = "No Content found"
    const val UNKNOWN_ERROR_STR = "Unknown error"
    const val ERROR_STR = "Error"


    override fun <T> toError(response: Response<T>): Result.Error {
        return Result.Error(response.code, "API Error")
    }

    override fun toError(e: Exception): Result.Error {
        return Result.Error(-1, e.message ?: "Unknown error")
    }
}