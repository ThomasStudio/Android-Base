package com.thomas.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Unified data class for ComboBox theme configuration including colors, text styles, and sizes
 */
data class ComboBoxTheme(
    // Colors with default values
    val borderColor: Color = Color(0xFFCCCCCC),
    val expandedBorderColor: Color = Color(0xFF1976D2), // Material Blue 700
    val backgroundColor: Color = Color(0xFFFFFFFF),
    val iconColor: Color = Color(0xFF666666),

    // Text Styles with default values
    val labelTextStyle: TextStyle = LabelTextStyle,
    val selectedItemTextStyle: TextStyle = SelectedItemTextStyle,
    val placeholderTextStyle: TextStyle = PlaceholderTextStyle,
    val dropdownItemTextStyle: TextStyle = DropdownItemTextStyle,
    val emptyStateTextStyle: TextStyle = EmptyStateTextStyle,
    val dialogTitleTextStyle: TextStyle = DialogTitleTextStyle,

    // Sizes
    val horizontalPadding: Dp = 16.dp,
    val verticalPadding: Dp = 12.dp,
    val cornerRadius: Dp = 8.dp,
    val buttonSize: Dp = 24.dp,
    val maxDropdownHeight: Dp = 200.dp
) {
    companion object {
        /**
         * Default text styles for ComboBox components
         */
        val LabelTextStyle = TextStyle(
            color = Color(0xFF666666),
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
        
        val SelectedItemTextStyle = TextStyle(
            color = Color(0xFF000000),
            fontSize = 16.sp
        )
        
        val PlaceholderTextStyle = TextStyle(
            color = Color(0xFF999999),
            fontSize = 16.sp
        )
        
        val DropdownItemTextStyle = TextStyle(
            color = Color(0xFF000000),
            fontSize = 14.sp
        )
        
        val EmptyStateTextStyle = TextStyle(
            color = Color(0xFF999999),
            fontSize = 14.sp
        )
        
        val DialogTitleTextStyle = TextStyle(
            color = Color(0xFF000000),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    }
}

/**
 * Data class for ComboBox semantic callback configuration
 */
data class ComboBoxSemantics<T>(
    val onLabelSemantics: ((String) -> String)? = null,
    val onPlaceholderSemantics: ((String) -> String)? = null,
    val onItemSemantics: ((T) -> String)? = null,
    val onSelectedItemSemantics: ((T) -> String)? = null,
    val onDropdownArrowSemantics: (() -> String)? = null,
    val onClearButtonSemantics: (() -> String)? = null,
    val onDialogTitleSemantics: ((String) -> String)? = null
)

/**
 * Data class for ComboBox configuration including basic properties
 */
data class ComboBoxConfig<T>(
    val label: String? = null,
    val placeholder: String = "Select an option",
    val enabled: Boolean = true,
    val showClearButton: Boolean = false,
    val noItemsFoundText: String = "No items found",
    val theme: ComboBoxTheme = ComboBoxTheme(),
    val semantics: ComboBoxSemantics<T> = ComboBoxSemantics()
)

/**
 * ComboBox component that provides a dropdown selection
 * 
 * @param selectedItem The currently selected item
 * @param items List of items to display in the dropdown
 * @param onItemSelected Callback when an item is selected
 * @param config ComboBoxConfig configuration for basic ComboBox properties
 * @param modifier Modifier for styling the ComboBox
 * @param itemContent Custom composable for displaying each item
 */
@Composable
fun <T> ComboBox(
    selectedItem: T?,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    config: ComboBoxConfig<T> = ComboBoxConfig(),
    itemContent: @Composable ((T) -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }

    // Dynamic border color based on state
    val borderColor = remember(expanded, config.enabled, config.theme) {
        when {
            !config.enabled -> config.theme.borderColor.copy(alpha = 0.5f)
            expanded -> config.theme.expandedBorderColor
            else -> config.theme.borderColor
        }
    }

    val displayText = remember(selectedItem, config.placeholder) {
        selectedItem?.toString() ?: config.placeholder
    }

    Column(modifier = modifier) {
        config.label?.let { labelText ->
            Text(
                text = labelText,
                style = config.theme.labelTextStyle,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .semantics {
                        contentDescription = config.semantics.onLabelSemantics?.invoke(labelText) ?: labelText
                    }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(config.theme.cornerRadius))
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(config.theme.cornerRadius)
                )
                .background(
                    color = config.theme.backgroundColor,
                    shape = RoundedCornerShape(config.theme.cornerRadius)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        enabled = config.enabled,
                        onClick = { expanded = true }
                    )
                    .padding(
                        horizontal = config.theme.horizontalPadding,
                        vertical = config.theme.verticalPadding
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = displayText,
                        style = if (selectedItem != null) config.theme.selectedItemTextStyle else config.theme.placeholderTextStyle,
                        maxLines = 1,
                        modifier = Modifier.semantics {
                            val contentDesc = if (selectedItem != null) {
                                config.semantics.onSelectedItemSemantics?.invoke(selectedItem) ?: displayText
                            } else {
                                config.semantics.onPlaceholderSemantics?.invoke(config.placeholder) ?: config.placeholder
                            }
                            contentDescription = contentDesc
                        }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (config.showClearButton && selectedItem != null && config.enabled) {
                        IconButton(
                            onClick = {
                                @Suppress("UNCHECKED_CAST")
                                onItemSelected(null as T)
                            },
                            modifier = Modifier
                                .size(config.theme.buttonSize)
                                .semantics {
                                    contentDescription = config.semantics.onClearButtonSemantics?.invoke()
                                        ?: "Clear selection"
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                tint = config.theme.iconColor
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = config.theme.iconColor,
                        modifier = Modifier
                            .size(config.theme.buttonSize)
                            .rotate(if (expanded) 180f else 0f)
                            .semantics {
                                contentDescription =
                                    config.semantics.onDropdownArrowSemantics?.invoke() ?: "Dropdown arrow"
                            }
                    )
                }
            }
        }

        // Dialog for dropdown menu
        if (expanded) {
            AlertDialog(
                onDismissRequest = { expanded = false },
                title = {
                    Text(
                        text = config.label ?: "Select an option",
                        style = theme.dialogTitleTextStyle,
                        modifier = Modifier.semantics {
                            val dialogTitle = config.label ?: "Select an option"
                            contentDescription = semantics.onDialogTitleSemantics?.invoke(dialogTitle) ?: dialogTitle
                        }
                    )
                },
                text = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(theme.maxDropdownHeight)
                    ) {
                        LazyColumn {
                            if (items.isEmpty()) {
                                item {
                                    Text(
                                        text = config.noItemsFoundText,
                                        style = theme.emptyStateTextStyle,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    )
                                }
                            } else {
                                items(
                                    items = items,
                                    key = { item -> item.hashCode() }
                                ) { item ->
                                    DropdownMenuItem(
                                        text = {
                                            if (itemContent != null) {
                                                itemContent(item)
                                            } else {
                                                Text(
                                                    text = item.toString(),
                                                    style = theme.dropdownItemTextStyle,
                                                    modifier = Modifier.semantics {
                                                        contentDescription =
                                                            semantics.onItemSemantics?.invoke(item)
                                                                ?: item.toString()
                                                    }
                                                )
                                            }
                                        },
                                        onClick = {
                                            onItemSelected(item)
                                            expanded = false
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {}
            )
        }
    }
}

/**
 * Simplified ComboBox for string items
 */
@Composable
fun ComboBox(
    selectedItem: String?,
    items: List<String>,
    onItemSelected: (String?) -> Unit,
    modifier: Modifier = Modifier,
    config: ComboBoxConfig = ComboBoxConfig(showClearButton = true),
    theme: ComboBoxTheme = ComboBoxTheme(),
    semantics: ComboBoxSemantics<String> = ComboBoxSemantics()
) {
    ComboBox<String>(
        selectedItem = selectedItem,
        items = items,
        onItemSelected = onItemSelected,
        modifier = modifier,
        config = config,
        theme = theme,
        semantics = semantics
    )
}