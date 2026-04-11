package com.thomas.base.domain

/**
 * Created by thomas on 4/11/2026.
 */

interface ErrorHandler {
    fun <T> toError(response:Response<T>):Result.Error

    fun toError(e:Exception): Result.Error
}