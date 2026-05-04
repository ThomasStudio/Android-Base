package com.thomas.base.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Contract for state-based ViewModels exposing minimal members the UI should observe/call.
 * S - state type; DATA - UI data type
 */
interface BaseStateContract<S, DATA> : BaseDataContract<DATA> {
    /**
     * Observable stream of current state values.
     */
    val state: StateFlow<S>

    /**
     * Synchronously read the current state value.
     */
    val currentState: S
}

abstract class DefaultStateContract<S, DATA> : DefaultContract<DATA>(), BaseStateContract<S, DATA> {
    abstract fun initialState(): S

    private val _state = MutableStateFlow(initialState())
    override val state: StateFlow<S>
        get() = _state
    override val currentState: S
        get() = _state.value

    protected open fun canTransition(from: S, to: S): Boolean = true

    protected open fun transitionTo(newState: S): Boolean {
        val old = _state.value
        if (old == newState) return false
        if (!canTransition(old, newState)) return false

        _state.value = newState
        onStateChanged(newState, old)
        return true
    }

    protected open fun onStateChanged(newState: S, oldState: S) {}

}