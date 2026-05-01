package com.thomas.androidbase.features.components

import androidx.lifecycle.SavedStateHandle
import com.thomas.androidbase.navigation.MainRoute
import com.thomas.base.domain.PayloadStore
import com.thomas.base.viewmodel.BaseDataViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComponentsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseDataViewModel<ComponentsData>(), ComponentsContract {
    override fun initialData(): ComponentsData = ComponentsData()

    init {
        val payloadId = savedStateHandle.get<String>(MainRoute.PAYLOAD_ID)
        payloadId?.let {
            PayloadStore.consume<ComponentId>(it)?.let { data ->
                updateData { copy(componentId = data.componentId) }
            }
        }

        scope.launch {
            delay(500) // Simulate loading
            updateData {
                copy(
                    components = listOf(
                        ComponentItem(
                            id = "combobox",
                            name = "ComboBox",
                            description = "Dropdown selection component"
                        ),
                        ComponentItem(
                            id = "input",
                            name = "Input",
                            description = "Various input styles and states"
                        ),
                        ComponentItem(
                            id = "button",
                            name = "Button",
                            description = "Various button styles and states"
                        ),
                        ComponentItem(
                            id = "textfield",
                            name = "TextField",
                            description = "Input field with validation"
                        ),
                        ComponentItem(
                            id = "card",
                            name = "Card",
                            description = "Container with elevation and padding"
                        ),
                        ComponentItem(
                            id = "comboboxNew",
                            name = "ComboBox New",
                            description = "Dropdown selection component"
                        ),
                    )
                )
            }
        }
    }

    override fun onComponentSelected(componentId: String) {
        navigateToDemoScreen(componentId)
    }

    private fun navigateToDemoScreen(componentId: String) {
        val id = PayloadStore.putJson("componentId" to componentId, "test" to "test")
        navigate(MainRoute.ComponentDemo(id))
    }
}