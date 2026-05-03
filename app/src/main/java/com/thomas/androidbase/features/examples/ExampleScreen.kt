package com.thomas.androidbase.features.examples

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.thomas.base.navigation.Navigator

/**
 * Created by thomas on 5/3/2026.
 */

@Composable
fun ExampleScreen(
    navigator: Navigator,
    viewModel: ExampleViewModel = hiltViewModel<ExampleViewModel>()
) {

}
