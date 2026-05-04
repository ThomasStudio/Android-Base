package com.thomas.base.viewmodel

import androidx.lifecycle.SavedStateHandle

/**
 * Base class to manage a Journey composed of an ordered list of steps.
 *
 * S - step type (enum, sealed class, data class, etc.)
 * DATA - UI data type handled by BaseDataViewModel
 */
abstract class BaseJourneyViewModel<S, DATA>(
    protected val steps: List<S>,
    savedStateHandle: SavedStateHandle? = null
) : BaseStepViewModel<S, DATA>(savedStateHandle) {

    init {
        require(steps.isNotEmpty()) { "Journey must contain at least one step" }
    }

    /**
     * Total number of steps in this journey.
     */
    val totalSteps: Int
        get() = steps.size

    /**
     * Current step index within [steps].
     */
    val currentIndex: Int
        get() = steps.indexOf(currentStep).let { if (it >= 0) it else 0 }

    val isFirstStep: Boolean
        get() = currentIndex == 0

    val isLastStep: Boolean
        get() = currentIndex >= totalSteps - 1

    val progress: Float
        get() = if (totalSteps > 0) (currentIndex + 1).toFloat() / totalSteps.toFloat() else 0f

    override fun initialStep(): S = steps.first()

    /**
     * Advance to the next step if possible. Returns true when the step changed.
     */
    fun next(): Boolean {
        if (isLastStep) return false
        val nextIndex = currentIndex + 1
        return goToStep(nextIndex)
    }

    /**
     * Move to previous step if possible.
     */
    fun previous(): Boolean {
        if (isFirstStep) return false
        val prevIndex = currentIndex - 1
        return goToStep(prevIndex)
    }

    /**
     * Jump to a specific step index.
     */
    fun goToStep(index: Int): Boolean {
        if (index < 0 || index >= steps.size) return false
        val target = steps[index]
        return transitionTo(target)
    }

    /**
     * Reset journey to first step.
     */
    fun resetJourney() {
        transitionTo(steps.first())
    }

    /**
     * Hook called when step changes; subclasses can override to react to index and step.
     */
    protected open fun onJourneyStepChanged(index: Int, step: S) {}

    override fun onStepChanged(newStep: S, oldStep: S) {
        super.onStepChanged(newStep, oldStep)
        val idx = steps.indexOf(newStep).let { if (it >= 0) it else 0 }
        onJourneyStepChanged(idx, newStep)
    }

    /**
     * Mark the journey complete. Default implementation uses existing completeSteps() helper.
     */
    protected fun completeJourney() {
        completeSteps()
    }
}

