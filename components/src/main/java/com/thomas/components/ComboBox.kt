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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
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

@Immutable
data class ComboBoxConfig<T>(
    val label: String? = null,
    val placeholder: String = "Select an option",
    val enabled: Boolean = true,
    val showClearButton: Boolean = false,
    val noItemsFoundText: String = "No items found",
    val borderColor: Color = Color(0xFFCCCCCC),
    val expandedBorderColor: Color = Color(0xFF1976D2),
    val backgroundColor: Color = Color(0xFFFFFFFF),
    val iconColor: Color = Color(0xFF666666),
    val labelTextStyle: TextStyle = Defaults.labelTextStyle,
    val selectedItemTextStyle: TextStyle = Defaults.selectedItemTextStyle,
    val placeholderTextStyle: TextStyle = Defaults.placeholderTextStyle,
    val dropdownItemTextStyle: TextStyle = Defaults.dropdownItemTextStyle,
    val emptyStateTextStyle: TextStyle = Defaults.emptyStateTextStyle,
    val dialogTitleTextStyle: TextStyle = Defaults.dialogTitleTextStyle,
    val horizontalPadding: Dp = 16.dp,
    val verticalPadding: Dp = 12.dp,
    val cornerRadius: Dp = 8.dp,
    val buttonSize: Dp = 24.dp,
    val maxDropdownHeight: Dp = 200.dp,
    val dropdownArrowSemantics: String = "Dropdown arrow",
    val clearButtonSemantics: String = "Clear selection",
    val onLabelSemantics: ((String) -> String)? = null,
    val onPlaceholderSemantics: ((String) -> String)? = null,
    val onItemSemantics: ((T) -> String)? = null,
    val onSelectedItemSemantics: ((T) -> String)? = null,
    val onDialogTitleSemantics: ((String) -> String)? = null
) {
    companion object Defaults {
        val labelTextStyle =
            TextStyle(color = Color(0xFF666666), fontWeight = FontWeight.Medium, fontSize = 14.sp)
        val selectedItemTextStyle = TextStyle(color = Color(0xFF000000), fontSize = 16.sp)
        val placeholderTextStyle = TextStyle(color = Color(0xFF999999), fontSize = 16.sp)
        val dropdownItemTextStyle = TextStyle(color = Color(0xFF000000), fontSize = 14.sp)
        val emptyStateTextStyle = TextStyle(color = Color(0xFF999999), fontSize = 14.sp)
        val dialogTitleTextStyle =
            TextStyle(color = Color(0xFF000000), fontWeight = FontWeight.Medium, fontSize = 16.sp)

        /**
         * create a [ComboBoxConfig] using MaterialTheme colors and text styles.
         */
        @Composable
        fun <T> material() = ComboBoxConfig<T>().copy(
            borderColor = MaterialTheme.colorScheme.outline,
            expandedBorderColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.surface,
            iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            labelTextStyle = label(),
            selectedItemTextStyle = selectedItem(),
            placeholderTextStyle = placeholder(),
            dropdownItemTextStyle = dropdownItem(),
            emptyStateTextStyle = emptyState(),
            dialogTitleTextStyle = dialogTitle()
        )

        @Composable
        fun label() = MaterialTheme.typography.labelMedium.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        @Composable
        fun selectedItem() = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        )

        @Composable
        fun placeholder() = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        @Composable
        fun dropdownItem() = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface
        )

        @Composable
        fun emptyState() = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        @Composable
        fun dialogTitle() = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


/**
 * Creates a new [ComboBoxConfig] using MaterialTheme colors and text styles.
 */
@Composable
fun <T> materialConfig() = ComboBoxConfig.material<T>()

@Stable
@Composable
fun <T> ComboBox(
    selectedItem: T?,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    config: ComboBoxConfig<T> = materialConfig(),
    itemContent: @Composable ((T) -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }

    val displayText = selectedItem?.toString() ?: config.placeholder

    Column(modifier = modifier) {
        config.label?.let { labelText ->
            ComboBoxLabel(
                text = labelText,
                textStyle = config.labelTextStyle,
                semanticsProvider = config.onLabelSemantics
            )
        }

        ComboBoxTrigger(
            displayText = displayText,
            selectedItem = selectedItem,
            expanded = expanded,
            onClearClick = { onItemSelected(null as T) },
            onTriggerClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            config = config
        )

        if (expanded) {
            ComboBoxDropdown(
                items = items,
                onItemSelected = { item ->
                    onItemSelected(item)
                    expanded = false
                },
                onDismiss = { expanded = false },
                config = config,
                itemContent = itemContent
            )
        }
    }
}

