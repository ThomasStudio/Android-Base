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
) : BaseStepViewModel<S, DATA>(savedStateHandle), BaseJourneyContract<S, DATA> {

    init {
        require(steps.isNotEmpty()) { "Journey must contain at least one step" }
    }

    override val totalSteps: Int
        get() = steps.size

    override val currentIndex: Int
        get() = steps.indexOf(currentStep).let { if (it >= 0) it else 0 }

    override val isFirstStep: Boolean
        get() = currentIndex == 0

    override val isLastStep: Boolean
        get() = currentIndex >= totalSteps - 1

    override val progress: Float
        get() = if (totalSteps > 0) (currentIndex + 1).toFloat() / totalSteps.toFloat() else 0f

    override fun initialStep(): S = steps.first()

    override fun next(): Boolean {
        if (isLastStep) return false
        val nextIndex = currentIndex + 1
        return goToStep(nextIndex)
    }

    override fun previous(): Boolean {
        if (isFirstStep) return false
        val prevIndex = currentIndex - 1
        return goToStep(prevIndex)
    }

    protected fun goToStep(index: Int): Boolean {
        if (index < 0 || index >= steps.size) return false
        val target = steps[index]
        return transitionTo(target)
    }

    /**
     * Reset journey to first step.
     */
    protected fun resetJourney() {
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
     * Mark the journey complete.
     */
    protected fun completeJourney() {}
}

