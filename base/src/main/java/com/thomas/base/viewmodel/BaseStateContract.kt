package com.thomas.base.viewmodel

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

    /**
     * Request a transition to [newState]. Returns true if transition occurred.
     */
    fun transitionTo(newState: S): Boolean

    /**
     * Convenience to compute and transition to a new state based on current state.
     */
    fun transition(transform: (S) -> S): Boolean
}

