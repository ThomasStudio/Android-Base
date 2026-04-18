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
import androidx.compose.material3.Typography

/**
 * ComboBox component that provides a dropdown selection
 *
 * @param selectedItem The currently selected item
 * @param items List of items to display in the dropdown
 * @param onItemSelected Callback when an item is selected
 * @param label Text label for the combobox
 * @param placeholder Placeholder text when no item is selected
 * @param modifier Modifier for styling the combobox
 * @param colorScheme Color scheme for styling the combobox
 * @param typography Typography for text styling
 * @param enabled Whether the combobox is enabled
 * @param maxDropdownHeight Maximum height for the dropdown menu
 * @param showClearButton Whether to show a clear button
 * @param labelFontSize Font size for the label text
 * @param placeholderFontSize Font size for the placeholder text
 * @param selectedItemFontSize Font size for the selected item text
 * @param dropdownItemFontSize Font size for the dropdown item text
 * @param horizontalPadding Horizontal padding for the combobox
 * @param verticalPadding Vertical padding for the combobox
 * @param cornerRadius Corner radius for the combobox
 * @param noItemsFoundText Text to display when no items are available
 * @param buttonSize Size for the clear button and dropdown arrow
 * @param itemContent Custom composable for displaying each item
 * @param onLabelSemantics Callback to customize label semantics (content description)
 * @param onPlaceholderSemantics Callback to customize placeholder semantics (content description)
 * @param onItemSemantics Callback to customize item semantics (content description)
 * @param onSelectedItemSemantics Callback to customize selected item semantics (content description)
 * @param onDropdownArrowSemantics Callback to customize dropdown arrow semantics (content description)
 * @param onClearButtonSemantics Callback to customize clear button semantics (content description)
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
    typography: Typography = MaterialTheme.typography,
    enabled: Boolean = true,
    maxDropdownHeight: Dp = 200.dp,
    showClearButton: Boolean = false,
    labelFontSize: Dp = 14.dp,
    placeholderFontSize: Dp = 16.dp,
    selectedItemFontSize: Dp = 16.dp,
    dropdownItemFontSize: Dp = 14.dp,
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = 12.dp,
    cornerRadius: Dp = 8.dp,
    noItemsFoundText: String = "No items found",
    buttonSize: Dp = 24.dp,
    onLabelSemantics: ((String) -> String)? = null,
    onPlaceholderSemantics: ((String) -> String)? = null,
    onItemSemantics: ((T) -> String)? = null,
    onSelectedItemSemantics: ((T) -> String)? = null,
    onDropdownArrowSemantics: (() -> String)? = null,
    onClearButtonSemantics: (() -> String)? = null,
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
                style = typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = labelFontSize.value.sp
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
                .clip(RoundedCornerShape(cornerRadius))
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(cornerRadius)
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
                    .padding(horizontal = horizontalPadding, vertical = verticalPadding),
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
                            fontSize = if (selectedItem != null) selectedItemFontSize.value.sp else placeholderFontSize.value.sp
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
                            modifier = Modifier
                                .size(buttonSize)
                                .semantics {
                                    contentDescription = onClearButtonSemantics?.invoke() ?: "Clear selection"
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null, // Content description handled by parent
                                tint = colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null, // Content description handled by parent
                        tint = colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(buttonSize)
                            .rotate(if (expanded) 180f else 0f)
                            .semantics {
                                contentDescription = onDropdownArrowSemantics?.invoke() ?: "Dropdown arrow"
                            }
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
                        style = typography.titleMedium
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
                                        text = noItemsFoundText,
                                        style = TextStyle(
                                            color = colorScheme.onSurfaceVariant,
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
                                                        color = colorScheme.onSurface,
                                                        fontSize = dropdownItemFontSize.value.sp
                                                    ),
                                                    modifier = Modifier.semantics {
                                                        contentDescription = onItemSemantics?.invoke(item) ?: item.toString()
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
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    typography: Typography = MaterialTheme.typography,
    enabled: Boolean = true,
    maxDropdownHeight: Dp = 200.dp,
    showClearButton: Boolean = true,
    labelFontSize: Dp = 14.dp,
    placeholderFontSize: Dp = 16.dp,
    selectedItemFontSize: Dp = 16.dp,
    dropdownItemFontSize: Dp = 14.dp,
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = 12.dp,
    cornerRadius: Dp = 8.dp,
    noItemsFoundText: String = "No items found",
    buttonSize: Dp = 24.dp,
    onLabelSemantics: ((String) -> String)? = null,
    onPlaceholderSemantics: ((String) -> String)? = null,
    onItemSemantics: ((String) -> String)? = null,
    onSelectedItemSemantics: ((String) -> String)? = null,
    onDropdownArrowSemantics: (() -> String)? = null,
    onClearButtonSemantics: (() -> String)? = null
) {
    ComboBox<String>(
        selectedItem = selectedItem,
        items = items,
        onItemSelected = onItemSelected,
        label = label,
        placeholder = placeholder,
        modifier = modifier,
        colorScheme = colorScheme,
        typography = typography,
        enabled = enabled,
        maxDropdownHeight = maxDropdownHeight,
        showClearButton = showClearButton,
        labelFontSize = labelFontSize,
        placeholderFontSize = placeholderFontSize,
        selectedItemFontSize = selectedItemFontSize,
        dropdownItemFontSize = dropdownItemFontSize,
        horizontalPadding = horizontalPadding,
        verticalPadding = verticalPadding,
        cornerRadius = cornerRadius,
        noItemsFoundText = noItemsFoundText,
        buttonSize = buttonSize,
        onLabelSemantics = onLabelSemantics,
        onPlaceholderSemantics = onPlaceholderSemantics,
        onItemSemantics = onItemSemantics,
        onSelectedItemSemantics = onSelectedItemSemantics,
        onDropdownArrowSemantics = onDropdownArrowSemantics,
        onClearButtonSemantics = onClearButtonSemantics
    )
}