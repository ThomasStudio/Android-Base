package com.thomas.base.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Base class to manage a simple state machine inside a ViewModel.
 *
 * S - the state type (enum, sealed class, or any value type with proper equals/hashCode).
 * DATA - the UI data type used by BaseDataViewModel.
 */
abstract class BaseStateViewModel<S, DATA>(savedStateHandle: SavedStateHandle? = null) :
    BaseDataViewModel<DATA>(savedStateHandle), BaseStateContract<S, DATA> {
    abstract fun initialState(): S

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState())
    override val state: StateFlow<S>
        get() = _state

    override val currentState: S
        get() = _state.value

    protected open fun canTransition(from: S, to: S): Boolean = true

    /**
     * Perform a state transition. Returns true if the state changed.
     */
    protected fun transitionTo(newState: S): Boolean {
        val old = _state.value
        if (old == newState) return false
        if (!canTransition(old, newState)) return false

        // hooks
        onExitState(old, newState)
        // update observable flow for UI
        _state.value = newState
        onEnterState(newState, old)
        onStateChanged(newState, old)

        return true
    }

    protected open fun onEnterState(state: S, from: S) {}
    protected open fun onExitState(from: S, to: S) {}
    protected open fun onStateChanged(newState: S, oldState: S) {}

    protected fun transition(transform: (S) -> S): Boolean =
        transitionTo(transform(_state.value))
}

