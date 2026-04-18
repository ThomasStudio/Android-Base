package com.thomas.androidbase.features.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thomas.base.navigation.Navigator
import com.thomas.components.ui.ComboBoxExample

@Composable
fun ComponentDemoScreen(
    navigator: Navigator,
    componentId: String
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Back button
            Button(
                onClick = { navigator.back() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Back to Components")
            }

            // Component demo content based on componentId
            when (componentId) {
                "combobox" -> {
                    Text(
                        text = "ComboBox Demo",
                        style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    ComboBoxExample()
                }
                "button" -> {
                    Text(
                        text = "Button Demo",
                        style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

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
                "textfield" -> {
                    Text(
                        text = "TextField Demo",
                        style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = "TextField component examples coming soon...",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                "card" -> {
                    Text(
                        text = "Card Demo",
                        style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = "Card component examples coming soon...",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    Text(
                        text = "Component Demo",
                        style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = "Demo for component '$componentId' coming soon...",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}