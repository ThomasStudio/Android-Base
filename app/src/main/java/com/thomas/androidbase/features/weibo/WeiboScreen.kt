package com.thomas.androidbase.features.weibo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thomas.androidbase.ui.components.LoadingScreen
import com.thomas.base.navigation.Navigator
import com.thomas.base.ui.collectUiState
import com.thomas.base.ui.handleEvents
import com.thomas.base.viewmodel.MessageEvent

@Composable
fun WeiboScreen(
    navigator: Navigator,
    viewModel: WeiboContract = hiltViewModel<WeiboViewModel>()
) {
    val uiState = viewModel.collectUiState()
    val context = LocalContext.current

    viewModel.handleEvents(navigator = navigator) {
        when (it) {
            is MessageEvent -> {
                android.widget.Toast.makeText(
                    context,
                    it.message,
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Call getWeiboHot when the screen is first composed
    LaunchedEffect(Unit) {
        viewModel.viewCreated()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Weibo Screen")
            Button(onClick = viewModel::showMessage) {
                Text("Show Message")
            }

            when (uiState.status) {
                com.thomas.base.viewmodel.Status.LOADING -> {
                    LoadingScreen()
                }

                com.thomas.base.viewmodel.Status.SUCCESS -> {
                    uiState.data?.let { weiboHot ->
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(weiboHot.data) { item ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "${item.index}. ${item.title}",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = item.hot)
                                }
                                HorizontalDivider(
                                    Modifier,
                                    DividerDefaults.Thickness,
                                    DividerDefaults.color
                                )
                            }
                        }
                    }
                }

                com.thomas.base.viewmodel.Status.ERROR -> {
                    Text(text = "Error: ${uiState.error?.message}")
                }
            }
        }
    }
}