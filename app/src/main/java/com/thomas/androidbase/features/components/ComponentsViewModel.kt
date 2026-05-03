package com.thomas.androidbase.features.components

import androidx.lifecycle.SavedStateHandle
import com.thomas.androidbase.Store
import com.thomas.androidbase.navigation.MainRoute
import com.thomas.base.viewmodel.BaseDataViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComponentsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseDataViewModel<ComponentsData>(savedStateHandle), ComponentsContract {
    override fun initialData(): ComponentsData = ComponentsData()

    init {
        payload<ComponentId>()?.let {
            Store.rootVM?.setTitle(it.componentId)
            updateData { copy(componentId = it.componentId) }
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

    override fun onComponentSelected(componentId: String) = navigateWithJson(
        MainRoute.ComponentDemo,
        "componentId" to componentId, "test" to "test"
    )

    override fun onViewInit() {
        Store.rootVM?.setTitle("Components")
    }
}