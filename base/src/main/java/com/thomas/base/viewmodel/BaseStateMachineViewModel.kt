package com.thomas.base.viewmodel

import androidx.lifecycle.SavedStateHandle

/**
 * Base class to manage a simple state machine inside a ViewModel.
 *
 * S - the state type (enum, sealed class, or any value type with proper equals/hashCode).
 * DATA - the UI data type used by BaseDataViewModel.
 */
abstract class BaseStateMachineViewModel<S, DATA>(
    initialState: S,
    savedStateHandle: SavedStateHandle? = null
) : BaseDataViewModel<DATA>(savedStateHandle) {

    private var _state: S = initialState
    val currentState: S
        get() = _state

    /**
     * Called to determine whether a transition from [from] to [to] is allowed.
     * Default implementation allows all transitions; override to restrict transitions.
     */
    protected open fun canTransition(from: S, to: S): Boolean = true

    /**
     * Perform a state transition. Returns true if the state changed.
     */
    fun transitionTo(newState: S): Boolean {
        val old = _state
        if (old == newState) return false
        if (!canTransition(old, newState)) return false

        // hooks
        onExitState(old, newState)
        _state = newState
        onEnterState(newState, old)
        onStateChanged(newState, old)

        return true
    }

    /**
     * Hook called when entering [state]. [from] is the previous state.
     */
    protected open fun onEnterState(state: S, from: S) {}

    /**
     * Hook called when exiting [from] state to [to] state.
     */
    protected open fun onExitState(from: S, to: S) {}

    /**
     * Hook called after a successful state change. Default is no-op; override to update UI state
     * or emit events.
     */
    protected open fun onStateChanged(newState: S, oldState: S) {}

    /**
     * Convenience: transition using a lambda that computes the next state based on current state.
     */
    protected fun transition(transform: (S) -> S): Boolean = transitionTo(transform(_state))

    /**
     * Complete the state machine workflow.
     */
    protected open fun completeStateMachine() {
        sendMessage("State machine completed")
    }
}

