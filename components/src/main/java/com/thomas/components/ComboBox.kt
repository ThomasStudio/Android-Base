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
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ComboBox component that provides a dropdown selection
 * 
 * @param selectedItem The currently selected item
 * @param items List of items to display in the dropdown
 * @param onItemSelected Callback when an item is selected
 * @param label Text label for the combobox
 * @param placeholder Placeholder text when no item is selected
 * @param modifier Modifier for styling the combobox
 * @param enabled Whether the combobox is enabled
 * @param maxDropdownHeight Maximum height for the dropdown menu
 * @param showClearButton Whether to show a clear button
 * @param itemContent Custom composable for displaying each item
 * @param onLabelSemantics Callback to customize label semantics (content description)
 * @param onPlaceholderSemantics Callback to customize placeholder semantics (content description)
 * @param onItemSemantics Callback to customize item semantics (content description)
 * @param onSelectedItemSemantics Callback to customize selected item semantics (content description)
 */
@Composable
fun <T> ComboBox(
    selectedItem: T?,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    label: String? = null,
    placeholder: String = "Select an option",
    modifier: Modifier = Modifier,
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    enabled: Boolean = true,
    maxDropdownHeight: Dp = 200.dp,
    showClearButton: Boolean = false,
    onLabelSemantics: ((String) -> String)? = null,
    onPlaceholderSemantics: ((String) -> String)? = null,
    onItemSemantics: ((T) -> String)? = null,
    onSelectedItemSemantics: ((T) -> String)? = null,
    itemContent: @Composable ((T) -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }

    // Optimized derived states for better performance
    val borderColor = remember(expanded, enabled, colorScheme) {
        when {
            !enabled -> colorScheme.outline.copy(alpha = 0.5f)
            expanded -> colorScheme.primary
            else -> colorScheme.outline
        }
    }

    val backgroundColor = remember(enabled, colorScheme) {
        if (enabled) colorScheme.surface else colorScheme.surfaceVariant
    }

    val displayText = remember(selectedItem, placeholder) {
        selectedItem?.toString() ?: placeholder
    }

    val textColor = remember(selectedItem, colorScheme) {
        if (selectedItem != null) colorScheme.onSurface else colorScheme.onSurfaceVariant
    }

    Column(modifier = modifier) {
        label?.let { labelText ->
            Text(
                text = labelText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                ),
                color = colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .semantics {
                        contentDescription = onLabelSemantics?.invoke(labelText) ?: labelText
                    }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        enabled = enabled,
                        onClick = {
                            if (enabled) expanded = !expanded
                        }
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = displayText,
                        style = TextStyle(
                            color = textColor,
                            fontSize = 16.sp
                        ),
                        maxLines = 1,
                        modifier = Modifier.semantics {
                            val contentDesc = if (selectedItem != null) {
                                onSelectedItemSemantics?.invoke(selectedItem) ?: displayText
                            } else {
                                onPlaceholderSemantics?.invoke(placeholder) ?: placeholder
                            }
                            contentDescription = contentDesc
                        }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (showClearButton && selectedItem != null && enabled) {
                        IconButton(
                            onClick = {
                                @Suppress("UNCHECKED_CAST")
                                onItemSelected(null as T)
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear selection",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown arrow",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(if (expanded) 180f else 0f)
                    )
                }
            }
        }

        // Dialog for dropdown menu - optimized with key to prevent unnecessary recompositions
        if (expanded) {
            AlertDialog(
                onDismissRequest = { expanded = false },
                title = {
                    Text(
                        text = label ?: "Select an option",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                text = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(maxDropdownHeight)
                    ) {
                        LazyColumn {
                            if (items.isEmpty()) {
                                item {
                                    Text(
                                        text = "No items found",
                                        style = TextStyle(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 14.sp
                                        ),
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
                                                    style = TextStyle(
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        fontSize = 14.sp
                                                    ),
                                                    modifier = Modifier.semantics {
                                                        contentDescription =
                                                            onItemSemantics?.invoke(item)
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
    label: String? = null,
    placeholder: String = "Select an option",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    maxDropdownHeight: Dp = 200.dp,
    showClearButton: Boolean = true,
    onLabelSemantics: ((String) -> String)? = null,
    onPlaceholderSemantics: ((String) -> String)? = null,
    onItemSemantics: ((String) -> String)? = null,
    onSelectedItemSemantics: ((String) -> String)? = null
) {
    ComboBox<String>(
        selectedItem = selectedItem,
        items = items,
        onItemSelected = onItemSelected,
        label = label,
        placeholder = placeholder,
        modifier = modifier,
        enabled = enabled,
        maxDropdownHeight = maxDropdownHeight,
        showClearButton = showClearButton,
        onLabelSemantics = onLabelSemantics,
        onPlaceholderSemantics = onPlaceholderSemantics,
        onItemSemantics = onItemSemantics,
        onSelectedItemSemantics = onSelectedItemSemantics
    )
}