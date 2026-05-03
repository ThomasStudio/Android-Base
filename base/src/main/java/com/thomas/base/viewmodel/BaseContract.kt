package com.thomas.base.viewmodel

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update

interface BaseContract<STATE : UIStateIF> {
    var initialized: Boolean

    val uiState: StateFlow<STATE>
    val event: SharedFlow<Event>

    // View init, call once when view created
    fun onViewInit() {}

    // call initCall only when first time view created, otherwise do nothing, call onViewInit default
    fun onViewCreated(initCall: () -> Unit = ::onViewInit) {
        if (initialized) return

        initialized = true
        initCall.invoke()
    }

    // View visible callback, call every time when view become visible, include first time
    fun onVisible() {}

    fun back()
}

interface BaseDataContract<DATA> : BaseContract<UIState<DATA>>

//for unit test and preview
open class DefaultContract<DATA> : BaseDataContract<DATA> {
    override var initialized: Boolean = true

    protected open fun initialData(): DATA? = null
    protected open fun initialStatus() = Status.SUCCESS
    protected fun initialState() = UIState(status = initialStatus(), data = initialData())
    override fun back() {}

    private val _uiState by lazy { MutableStateFlow(initialState()) }
    override val uiState: StateFlow<UIState<DATA>>
        get() = _uiState

    private val _event = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    override val event: SharedFlow<Event> = _event.asSharedFlow()

    protected fun updateData(data: DATA) =
        updateState { copy(status = Status.SUCCESS, data = data) }

    protected fun updateData(reducer: DATA.() -> DATA) = updateState { toData(reducer) }

    protected fun showLoading() = updateState { toLoading() }
    protected fun showError(code: Int = -1, message: String = "") =
        updateState { toError(code, message) }

    protected fun updateState(reducer: UIState<DATA>.() -> UIState<DATA>) {
        _uiState.update(reducer)
    }

}