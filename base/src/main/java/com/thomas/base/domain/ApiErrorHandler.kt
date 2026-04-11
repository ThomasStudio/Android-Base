package com.thomas.base.domain

import okio.IOException
import retrofit2.HttpException

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

    fun getErrorMessage(code: Int, message: String? = null): String {
        val errorMsg = message ?: ""

        return when (code) {
            SYSTEM_ERROR -> "$SYSTEM_ERROR_STR: $errorMsg"
            NETWORK_ERROR -> "$NETWORK_ERROR_STR: $errorMsg"
            NO_CONTENT_ERROR -> "$NO_CONTENT_ERROR_STR: $errorMsg"
            else -> "$ERROR_STR $code: ${message ?: UNKNOWN_ERROR_STR}"
        }
    }

    override fun <T> toError(response: Response<T>): Result.Error {
        return if (response.isSuccess() && response.data == null) {
            Result.Error(NO_CONTENT_ERROR, getErrorMessage(NO_CONTENT_ERROR, response.message))
        } else {
            Result.Error(response.code, response.message ?: BUSINESS_ERROR_STR)
        }
    }

    override fun toError(e: Exception) = when (e) {
        is HttpException -> Result.Error(
            e.code(),
            "$HTTP_ERROR_STR: ${e.response()?.errorBody()?.string() ?: e.message()}"
        )

        is IOException -> Result.Error(NETWORK_ERROR, getErrorMessage(NETWORK_ERROR, e.message))
        else -> Result.Error(SYSTEM_ERROR, getErrorMessage(SYSTEM_ERROR, e.message))
    }
}