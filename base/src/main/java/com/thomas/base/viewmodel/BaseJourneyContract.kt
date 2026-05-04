package com.thomas.base.viewmodel

/**
 * Contract for journey-style ViewModels. Exposes minimal members the UI needs.
 * S - step type; DATA - UI data type
 */
interface BaseJourneyContract<S, DATA> : BaseStepContract<S, DATA> {
    val totalSteps: Int
    val currentIndex: Int
    val isFirstStep: Boolean
    val isLastStep: Boolean
    val progress: Float

    fun next(): Boolean
    fun previous(): Boolean
}

