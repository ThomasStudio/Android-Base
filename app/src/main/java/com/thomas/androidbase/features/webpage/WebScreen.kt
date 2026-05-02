package com.thomas.androidbase.features.webpage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.thomas.base.ui.HandleEvents
import com.thomas.base.ui.collectUIState
import com.thomas.components.WebPage

/**
 * Created by thomas on 5/2/2026.
 */

@Composable
fun WebScreen(viewModel: WebContract = hiltViewModel<WebViewModel>()) {
    val uiState = viewModel.collectUIState()
    val data = uiState.data
    viewModel.HandleEvents()

    data?.let {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(it.title)
            WebPage(url = it.url, modifier = Modifier.weight(1f))
        }
    }

}