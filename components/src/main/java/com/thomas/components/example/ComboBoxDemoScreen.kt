package com.thomas.components.example

/**
 * Created by thomas on 4/19/2026.
 */

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.thomas.components.new.ComboBox
import com.thomas.components.new.ComboBoxColorConfig
import com.thomas.components.new.ComboBoxConfig
import com.thomas.components.new.ComboBoxSemanticsConfig
import com.thomas.components.new.ComboBoxShapeConfig
import com.thomas.components.new.ComboBoxSizeConfig
import com.thomas.components.new.ComboBoxTextStyleConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComboBoxDemoScreen() {
    var selectedFruit by remember { mutableStateOf<String?>(null) }
    var selectedCity by remember { mutableStateOf<City?>(null) }

    val fruits = listOf("Apple", "Banana", "Orange", "Mango", "Grapes")
    val cities = listOf(
        City("NYC", "New York City"),
        City("LA", "Los Angeles"),
        City("CHI", "Chicago"),
        City("HOU", "Houston")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 基本用法
        Text(
            text = "Basic ComboBox",
            style = MaterialTheme.typography.titleMedium
        )

        ComboBox(
            items = fruits,
            selectedItem = selectedFruit,
            onItemSelected = { selectedFruit = it },
            config = ComboBoxConfig(
                semantics = ComboBoxSemanticsConfig(
                    textFieldDescription = "Select a fruit",
                    dropdownButtonDescription = "Show fruit options",
                    dropdownMenuDescription = "Available fruits",
                    optionDescriptionPrefix = "Fruit: "
                ),
                size = ComboBoxSizeConfig(
                    width = 300.dp,
                    height = 56.dp
                ),
                textStyle = ComboBoxTextStyleConfig(
                    fontSize = 16,
                    fontWeight = FontWeight.Medium
                ),
                colors = ComboBoxColorConfig(
                    focusedContentColor = Color(0xFF6200EE),
                    unfocusedContentColor = Color(0xFF757575)
                )
            ),
        )

        // 自定义样式
        Text(
            text = "Custom Styled ComboBox",
            style = MaterialTheme.typography.titleMedium
        )

        ComboBox(
            items = cities,
            selectedItem = selectedCity,
            onItemSelected = { selectedCity = it },
            config = ComboBoxConfig(
                semantics = ComboBoxSemanticsConfig(
                    textFieldDescription = "Select a city",
                    dropdownButtonDescription = "Toggle city list",
                    dropdownMenuDescription = "List of available cities"
                ),
                size = ComboBoxSizeConfig(
                    width = 350.dp,
                    height = 64.dp,
                    dropdownMenuMaxHeight = 250.dp,
                    dropdownItemHeight = 56.dp
                ),
                textStyle = ComboBoxTextStyleConfig(
                    fontSize = 18,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                ),
                colors = ComboBoxColorConfig(
                    containerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFFFFFFF),
                    focusedContentColor = Color(0xFF1976D2),
                    unfocusedContentColor = Color(0xFF424242),
                    cursorColor = Color(0xFF1976D2)
                ),
                shape = ComboBoxShapeConfig(
                    shape = RoundedCornerShape(12.dp),
                    focusedBorderThickness = 2.dp
                ),
                dropdownIcon = Icons.Default.ArrowDropDown,
                expandedDropdownIcon = Icons.Default.ArrowDropDown
            ),
            itemContent = { city ->
                Column {
                    Text(
                        text = city.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF1976D2)
                    )
                    Text(
                        text = city.code,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF757575)
                    )
                }
            },
        )

        // 禁用状态
        Text(
            text = "Disabled ComboBox",
            style = MaterialTheme.typography.titleMedium
        )

        ComboBox(
            items = fruits,
            selectedItem = "Apple",
            onItemSelected = { },
            config = ComboBoxConfig(
                enabled = false,
                size = ComboBoxSizeConfig(width = 300.dp),
                colors = ComboBoxColorConfig(
                    disabledContentColor = Color(0xFFBDBDBD)
                )
            ),
        )

        // 错误状态
        Text(
            text = "Error State ComboBox",
            style = MaterialTheme.typography.titleMedium
        )

        ComboBox(
            items = fruits,
            selectedItem = null,
            onItemSelected = { },
            config = ComboBoxConfig(
                isError = true,
                size = ComboBoxSizeConfig(width = 300.dp),
                colors = ComboBoxColorConfig(
                    focusedContentColor = MaterialTheme.colorScheme.error,
                    unfocusedContentColor = MaterialTheme.colorScheme.error
                )
            ),
        )
    }
}

data class City(val code: String, val name: String)