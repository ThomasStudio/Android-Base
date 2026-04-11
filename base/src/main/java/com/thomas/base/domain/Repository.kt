package com.thomas.base.domain

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
}