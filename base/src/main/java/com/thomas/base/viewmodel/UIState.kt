package com.thomas.base.viewmodel

/**
 * Created by thomas on 4/11/2026.
 */

data class UIState<DATA>(
    override val status: Status = Status.LOADING,
    override val data: DATA? = null,
    override val error: Error? = null
) : UIStateIF {
    fun isLoading() = status == Status.LOADING
    fun isSuccess() = status == Status.SUCCESS
    fun isError() = status == Status.ERROR

    fun toLoading() = copy(status = Status.LOADING)
    fun toError(code: Int = -1, message: String = "") =
        copy(status = Status.ERROR, error = Error(code, message))

    fun toData(reducer: DATA.() -> DATA) =
        copy(status = Status.SUCCESS, data = data?.let(reducer), error = null)

    fun toData(data: DATA) = toData { data }
}



interface UIStateIF {
    val status: Status
    val data: Any?
    val error: Error?
}

enum class Status {
    LOADING, SUCCESS, ERROR
}


open class Error(val code: Int = -1, val message: String = "")
