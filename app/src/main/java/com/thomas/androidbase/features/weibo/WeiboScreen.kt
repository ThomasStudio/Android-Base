package com.thomas.androidbase.features.weibo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.thomas.base.navigation.Navigator
import com.thomas.base.ui.collectUiState
import com.thomas.base.ui.handleEvents
import com.thomas.base.viewmodel.MessageEvent

@Composable
fun WeiboScreen(
    navigator: Navigator,
    viewModel: WeiboContract = hiltViewModel<WeiboViewModel>()
) {
    val uiState = viewModel.collectUiState()
    val context = LocalContext.current
    
    viewModel.handleEvents(navigator = navigator) {
        when (it) {
            is MessageEvent -> {
                android.widget.Toast.makeText(context, it.message, android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    Scaffold(modifier = Modifier.fillMaxSize()) {
 innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Weibo Screen")
            Button(onClick = viewModel::showMessage) {
                Text("Show Message")
            }
        }
    }
}