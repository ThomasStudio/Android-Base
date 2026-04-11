package com.thomas.base.domain

/**
 * Created by thomas on 4/11/2026.
 */

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val code: Int, val message: String?) : Result<Nothing>()
}