package com.thomas.base.viewmodel

/**
 * Created by thomas on 4/11/2026.
 */

abstract class BaseDataViewModel<DATA> : BaseViewModel<UIState<DATA>>() {
    protected open fun initialData(): DATA? = null
    protected open fun initialStatus() = Status.LOADING
    protected open fun defaultData() = initialData()

    override fun initialState() = UIState(status = initialStatus(), data = initialData())

    protected fun updateData(reducer: DATA.() -> DATA) = updateState { toData(reducer) }
    protected fun updateData(data: DATA) = updateData { data }

    protected fun showLoading() = updateState { toLoading() }
    protected fun showError(code: Int = -1, message: String = "") =
        updateState { toError(code, message) }

    protected fun currentData() = uiState.value.data ?: defaultData()
}
