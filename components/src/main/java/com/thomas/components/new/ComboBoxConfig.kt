package com.thomas.components.new

/**
 * Created by thomas on 4/19/2026.
 */

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// 语义配置
data class ComboBoxSemanticsConfig(
    val textFieldDescription: String = "ComboBox input field",
    val dropdownButtonDescription: String = "Expand dropdown menu",
    val dropdownMenuDescription: String = "Dropdown options menu",
    val optionDescriptionPrefix: String = "Option: "
)

// 尺寸配置
data class ComboBoxSizeConfig(
    val width: Dp = Dp.Unspecified,
    val height: Dp = 56.dp,
    val dropdownMenuMaxHeight: Dp = 300.dp,
    val dropdownItemHeight: Dp = 48.dp,
    val horizontalPadding: Dp = 16.dp,
    val verticalPadding: Dp = 8.dp
)

// 文本样式配置
data class ComboBoxTextStyleConfig(
    val fontSize: Int = 16,
    val fontWeight: FontWeight = FontWeight.Normal,
    val fontFamily: FontFamily? = null,
    val textAlign: TextAlign = TextAlign.Start,
    val lineHeight: Int = 24
)

// 颜色配置
data class ComboBoxColorConfig(
    val containerColor: Color = Color.Transparent,
    val contentColor: Color,
    val focusedContainerColor: Color = Color.Transparent,
    val focusedContentColor: Color,
    val unfocusedContainerColor: Color = Color.Transparent,
    val unfocusedContentColor: Color,
    val disabledContainerColor: Color = Color.Transparent,
    val disabledContentColor: Color,
    val cursorColor: Color,
    val errorCursorColor: Color
)

// 默认颜色配置
@Composable
fun defaultComboBoxColorConfig() = ComboBoxColorConfig(
    contentColor = MaterialTheme.colorScheme.onSurface,
    focusedContentColor = MaterialTheme.colorScheme.primary,
    unfocusedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    cursorColor = MaterialTheme.colorScheme.primary,
    errorCursorColor = MaterialTheme.colorScheme.error
)

// 形状配置
data class ComboBoxShapeConfig(
    val shape: Shape = RoundedCornerShape(4.dp),
    val focusedBorderThickness: Dp = 2.dp,
    val unfocusedBorderThickness: Dp = 1.dp,
    val disabledBorderThickness: Dp = 1.dp
)

// 主配置类
data class ComboBoxConfig(
    // 语义配置
    val semantics: ComboBoxSemanticsConfig = ComboBoxSemanticsConfig(),

    // 尺寸配置
    val size: ComboBoxSizeConfig = ComboBoxSizeConfig(),

    // 文本样式配置
    val textStyle: ComboBoxTextStyleConfig = ComboBoxTextStyleConfig(),

    // 颜色配置
    val colors: ComboBoxColorConfig,

    // 形状配置
    val shape: ComboBoxShapeConfig = ComboBoxShapeConfig(),

    // 下拉按钮配置
    val dropdownIcon: ImageVector = Icons.Default.ArrowDropDown,
    val expandedDropdownIcon: ImageVector = Icons.Default.ArrowDropDown,

    // 其他选项
    val readOnly: Boolean = true,
    val singleLine: Boolean = true,
    val enabled: Boolean = true,
    val isError: Boolean = false,
    val label: @Composable (() -> Unit)? = null,
    val placeholder: @Composable (() -> Unit)? = null,
    val leadingIcon: @Composable (() -> Unit)? = null,
    val trailingIcon: @Composable ((Boolean) -> Unit)? = null
)

// 默认配置
@Composable
fun defaultComboBoxConfig() = ComboBoxConfig(
    colors = defaultComboBoxColorConfig()
)