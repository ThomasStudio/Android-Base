package com.thomas.base.viewmodel

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BaseContract<STATE : UIStateIF> {
    val uiState: StateFlow<STATE>
    val event: SharedFlow<Event>

    fun viewCreated() {}
    
    fun back()
}