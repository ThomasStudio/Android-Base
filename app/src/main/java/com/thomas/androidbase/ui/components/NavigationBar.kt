package com.thomas.androidbase.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thomas.base.navigation.Navigator

@Composable
fun NavigationBar(navigator: Navigator) {
    Row(modifier = Modifier.fillMaxWidth()) {
        IconButton({ navigator.back() }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
