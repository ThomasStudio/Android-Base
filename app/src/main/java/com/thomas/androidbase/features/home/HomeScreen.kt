package com.thomas.androidbase.features.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.thomas.base.navigation.DefaultNavigator
import com.thomas.base.navigation.Navigator
import com.thomas.base.ui.HandleEvents
import com.thomas.base.ui.collectUIState
import com.thomas.base.viewmodel.Event
import com.thomas.base.viewmodel.MessageEvent
import com.thomas.base.viewmodel.Status
import com.thomas.base.viewmodel.UIState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import com.thomas.base.ui.*
import com.thomas.base.viewmodel.DefaultContract

@Composable
fun HomeScreen(
    navigator: Navigator,
    viewModel: HomeContract = hiltViewModel<HomeViewModel>()
) {
    val uiState = viewModel.collectUIState()
    val context = LocalContext.current

    viewModel.HandleEvents(navigator = navigator) {
        when (it) {
            is MessageEvent -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (uiState.status) {
                Status.LOADING -> CircularProgressIndicator(modifier = Modifier.size(50.dp))
                Status.ERROR -> Text("Error loading data.")
                Status.SUCCESS -> {
                    Text(uiState.data?.content ?: "", style = Style.t.blue().f10().medium())
                    Btn("Weibo", viewModel::onClickWeibo)
                    Btn("Zhihu", viewModel::onClickNews)
                    Btn("Booking", viewModel::onClickBooking)
                    Btn("Examples", viewModel::onClickExamples)
                    Btn("Components", viewModel::onClickComponents)
                    Btn("loadingEvent", viewModel::loadingEvent)
                    Btn("Show Message", viewModel::showMessage)
                }
            }
        }
    }
}

@Composable
fun Btn(text: String, onClick: () -> Unit) {
    Spacer(Modifier.size(5.dp))
    OutlinedButton(onClick = onClick, shape = RoundedCornerShape(0.dp)) {
        Text(text, style = Style.t.red().f8().medium())
    }
    Spacer(Modifier.size(5.dp))
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navigator = DefaultNavigator(), FakeHomeContract())
}