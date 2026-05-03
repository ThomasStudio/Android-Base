package com.thomas.androidbase.features.examples

import com.thomas.base.viewmodel.BaseDataContract

/**
 * Created by thomas on 5/3/2026.
 */

interface ExampleContract : BaseDataContract<ExampleData>

data class ExampleData(
    val title: String = "Component examples",
    val currentExample: Examples? = null,
)

enum class Examples(val info: ExampleItem) {
    DROPDOWN(ExampleItem("dropdown", "Dropdown", "Dropdown list")),
    INPUT(ExampleItem("input", "TextField", "Input field with validation")),
    BUTTON(ExampleItem("button", "Button", "Clickable button")),

}

data class ExampleItem(
    val id: String,
    val name: String,
    val description: String
)