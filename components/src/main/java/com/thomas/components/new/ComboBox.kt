package com.thomas.components.new

/**
 * Created by thomas on 4/19/2026.
 */

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun <T> ComboBox(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T?) -> Unit,
    modifier: Modifier = Modifier,
    config: ComboBoxConfig = ComboBoxConfig(),
    itemContent: @Composable (T) -> Unit = { item ->
        Text(
            text = item.toString(),
            style = TextStyle(
                fontSize = config.textStyle.fontSize.sp,
                fontWeight = config.textStyle.fontWeight,
                fontFamily = config.textStyle.fontFamily,
                textAlign = config.textStyle.textAlign,
                lineHeight = config.textStyle.lineHeight.sp
            ),
            color = config.colors.contentColor
        )
    },
    keySelector: (T) -> Any = { it.hashCode() }
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = selectedItem?.toString() ?: "",
                selection = TextRange(selectedItem?.toString()?.length ?: 0)
            )
        )
    }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    // 更新选中项时同步文本
    LaunchedEffect(selectedItem) {
        textFieldValue = TextFieldValue(
            text = selectedItem?.toString() ?: "",
            selection = TextRange(selectedItem?.toString()?.length ?: 0)
        )
    }

    Box(
        modifier = modifier
            .semantics {
                contentDescription = config.semantics.textFieldDescription
            }
    ) {
        Column {
            // 输入框
            OutlinedTextField<String>(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                    // 过滤选项
                    expanded = true
                },
                modifier = Modifier
                    .then(
                        if (config.size.width != Dp.Unspecified) {
                            Modifier.width(config.size.width)
                        } else {
                            Modifier.fillMaxWidth()
                        }
                    )
                    .height(config.size.height)
                    .focusRequester(focusRequester)
                    .onKeyEvent { event ->
                        when (event.key) {
                            Key.DirectionDown -> {
                                expanded = true
                                true
                            }
                            Key.Escape -> {
                                expanded = false
                                focusManager.clearFocus()
                                true
                            }
                            else -> false
                        }
                    }
                    .semantics {
                        contentDescription = config.semantics.textFieldDescription
                        stateDescription = if (expanded) "Expanded" else "Collapsed"
                    },
                enabled = config.enabled,
                readOnly = config.readOnly,
                singleLine = config.singleLine,
                isError = config.isError,
                label = config.label,
                placeholder = config.placeholder,
                leadingIcon = config.leadingIcon,
                trailingIcon = config.trailingIcon ?: {
                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.semantics {
                            contentDescription = config.semantics.dropdownButtonDescription
                        }
                    ) {
                        Icon(
                            imageVector = if (expanded) config.expandedDropdownIcon else config.dropdownIcon,
                            contentDescription = null,
                            tint = if (config.enabled) {
                                config.colors.contentColor
                            } else {
                                config.colors.disabledContentColor
                            }
                        )
                    }
                },
                shape = config.shape.shape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = config.colors.focusedContainerColor,
                    unfocusedContainerColor = config.colors.unfocusedContainerColor,
                    disabledContainerColor = config.colors.disabledContainerColor,
                    focusedTextColor = config.colors.focusedContentColor,
                    unfocusedTextColor = config.colors.unfocusedContentColor,
                    disabledTextColor = config.colors.disabledContentColor,
                    focusedBorderColor = config.colors.focusedContentColor,
                    unfocusedBorderColor = config.colors.unfocusedContentColor,
                    disabledBorderColor = config.colors.disabledContentColor,
                    cursorColor = config.colors.cursorColor,
                    errorCursorColor = config.colors.errorCursorColor
                ),
                textStyle = TextStyle(
                    fontSize = config.textStyle.fontSize.sp,
                    fontWeight = config.textStyle.fontWeight,
                    fontFamily = config.textStyle.fontFamily,
                    textAlign = config.textStyle.textAlign,
                    lineHeight = config.textStyle.lineHeight.sp
                ),
                interactionSource = interactionSource
            )

            // 下拉菜单
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Popup(
                    alignment = Alignment.TopStart,
                    properties = PopupProperties(
                        dismissOnClickOutside = true,
                        dismissOnBackPress = true
                    ),
                    onDismissRequest = {
                        expanded = false
                        focusManager.clearFocus()
                    }
                ) {
                    Card(
                        modifier = Modifier
                            .then(
                                if (config.size.width != Dp.Unspecified) {
                                    Modifier.width(config.size.width)
                                } else {
                                    Modifier.fillMaxWidth()
                                }
                            )
                            .heightIn(max = config.size.dropdownMenuMaxHeight)
                            .clip(MaterialTheme.shapes.medium)
                            .semantics {
                                contentDescription = config.semantics.dropdownMenuDescription
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier.padding(vertical = config.size.verticalPadding / 2),
                            contentPadding = PaddingValues(horizontal = config.size.horizontalPadding)
                        ) {
                            items(items, key = keySelector) { item ->
                                val isSelected = selectedItem == item

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(config.size.dropdownItemHeight)
                                        .clickable {
                                            onItemSelected(item)
                                            expanded = false
                                            focusManager.clearFocus()
                                        }
                                        .semantics {
                                            contentDescription = "${config.semantics.optionDescriptionPrefix}${item}"
                                            selected = isSelected
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = config.colors.focusedContentColor,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }

                                    Box(modifier = Modifier.weight(1f)) {
                                        itemContent(item)
                                    }
                                }

                                if (items.indexOf(item) < items.size - 1) {
                                    HorizontalDivider(
                                        thickness = 0.5.dp,
                                        color = MaterialTheme.colorScheme.outlineVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 点击外部关闭下拉菜单
    DisposableEffect(Unit) {
        onDispose {
            expanded = false
        }
    }
}