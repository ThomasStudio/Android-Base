package com.thomas.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thomas.base.navigation.AppRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by thomas on 4/11/2026.
 */

abstract class BaseViewModel<STATE : UIStateIF> : ViewModel(), BaseContract<STATE> {
    override var initialized: Boolean = false
    protected abstract fun initialState(): STATE

    protected open val scope: CoroutineScope
        get() = viewModelScope

    private val _uiState by lazy { MutableStateFlow(initialState()) }
    override val uiState: StateFlow<STATE>
        get() = _uiState

    private val _event = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    override val event: SharedFlow<Event> = _event.asSharedFlow()

    override fun back() {
        send(BackEvent)
    }

    override fun onViewCreated(initCall: () -> Unit) {
        if (initialized) return

        initialized = true
        initCall.invoke()
    }

    protected fun updateState(state: STATE) {
        updateState { state }
    }

    protected fun updateState(reducer: STATE.() -> STATE) {
        _uiState.update(reducer)
    }

    protected fun send(viewEvent: Event) {
        if (_event.tryEmit(viewEvent)) return

        scope.launch { _event.emit(viewEvent) }
    }


    protected fun sendMessage(message: String) {
        send(MessageEvent(message))
    }

    protected fun navigate(
        route: String,
        popUpToRoute: String? = null,
        inclusive: Boolean = false,
        launchSingleTop: Boolean = false
    ) {
        send(NavigateEvent(route, popUpToRoute, inclusive, launchSingleTop))
    }

    protected fun navigate(
        route: AppRoute,
        popUpToRoute: AppRoute? = null,
        inclusive: Boolean = false,
        launchSingleTop: Boolean = false
    ) {
        send(NavigateEvent(route.path, popUpToRoute?.path, inclusive, launchSingleTop))
    }

}
