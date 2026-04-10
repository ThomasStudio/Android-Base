package com.thomas.base.viewmodel

/**
 * Created by thomas on 4/11/2026.
 */

data class UIState<DATA>(
    override val status: Status = Status.LOADING,
    override val data: DATA? = null,
    override val error: Error? = null
) : UIStateIF


interface UIStateIF {
    val status: Status
    val data: Any?
    val error: Error?
}

enum class Status {
    LOADING, SUCCESS, ERROR
}


open class Error(val code: Int = -1, val message: String = "")
