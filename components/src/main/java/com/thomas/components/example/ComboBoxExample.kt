package com.thomas.components.example

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thomas.components.ComboBox
import com.thomas.components.ComboBoxConfig
import com.thomas.components.ComboBoxSemantics
import com.thomas.components.ComboBoxTheme
import com.thomas.components.ComboBoxTheme.Companion.DropdownItemTextStyle
import com.thomas.components.ComboBoxTheme.Companion.LabelTextStyle

/**
 * Example demonstrating various usages of the ComboBox component
 */
@Composable
fun ComboBoxExample() {
    val countries = listOf(
        "United States",
        "Canada",
        "United Kingdom",
        "Germany",
        "France",
        "Japan",
        "Australia",
        "Brazil",
        "India",
        "China"
    )

    val programmingLanguages = listOf(
        "Kotlin",
        "Java",
        "Python",
        "JavaScript",
        "TypeScript",
        "Swift",
        "Go",
        "Rust",
        "C++",
        "C#"
    )

    var selectedCountry by remember { mutableStateOf<String?>(null) }
    var selectedLanguage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomObjectComboBoxExample()

        Text(
            text = "ComboBox Examples",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )

        // Basic ComboBox
        Text(
            text = "Basic ComboBox",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )
        ComboBox(
            selectedItem = selectedCountry,
            items = countries,
            onItemSelected = { selectedCountry = it },
            config = ComboBoxConfig(
                label = "Select Country",
                showClearButton = true,
                placeholder = "Choose a country",
            ),
            semantics = ComboBoxSemantics(
                onItemSemantics = { "Select $it as your country" },
                onLabelSemantics = { "Country selection $it" },
                onPlaceholderSemantics = { "Country selection placeholder $it" },
                onSelectedItemSemantics = { "Selected country: $it" },
                onDropdownArrowSemantics = { "Dropdown arrow button" },
                onClearButtonSemantics = { "Clear selected country" },
                onDialogTitleSemantics = { "$it title" }
            ),
            theme = ComboBoxTheme(
                cornerRadius = 0.dp,
                horizontalPadding = 22.dp,
                verticalPadding = 33.dp,
                buttonSize = 48.dp,
                maxDropdownHeight = 200.dp,
                labelTextStyle = LabelTextStyle.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
                dropdownItemTextStyle = DropdownItemTextStyle.copy(
                    fontSize = 24.sp,
                ),
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ComboBox with custom styling
        Text(
            text = "ComboBox with Custom Styling",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )
        ComboBox(
            selectedItem = selectedLanguage,
            items = programmingLanguages,
            onItemSelected = { selectedLanguage = it },
            config = ComboBoxConfig(
                label = "Programming Language",
                placeholder = "Select a programming language",
                showClearButton = false
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        ComboBox(
            selectedItem = selectedLanguage,
            items = emptyList(),
            onItemSelected = { selectedLanguage = it },
            modifier = Modifier.fillMaxWidth(),
            config = ComboBoxConfig(
                label = "No items example",
                placeholder = "Select a item",
                showClearButton = false,
                noItemsFoundText = "There is 0 item in the list",
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Disabled ComboBox
        Text(
            text = "Disabled ComboBox",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )
        ComboBox(
            selectedItem = "United States",
            items = countries,
            onItemSelected = {},
            modifier = Modifier.fillMaxWidth(),
            config = ComboBoxConfig(
                label = "Disabled Selection",
                placeholder = "This is disabled",
                enabled = false
            )

        )

        // Display selected values
        Column {
            Text(
                text = "Selected Values:",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Country: ${selectedCountry ?: "None"}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Language: ${selectedLanguage ?: "None"}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * Data class example for custom objects in ComboBox
 */
data class User(
    val id: Int,
    val name: String,
    val email: String
) {
    override fun toString(): String = name
}

/**
 * Example using custom objects with ComboBox
 */
@Composable
fun CustomObjectComboBoxExample() {
    val users = listOf(
        User(1, "John Doe", "john@example.com"),
        User(2, "Jane Smith", "jane@example.com"),
        User(3, "Bob Johnson", "bob@example.com"),
        User(4, "Alice Brown", "alice@example.com")
    )

    var selectedUser by remember { mutableStateOf<User?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Custom Object ComboBox",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )

        ComboBox(
            selectedItem = selectedUser,
            items = users,
            onItemSelected = { selectedUser = it },
            config = ComboBoxConfig(
                label = "Select User",
                placeholder = "Choose a user",
            ),
            modifier = Modifier.fillMaxWidth(),
        ) { user ->
            Column {
                Text(
                    text = user.name,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = user.email,
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Selected User: ${selectedUser?.name ?: "None"}",
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ComboBoxExamplePreview() {
    ComboBoxExample()
}