package com.thomas.androidbase.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NavigationBar(navigationData: NavigationData = NavigationData()) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (navigationData.canBack)
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .clickable { navigationData.onBack() }
                    .padding(4.dp)
            )

        Spacer(Modifier.width(2.dp))
        Text(text = navigationData.title, maxLines = 1)
        Spacer(Modifier.weight(1f))
    }
}

data class NavigationData(
    val title: String = "",
    val onBack: () -> Unit = {},
    val canBack: Boolean = true
)
