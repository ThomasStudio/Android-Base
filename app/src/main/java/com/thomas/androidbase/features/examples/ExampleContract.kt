package com.thomas.androidbase.features.examples

import com.thomas.base.viewmodel.BaseDataContract

/**
 * Created by thomas on 5/3/2026.
 */

interface ExampleContract : BaseDataContract<ExampleData> {
    fun onClickExample(example: Examples)
}

data class ExampleData(
    val title: String = "Component examples",
    val currentExample: Examples? = null,
)

enum class Examples(val info: ExampleItem) {
    DROPDOWN(ExampleItem("dropdown", "Dropdown", "Dropdown list")),
    INPUT(ExampleItem("input", "TextField", "Input field with validation")),
    BUTTON(ExampleItem("button", "Button", "Clickable button")),
    DROPDOWN2(ExampleItem("dropdown2", "Dropdown2", "Dropdown list")),
}

data class ExampleItem(
    val id: String,
    val name: String,
    val description: String
)