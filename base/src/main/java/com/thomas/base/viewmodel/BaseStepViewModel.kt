package com.thomas.base.viewmodel

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Base class to manage a simple state machine inside a ViewModel.
 *
 * S - the state type (enum, sealed class, or any value type with proper equals/hashCode).
 * DATA - the UI data type used by BaseDataViewModel.
 */
abstract class BaseStepViewModel<S, DATA>(savedStateHandle: SavedStateHandle) :
    BaseDataViewModel<DATA>(savedStateHandle) {
    /**
     * Provide the initial state for the state machine. Subclasses must implement.
     */
    abstract fun initialStep(): S

    // Backing observable state so UI can collect state changes
    private val _step: MutableStateFlow<S> = MutableStateFlow(initialStep())
    val step: StateFlow<S> = _step.asStateFlow()

    /**
     * Convenience to access the current state value synchronously.
     */
    val currentStep: S
        get() = _step.value

    /**
     * Called to determine whether a transition from [from] to [to] is allowed.
     * Default implementation allows all transitions; override to restrict transitions.
     */
    protected open fun canTransition(from: S, to: S): Boolean = true

    /**
     * Perform a state transition. Returns true if the state changed.
     */
    fun transitionTo(newStep: S): Boolean {
        val old = _step.value
        if (old == newStep) return false
        if (!canTransition(old, newStep)) return false

        // hooks
        onExitStep(old, newStep)
        // update observable flow for UI
        _step.value = newStep
        onEnterStep(newStep, old)
        onStepChanged(newStep, old)

        return true
    }

    /**
     * Hook called when entering [step]. [from] is the previous state.
     */
    protected open fun onEnterStep(step: S, from: S) {}

    /**
     * Hook called when exiting [from] state to [to] state.
     */
    protected open fun onExitStep(from: S, to: S) {}

    /**
     * Hook called after a successful state change. Default is no-op; override to update UI state
     * or emit events.
     */
    protected open fun onStepChanged(newStep: S, oldStep: S) {}

    /**
     * Convenience: transition using a lambda that computes the next state based on current state.
     */
    protected fun transition(transform: (S) -> S): Boolean =
        transitionTo(transform(_step.value))

    /**
     * Complete the state machine workflow.
     */
    protected open fun completeSteps() {
        sendMessage("State machine completed")
    }
}

