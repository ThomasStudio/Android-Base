package com.thomas.androidbase.features.components

import com.thomas.base.viewmodel.BaseContract
import com.thomas.base.viewmodel.Error
import com.thomas.base.viewmodel.Status
import com.thomas.base.viewmodel.UIState
import com.thomas.base.viewmodel.UIStateIF

interface ComponentsContract : BaseContract<UIState<ComponentsData>> {
    fun onComponentSelected(componentId: String)
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