@Composable
private fun ComboBoxLabel(
    text: String,
    textStyle: TextStyle,
    semanticsProvider: ((String) -> String)?
) {
    Text(
        text = text,
        style = textStyle,
        modifier = Modifier
            .padding(bottom = 4.dp)
            .semantics {
                contentDescription = semanticsProvider?.invoke(text) ?: text
            }
    )
}

@Composable
private fun <T> ComboBoxTrigger(
    displayText: String,
    selectedItem: T?,
    expanded: Boolean,
    onClearClick: () -> Unit,
    onTriggerClick: () -> Unit,
    modifier: Modifier = Modifier,
    config: ComboBoxConfig<T>
) {
    val isSelected = selectedItem != null

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(config.cornerRadius))
            .border(
                width = 1.dp,
                color = if (config.enabled && expanded) config.expandedBorderColor else config.borderColor,
                shape = RoundedCornerShape(config.cornerRadius)
            )
            .background(config.backgroundColor, RoundedCornerShape(config.cornerRadius))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = config.enabled) { onTriggerClick() }
                .padding(horizontal = config.horizontalPadding, vertical = config.verticalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(1f)) {
                Text(
                    text = displayText,
                    style = if (isSelected) config.selectedItemTextStyle else config.placeholderTextStyle,
                    maxLines = 1,
                    modifier = Modifier.semantics {
                        contentDescription = if (isSelected) {
                            config.onSelectedItemSemantics?.invoke(selectedItem!!) ?: displayText
                        } else {
                            config.onPlaceholderSemantics?.invoke(config.placeholder)
                                ?: config.placeholder
                        }
                    }
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (config.showClearButton && isSelected && config.enabled) {
                    IconButton(
                        onClick = onClearClick,
                        modifier = Modifier
                            .size(config.buttonSize)
                            .semantics { contentDescription = config.clearButtonSemantics }
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = null,
                            tint = config.iconColor
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = config.iconColor,
                    modifier = Modifier
                        .size(config.buttonSize)
                        .rotate(if (expanded) 180f else 0f)
                        .semantics { contentDescription = config.dropdownArrowSemantics }
                )
            }
        }
    }
}

@Composable
private fun <T> ComboBoxDropdown(
    items: List<T>,
    onItemSelected: (T) -> Unit,
    onDismiss: () -> Unit,
    config: ComboBoxConfig<T>,
    itemContent: (@Composable (T) -> Unit)?
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            val titleText = config.label ?: "Select an option"
            Text(
                text = titleText,
                style = config.dialogTitleTextStyle,
                modifier = Modifier.semantics {
                    contentDescription =
                        config.onDialogTitleSemantics?.invoke(titleText) ?: titleText
                }
            )
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(config.maxDropdownHeight)
            ) {
                LazyColumn {
                    if (items.isEmpty()) {
                        item {
                            Text(
                                text = config.noItemsFoundText,
                                style = config.emptyStateTextStyle,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    } else {
                        items(items, key = { it.hashCode() }) { item ->
                            DropdownMenuItem(
                                text = {
                                    if (itemContent != null) {
                                        itemContent(item)
                                    } else {
                                        Text(
                                            text = item.toString(),
                                            style = config.dropdownItemTextStyle,
                                            modifier = Modifier.semantics {
                                                contentDescription =
                                                    config.onItemSemantics?.invoke(item)
                                                        ?: item.toString()
                                            }
                                        )
                                    }
                                },
                                onClick = { onItemSelected(item) },
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

@Composable
fun ComboBox(
    selectedItem: String?,
    items: List<String>,
    onItemSelected: (String?) -> Unit,
    modifier: Modifier = Modifier,
    config: ComboBoxConfig<String> = materialConfig()
) {
    ComboBox<String>(
        selectedItem = selectedItem,
        items = items,
        onItemSelected = onItemSelected,
        modifier = modifier,
        config = config
    )
}