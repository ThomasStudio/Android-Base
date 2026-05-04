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
abstract class BaseStepViewModel<S, DATA>(savedStateHandle: SavedStateHandle? = null) :
    BaseDataViewModel<DATA>(savedStateHandle), BaseStepContract<S, DATA> {
    abstract fun initialStep(): S

    private val _step: MutableStateFlow<S> = MutableStateFlow(initialStep())
    override val step: StateFlow<S>
        get() = _step

    override val currentStep: S
        get() = _step.value

    protected open fun canTransition(from: S, to: S): Boolean = true

    /**
     * Perform a state transition. Returns true if the state changed.
     */
    override fun transitionTo(newStep: S): Boolean {
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

    protected open fun onEnterStep(step: S, from: S) {}
    protected open fun onExitStep(from: S, to: S) {}
    protected open fun onStepChanged(newStep: S, oldStep: S) {}

    override fun transition(transform: (S) -> S): Boolean =
        transitionTo(transform(_step.value))
}

