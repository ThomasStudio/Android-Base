package com.thomas.base.viewmodel

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BaseContract<STATE : UIStateIF> {
    var initialized: Boolean

    val uiState: StateFlow<STATE>
    val event: SharedFlow<Event>

    // View init
    fun onViewInit() {}

    // call initCall only when first time view created, otherwise do nothing, call onViewInit default
    fun onViewCreated(initCall: () -> Unit = ::onViewInit)

    // View visible callback
    fun onVisible() {}

    fun back()
}

interface BaseDataContract<DATA> : BaseContract<UIState<DATA>>