package com.thomas.androidbase.features.examples

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thomas.androidbase.features.components.ButtonExample
import com.thomas.androidbase.features.components.InputExample
import com.thomas.base.navigation.Navigator
import com.thomas.base.ui.HandleEvents
import com.thomas.base.ui.collectUIState
import com.thomas.components.example.ComboBoxDemoScreen
import com.thomas.components.example.ComboBoxExample

/**
 * Created by thomas on 5/3/2026.
 */

@Composable
fun ExampleScreen(
    navigator: Navigator,
    viewModel: ExampleContract = hiltViewModel<ExampleViewModel>()
) {
    val uiState = viewModel.collectUIState()
    viewModel.HandleEvents(navigator)

    val data = uiState.data

    Scaffold { paddingValues ->
        data?.currentExample?.let {
            Box(modifier = Modifier.padding(paddingValues)) {
                Example(it)
            }
        } ?: ExampleList(data, Modifier.padding(paddingValues))
    }
}

@Composable
fun Example(example: Examples) {
    when (example) {
        Examples.DROPDOWN -> ComboBoxExample()
        Examples.INPUT -> InputExample()
        Examples.BUTTON -> ButtonExample()
        Examples.DROPDOWN2 -> ComboBoxDemoScreen()
    }
}

@Composable
fun ExampleList(data: ExampleData?, modifier: Modifier = Modifier) {
    if (data?.currentExample != null) return

    LazyColumn(
        modifier = modifier.padding(horizontal = 5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(Examples.entries) { item ->
            Btn(item)
        }
    }
}

@Composable
fun Btn(example: Examples, viewModel: ExampleContract = hiltViewModel<ExampleViewModel>()) {
    OutlinedButton({ viewModel.onClickExample(example) }) {
        Text(example.name)
    }
}
