package com.thomas.androidbase.features.examples

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    BackHandler { viewModel.back() }
    val uiState = viewModel.collectUIState()
    viewModel.HandleEvents(navigator)

    val data = uiState.data

    data?.currentExample?.let {
        Box(modifier = Modifier) {
            Example(it)
        }
    } ?: ExampleList(data, viewModel, Modifier)
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
fun ExampleList(data: ExampleData?, viewModel: ExampleContract, modifier: Modifier = Modifier) {
    if (data?.currentExample != null) return

    LazyColumn(
        modifier = modifier
            .padding(horizontal = 5.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(Examples.entries) { item ->
            Btn(item, viewModel)
        }
    }
}

@Composable
fun Btn(example: Examples, viewModel: ExampleContract) {
    OutlinedButton({ viewModel.onClickExample(example) }) {
        Text(example.name)
    }
}
