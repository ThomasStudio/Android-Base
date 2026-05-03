package com.thomas.base.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    open val initState = UIState<DATA>()
    override var initialized: Boolean = true

    // Use a controlled scope for event emissions instead of GlobalScope
    open val eventScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val _uiState by lazy { MutableStateFlow(initState) }
    override val uiState: StateFlow<UIState<DATA>>
        get() = _uiState

    private val _event = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    override val event: SharedFlow<Event> = _event.asSharedFlow()

    override fun back() = send(BackEvent)

    protected fun send(viewEvent: Event) {
        if (_event.tryEmit(viewEvent)) return

        // Emit from a local scope to avoid GlobalScope usage
        eventScope.launch { _event.emit(viewEvent) }
    }
}