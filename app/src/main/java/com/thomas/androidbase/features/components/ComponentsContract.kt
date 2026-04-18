package com.thomas.androidbase.features.components

import com.thomas.base.viewmodel.BaseContract
import com.thomas.base.viewmodel.UIState

interface ComponentsContract : BaseContract<UIState<ComponentsData>> {
    fun onComponentSelected(componentId: String)
    fun navigateToDemoScreen(componentId: String)
}

data class ComponentsData(
    val components: List<ComponentItem> = emptyList(),
    val selectedComponentId: String? = null
)

data class ComponentItem(
    val id: String,
    val name: String,
    val description: String
)