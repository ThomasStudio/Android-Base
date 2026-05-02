package com.thomas.androidbase.features.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.thomas.base.ui.collectUIState
import com.thomas.base.viewmodel.Status.ERROR
import com.thomas.base.viewmodel.Status.LOADING
import com.thomas.base.viewmodel.Status.SUCCESS

@Composable
fun NewsScreen(
    navigator: Navigator,
    viewModel: NewsContract = hiltViewModel<NewsViewModel>()
) {
    val uiState = viewModel.collectUIState()
    val data = uiState.data

    viewModel.HandleEvents(navigator)

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp)
    ) {
        Text(text = "News")

        when (uiState.status) {
            LOADING -> LoadingScreen()

            SUCCESS -> {
                data?.let { zhihuHot ->
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(zhihuHot.data) { item ->
                            Column(
                                modifier = Modifier
                                    .clickable { viewModel.onItemClick(item) }
                                    .fillMaxWidth(),
                            ) {
                                Text(
                                    text = item.target.question.title,
                                    fontWeight = FontWeight.Bold
                                )
                                Row {
                                    Text(text = "Answer: ${item.target.question.answerCount}")
                                    Spacer(Modifier.width(3.dp))
                                    Text(text = "Follower: ${item.target.question.followerCount}")
                                }
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