package com.thomas.androidbase.features.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thomas.base.navigation.Navigator
import com.thomas.base.ui.HandleEvents
import com.thomas.base.ui.collectUIState
import com.thomas.base.viewmodel.Status

@Composable
fun ComponentsScreen(
    navigator: Navigator,
    viewModel: ComponentsContract = hiltViewModel<ComponentsViewModel>()
) {
    val uiState = viewModel.collectUIState()
    viewModel.HandleEvents(navigator = navigator)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Button to go back to home
            Button(
                onClick = { viewModel.back() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Back to Home")
            }

            when (uiState.status) {
                Status.LOADING -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading components...")
                    }
                }
                Status.ERROR -> {
                    Text(
                        text = "Error loading components",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Status.SUCCESS -> {
                    uiState.data?.let { data ->
                        // Component selection buttons
                        Text(
                            text = "Available Components",
                            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        data.components.forEach { component ->
                            Button(
                                onClick = { viewModel.onComponentSelected(component.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = component.name,
                                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = component.description,
                                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}