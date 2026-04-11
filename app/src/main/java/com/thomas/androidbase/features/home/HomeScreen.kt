package com.thomas.androidbase.features.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thomas.androidbase.features.home.HomeViewModel
import com.thomas.base.navigation.Navigator
import com.thomas.base.ui.collectUiState
import com.thomas.base.ui.handleEvents
import com.thomas.base.viewmodel.MessageEvent
import com.thomas.base.viewmodel.Status

@Composable
fun HomeScreen(
    navigator: Navigator,
    viewModel: HomeContract = hiltViewModel<HomeViewModel>()
) {
    val uiState = viewModel.collectUiState()
    val context = LocalContext.current

    viewModel.handleEvents(navigator = navigator) {
        when (it) {
            is MessageEvent -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState.status) {
                Status.LOADING -> CircularProgressIndicator(modifier = Modifier.size(50.dp))
                Status.ERROR -> Text("Error loading data.")
                Status.SUCCESS -> {
                    Text("Data loaded successfully! ${uiState.data?.content ?: ""}")
                    Button(onClick = viewModel::onClickWeibo) {
                        Text("Weibo")
                    }
                    Button(onClick = viewModel::onClickNews) {
                        Text("News")
                    }
                }
            }
        }
    }
}