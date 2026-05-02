package com.thomas.base.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.thomas.base.domain.PayloadStore
import com.thomas.base.navigation.AppRoute

/**
 * Created by thomas on 4/11/2026.
 */

abstract class BaseDataViewModel<DATA>(val savedStateHandle: SavedStateHandle? = null) :
    BaseViewModel<UIState<DATA>>() {
    protected val store = PayloadStore

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

    // get saved state and payloadId
    protected inline fun <reified T> getSavedState(name: String): T? = savedStateHandle?.get(name)
    protected fun payloadId() = getSavedState<String>(AppRoute.PAYLOAD_ID)

    // payload management
    protected fun putPayload(data: Any) = store.put(data)
    protected fun putJsonPayload(vararg kvs: Pair<String, Any?>) = store.putJson(*kvs)
    protected inline fun <reified T> payload() = payloadId()?.let { store.consume<T>(it) }

    // navigation with payload
    protected fun navigateWithPayload(route: AppRoute, data: Any) =
        navigate(route.withId(putPayload(data)))

    protected fun navigateWithJson(route: AppRoute, vararg kvs: Pair<String, Any?>) =
        navigate(route.withId(putJsonPayload(*kvs)))


}
