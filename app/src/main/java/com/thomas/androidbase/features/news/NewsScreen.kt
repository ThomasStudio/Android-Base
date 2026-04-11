package com.thomas.androidbase.features.news

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thomas.androidbase.features.news.NewsViewModel
import com.thomas.base.navigation.Navigator
import com.thomas.base.ui.collectUiState
import com.thomas.base.ui.handleEvents

@Composable
fun NewsScreen(navigator: Navigator? = null) {
    val viewModel: NewsViewModel = viewModel()
    val uiState = viewModel.collectUiState()
    
    viewModel.handleEvents(navigator = navigator)
    
    Scaffold(modifier = Modifier.fillMaxSize()) {
 innerPadding ->
        Text(
            text = "News Screen",
            modifier = Modifier.padding(innerPadding)
        )
    }
}