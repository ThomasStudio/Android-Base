package com.thomas.androidbase.features.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thomas.androidbase.ui.components.LoadingScreen
import com.thomas.base.navigation.Navigator
import com.thomas.base.ui.ViewCreated
import com.thomas.base.ui.collectUIState
import com.thomas.base.ui.HandleEvents
import com.thomas.base.viewmodel.MessageEvent
import com.thomas.base.viewmodel.Status.*

@Composable
fun NewsScreen(
    navigator: Navigator,
    viewModel: NewsContract = hiltViewModel<NewsViewModel>()
) {
    val uiState = viewModel.collectUIState()
    val data = uiState.data
    val context = LocalContext.current

    // Call getZhihuHot when the screen is first composed
    viewModel.ViewCreated()

    viewModel.HandleEvents(navigator = navigator) {
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

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "News Screen")
            Button(onClick = viewModel::showMessage) {
                Text("Show Message")
            }

            when (uiState.status) {
                LOADING -> LoadingScreen()

                SUCCESS -> {
                    data?.let { zhihuHot ->
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(zhihuHot.data) { item ->
                                Column(
                                    modifier = Modifier
                                        .clickable { viewModel.onItemClick(item) }
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = item.target.question.title,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = "Answer Count: ${item.target.question.answerCount}")
                                    Text(text = "Follower Count: ${item.target.question.followerCount}")
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
                    Text(text = "code: ${uiState.error?.code} Error: ${uiState.error?.message}")
                }
            }
        }
    }
}