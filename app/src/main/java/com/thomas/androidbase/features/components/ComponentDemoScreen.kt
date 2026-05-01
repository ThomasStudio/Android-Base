package com.thomas.androidbase.features.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thomas.base.navigation.Navigator
import com.thomas.base.ui.collectUiState
import com.thomas.components.example.ComboBoxDemoScreen
import com.thomas.components.example.ComboBoxExample

@Composable
fun ComponentDemoScreen(
    navigator: Navigator,
    viewModel: ComponentsContract = hiltViewModel<ComponentsViewModel>()
) {
    val uiState = viewModel.collectUiState()
    val componentId = uiState.data?.componentId ?: "unknown"

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
                "combobox" -> ComboBoxExample()
                "input" -> InputExample()
                "comboboxNew" -> ComboBoxDemoScreen()
                "button" -> ButtonExample()
                else -> DefaultExample(componentId)

            }
        }
    }
}

@Composable
fun ButtonExample() {
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

@Composable
fun DefaultExample(componentId: String = "unknown") {
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

/**
 * Input examples
 */
@Composable
fun InputExample() {
    var input by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var number by remember { mutableStateOf(TextFieldValue()) }
    var multiline by remember { mutableStateOf(TextFieldValue()) }
    var errorText by remember { mutableStateOf(TextFieldValue()) }
    var disabledText by remember { mutableStateOf(TextFieldValue("Disabled text")) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Basic TextField
        TextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Basic Input") },
            placeholder = { Text("Enter text") },
        )
        Text("Input: ${input.text}")

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            placeholder = { Text("Enter password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        // Number TextField
        TextField(
            value = number,
            onValueChange = { number = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Number Input") },
            placeholder = { Text("Enter number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // Multiline TextField
        TextField(
            value = multiline,
            onValueChange = { multiline = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            label = { Text("Multiline Input") },
            placeholder = { Text("Enter multiple lines") },
            maxLines = 4
        )

        // TextField with error
        TextField(
            value = errorText,
            onValueChange = { errorText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Error Input") },
            placeholder = { Text("Enter text") },
            isError = errorText.text.isEmpty(),
            supportingText = { if (errorText.text.isEmpty()) Text("This field is required") }
        )

        // Disabled TextField
        TextField(
            value = disabledText,
            onValueChange = { disabledText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Disabled Input") },
            enabled = false
        )

        // TextField with icons
        TextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("TextField with Icons") },
            placeholder = { Text("Enter text") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Person") },
            trailingIcon = {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "Clear",
                    modifier = Modifier.clickable { input = TextFieldValue() }
                )
            }
        )
    }
}