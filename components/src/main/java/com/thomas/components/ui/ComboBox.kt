package com.thomas.components.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ComboBox component that provides a dropdown selection with search functionality
 * 
 * @param selectedItem The currently selected item
 * @param items List of items to display in the dropdown
 * @param onItemSelected Callback when an item is selected
 * @param label Text label for the combobox
 * @param placeholder Placeholder text when no item is selected
 * @param modifier Modifier for styling the combobox
 * @param enabled Whether the combobox is enabled
 * @param searchable Whether the combobox should have a search functionality
 * @param maxDropdownHeight Maximum height for the dropdown menu
 * @param showClearButton Whether to show a clear button
 * @param itemContent Custom composable for displaying each item
 */
@Composable
fun <T> ComboBox(
    selectedItem: T?,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    label: String? = null,
    placeholder: String = "Select an option",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    searchable: Boolean = false,
    maxDropdownHeight: Dp = 200.dp,
    showClearButton: Boolean = true,
    itemContent: @Composable ((T) -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    
    // Use LaunchedEffect to request focus when the dropdown expands and is searchable
    LaunchedEffect(expanded, searchable) {
        if (expanded && searchable) {
            focusRequester.requestFocus()
        }
    }
    
    val filteredItems = if (searchable && searchQuery.isNotEmpty()) {
        items.filter { item ->
            item.toString().contains(searchQuery, ignoreCase = true)
        }
    } else {
        items
    }
    
    Column(modifier = modifier) {
        label?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = if (expanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(
                    color = if (enabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = enabled) {
                        expanded = !expanded
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    if (searchable && expanded) {
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    expanded = false
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .onFocusChanged { focusState ->
                                    if (focusState.hasFocus) {
                                        expanded = true
                                    }
                                },
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = placeholder,
                                        style = TextStyle(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 16.sp
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        )
                    } else {
                        Text(
                            text = selectedItem?.toString() ?: placeholder,
                            style = TextStyle(
                                color = if (selectedItem != null) MaterialTheme.colorScheme.onSurface 
                                       else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 16.sp
                            ),
                            maxLines = 1
                        )
                    }
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (showClearButton && selectedItem != null && enabled) {
                        IconButton(
                            onClick = { onItemSelected(null as T) },
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
        
        // Dropdown menu
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 2 })
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(MaterialTheme.colorScheme.surface)
                    .height(maxDropdownHeight)
            ) {
                LazyColumn {
                    if (filteredItems.isEmpty()) {
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
                        items(filteredItems) { item ->
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
                                            )
                                        )
                                    }
                                },
                                onClick = {
                                    onItemSelected(item)
                                    expanded = false
                                    searchQuery = ""
                                    keyboardController?.hide()
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
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
    searchable: Boolean = false,
    maxDropdownHeight: Dp = 200.dp,
    showClearButton: Boolean = true
) {
    ComboBox<String>(
        selectedItem = selectedItem,
        items = items,
        onItemSelected = onItemSelected,
        label = label,
        placeholder = placeholder,
        modifier = modifier,
        enabled = enabled,
        searchable = searchable,
        maxDropdownHeight = maxDropdownHeight,
        showClearButton = showClearButton
    )
}

/**
 * ComboBox with search functionality
 */
@Composable
fun <T> SearchableComboBox(
    selectedItem: T?,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    label: String? = null,
    placeholder: String = "Search and select an option",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    maxDropdownHeight: Dp = 200.dp,
    showClearButton: Boolean = true,
    itemContent: @Composable ((T) -> Unit)? = null
) {
    ComboBox(
        selectedItem = selectedItem,
        items = items,
        onItemSelected = onItemSelected,
        label = label,
        placeholder = placeholder,
        modifier = modifier,
        enabled = enabled,
        searchable = true,
        maxDropdownHeight = maxDropdownHeight,
        showClearButton = showClearButton,
        itemContent = itemContent
    )
}