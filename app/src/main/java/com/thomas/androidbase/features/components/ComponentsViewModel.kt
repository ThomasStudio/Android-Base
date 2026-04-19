package com.thomas.androidbase.features.components

import com.thomas.androidbase.navigation.MainRoute
import com.thomas.base.viewmodel.BaseViewModel
import com.thomas.base.viewmodel.Status
import com.thomas.base.viewmodel.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComponentsViewModel @Inject constructor() : BaseViewModel<UIState<ComponentsData>>(),
    ComponentsContract {
    override fun initialState() = UIState<ComponentsData>()

    init {
        scope.launch {
            delay(500) // Simulate loading
            updateState {
                copy(
                    status = Status.SUCCESS,
                    data = ComponentsData(
                        components = listOf(
                            ComponentItem(
                                id = "combobox",
                                name = "ComboBox",
                                description = "Dropdown selection component"
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
                )
            }
        }
    }

    override fun onComponentSelected(componentId: String) {
        navigateToDemoScreen(componentId)
    }

    private fun navigateToDemoScreen(componentId: String) {
        navigate(MainRoute.ComponentDemo(componentId))
    }
}