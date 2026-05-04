package com.thomas.base.viewmodel

import kotlinx.coroutines.flow.StateFlow

/**
 * Contract for step-based ViewModels exposing minimal members the UI should observe/call.
 * S - step type; DATA - UI data type
 */
interface BaseStepContract<S, DATA> : BaseDataContract<DATA> {
    /**
     * Observable stream of current step values.
     */
    val step: StateFlow<S>

    /**
     * Synchronously read the current step value.
     */
    val currentStep: S

    /**
     * Request a transition to [newStep]. Returns true if transition occurred.
     */
    fun transitionTo(newStep: S): Boolean

    /**
     * Convenience to compute and transition to a new step based on current step.
     */
    fun transition(transform: (S) -> S): Boolean
}

