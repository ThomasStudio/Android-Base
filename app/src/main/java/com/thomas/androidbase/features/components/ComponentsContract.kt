package com.thomas.androidbase.features.components

import com.thomas.base.viewmodel.BaseDataContract

interface ComponentsContract : BaseDataContract<ComponentsData> {
    fun onComponentSelected(componentId: String)
}

data class ComponentsData(
    val components: List<ComponentItem> = emptyList(),
    val selectedComponentId: String? = null,
    val componentId: String? = null
)

@Serializable
data class ComponentId(
    val componentId: String
)

data class ComponentItem(
    val id: String,
    val name: String,
    val description: String
)