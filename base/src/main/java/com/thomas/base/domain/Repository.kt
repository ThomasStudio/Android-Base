package com.thomas.base.domain

import kotlin.map

/**
 * Created by thomas on 4/11/2026.
 */

abstract class Repository {
    protected open val errorHandler: ErrorHandler = ApiErrorHandler

    suspend fun <T> runCall(
        call: suspend () -> Response<T>
    ): Result<T> {
        return try {
            val response = call()

            if (response.isSuccess() && response.data != null) {
                Result.Success(response.data)
            } else {
                errorHandler.toError(response)
            }
        } catch (e: Exception) {
            errorHandler.toError(e)
        }
    }

    suspend fun <T, R> runLegacy(legacyCall: suspend () -> R, converter: (R) -> Response<T>) =
        runCall { converter(legacyCall()) }

}