package com.thomas.base.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.thomas.base.navigation.Navigator
import com.thomas.base.viewmodel.BackEvent
import com.thomas.base.viewmodel.BaseContract
import com.thomas.base.viewmodel.Event
import com.thomas.base.viewmodel.NavigateEvent
import com.thomas.base.viewmodel.UIStateIF
import kotlinx.coroutines.flow.collectLatest

/**
 * Created by thomas on 4/11/2026.
 */

fun handleBaseContractEvent(
    event: Event,
    navigator: Navigator? = null,
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
        else -> onEvent(event)
    }
}

@Composable
fun <STATE : UIStateIF> BaseContract<STATE>.collectUiState(): STATE {
    val uiState by this.uiState.collectAsStateWithLifecycle()
    return uiState
}

@Composable
fun <STATE : UIStateIF> BaseContract<STATE>.handleEvents(
    navigator: Navigator? = null,
    onEvent: (Event) -> Unit = {},
) {
    val lifecyclerOwner = LocalLifecycleOwner.current

    LaunchedEffect(this, lifecyclerOwner, navigator) {
        lifecyclerOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            event.collectLatest { viewEvent ->
                handleBaseContractEvent(viewEvent, navigator, onEvent)
            }
        }
    }
}