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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thomas.base.navigation.Navigator
import com.thomas.base.ui.HandleEvents
import com.thomas.base.ui.collectUiState
import com.thomas.base.viewmodel.Status
import com.thomas.components.ui.ComboBox
import com.thomas.components.ui.ComboBoxExample

@Composable
fun ComponentsScreen(
    navigator: Navigator,
    viewModel: ComponentsContract = hiltViewModel<ComponentsViewModel>()
) {
    val uiState = viewModel.collectUiState()
    viewModel.HandleEvents(navigator = navigator)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
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

                        Spacer(modifier = Modifier.height(24.dp))

                        // Show selected component example
                        data.selectedComponentId?.let { selectedId ->
                            Text(
                                text = "${data.components.find { it.id == selectedId }?.name ?: "Component"} Example",
                                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            when (selectedId) {
                                "combobox" -> {
                                    val countries = listOf(
                                        "United States", "Canada", "United Kingdom", "Germany", "France",
                                        "Japan", "Australia", "Brazil", "India", "China"
                                    )
                                    var selectedCountry by remember { mutableStateOf<String?>(null) }

                                    ComboBox(
                                        selectedItem = selectedCountry,
                                        items = countries,
                                        onItemSelected = { selectedCountry = it },
                                        label = "Select Country",
                                        placeholder = "Choose a country",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                    )
                                }
                                "button" -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Default Button")
                                        }
                                        Button(
                                            onClick = { },
                                            modifier = Modifier.fillMaxWidth(),
                                            enabled = false
                                        ) {
                                            Text("Disabled Button")
                                        }
                                    }
                                }
                                else -> {
                                    Text(
                                        text = "Component example coming soon...",
                                        modifier = Modifier.padding(16.dp)
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