package com.thomas.androidbase.features.weibo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thomas.androidbase.ui.components.LoadingScreen
import com.thomas.base.navigation.Navigator
import com.thomas.base.ui.HandleEvents
import com.thomas.base.ui.ViewCreated
import com.thomas.base.ui.collectUIState
import com.thomas.base.viewmodel.Status.ERROR
import com.thomas.base.viewmodel.Status.LOADING
import com.thomas.base.viewmodel.Status.SUCCESS

@Composable
fun WeiboScreen(
    navigator: Navigator,
    viewModel: WeiboContract = hiltViewModel<WeiboViewModel>()
) {
    val uiState = viewModel.collectUIState()

    viewModel.HandleEvents(navigator)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp)
    ) {
        Text(text = "Weibo")

        when (uiState.status) {
            LOADING -> {
                LoadingScreen()
            }

            SUCCESS -> {
                uiState.data?.let { weiboHot ->
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(weiboHot.data) { item ->
                            Column(
                                modifier = Modifier
                                    .clickable { viewModel.onItemClick(item) }
                                    .fillMaxWidth(),
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

            ERROR -> {
                Text(text = "Error: ${uiState.error?.message}")
            }
        }
    }
}