package com.thomas.base.viewmodel

/**
 * Contract for journey-style ViewModels. Exposes minimal members the UI needs.
 * S - step type; DATA - UI data type
 */
interface BaseJourneyContract<S, DATA> : BaseStateContract<S, DATA> {
    val totalSteps: Int
    val currentIndex: Int
    val isFirstStep: Boolean
    val isLastStep: Boolean
    val progress: Float

    fun next(): Boolean
    fun previous(): Boolean
}

abstract class DefaultJourneyContract<S, DATA> : DefaultStateContract<S, DATA>(),
    BaseJourneyContract<S, DATA> {
    abstract val steps: List<S>

    override val totalSteps: Int
        get() = steps.size

    override val currentIndex: Int
        get() = steps.indexOf(currentState).let { if (it >= 0) it else 0 }

    override val isFirstStep: Boolean
        get() = currentIndex == 0

    override val isLastStep: Boolean
        get() = currentIndex >= totalSteps - 1

    override val progress: Float
        get() = if (totalSteps > 0) (currentIndex + 1).toFloat() / totalSteps.toFloat() else 0f

    override fun initialState(): S = steps.first()

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

    protected fun resetJourney() {
        transitionTo(steps.first())
    }

    protected open fun onJourneyStepChanged(index: Int, step: S) {}

    override fun onStateChanged(newState: S, oldState: S) {
        super.onStateChanged(newState, oldState)
        val idx = steps.indexOf(newState).let { if (it >= 0) it else 0 }
        onJourneyStepChanged(idx, newState)
    }
}
