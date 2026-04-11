package com.thomas.base.domain

import com.google.gson.annotations.SerializedName

/**
 * Created by thomas on 4/11/2026.
 */

open class Response<T>(
    @SerializedName("code") val code: Int,
    @SerializedName("msg") val message: String?,
    @SerializedName("data") val data: T?
) {
    open fun isSuccess(): Boolean = code == 200
}