package com.thomas.base.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.thomas.base.navigation.Navigator
import com.thomas.base.viewmodel.BackEvent
import com.thomas.base.viewmodel.BaseContract
import com.thomas.base.viewmodel.Event
import com.thomas.base.viewmodel.HideLoadingIndicatorEvent
import com.thomas.base.viewmodel.MessageEvent
import com.thomas.base.viewmodel.NavigateEvent
import com.thomas.base.viewmodel.ShowLoadingIndicatorEvent
import com.thomas.base.viewmodel.UIStateIF
import kotlinx.coroutines.flow.collectLatest

/**
 * Created by thomas on 4/11/2026.
 */

fun handleBaseContractEvent(
    event: Event,
    context: Context,
    navigator: Navigator? = null,
    loadingState: MutableState<Boolean>? = null,
    onEvent: (Event) -> Unit = {},
) {
    when (event) {
        is NavigateEvent -> {
            navigator?.navigate(
                route = event.route,
                popUpToRoute = event.popUpToRoute,
                inclusive = event.inclusive,
                launchSingleTop = event.launchSingleTop
            )
        }

        is BackEvent -> navigator?.back()
        is ShowLoadingIndicatorEvent -> loadingState?.value = true
        is HideLoadingIndicatorEvent -> loadingState?.value = false
        is MessageEvent -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
        else -> onEvent(event)
    }
}

@Composable
fun <STATE : UIStateIF> BaseContract<STATE>.collectUIState(): STATE {
    val uiState by this.uiState.collectAsStateWithLifecycle()
    return uiState
}

@Composable
fun <STATE : UIStateIF> BaseContract<STATE>.HandleEvents(
    navigator: Navigator? = null,
    onEvent: (Event) -> Unit = {},
) {
    val lifecyclerOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val loadingState = remember { mutableStateOf(false) }

    LoadingOverlay(visible = loadingState.value)
    ViewCreated()
    OnVisible()

    LaunchedEffect(this, lifecyclerOwner, navigator) {
        lifecyclerOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            event.collectLatest { viewEvent ->
                handleBaseContractEvent(
                    event = viewEvent,
                    context = context,
                    navigator = navigator,
                    loadingState = loadingState,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
fun <STATE : UIStateIF> BaseContract<STATE>.ViewCreated() {
    LaunchedEffect(Unit) {
        viewCreated()
    }
}

@Composable
fun <STATE : UIStateIF> BaseContract<STATE>.OnVisible() {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            onVisible()
        }
    }
}

@Composable
private fun LoadingOverlay(visible: Boolean) {
    if (!visible) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
