package com.thomas.androidbase.features.booking

import android.app.DatePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thomas.base.navigation.DefaultNavigator
import com.thomas.base.navigation.Navigator
import com.thomas.base.ui.HandleEvents
import com.thomas.base.ui.collectUIState
import com.thomas.components.ComboBox
import com.thomas.components.materialConfig
import java.util.Calendar


/**
 * Booking screen showing a 4-step flow: LOCATION, DATETIME, ROOM, CONFIRM
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    navigator: Navigator,
    viewModel: BookingContract = hiltViewModel<BookingViewModel>()
) {
    BackHandler { viewModel.back() }
    // UI state
    val uiState = viewModel.collectUIState()
    viewModel.HandleEvents(navigator)
    val data = uiState.data

    // observe current step from ViewModel's step flow
    val step by viewModel.state.collectAsStateWithLifecycle(initialValue = viewModel.currentState)

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (step) {
            BookingStep.LOCATION -> LocationStep(viewModel = viewModel, data = data)
            BookingStep.DATETIME -> DateTimeStep(
                viewModel = viewModel,
                data = data,
                context = context
            )

            BookingStep.ROOM -> RoomStep(viewModel = viewModel, data = data)
            BookingStep.CONFIRM -> ConfirmStep(
                viewModel = viewModel,
                data = data
            )
        }
    }
}

@Composable
private fun LocationStep(viewModel: BookingContract, data: BookingData?) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        ComboBox(
            items = data?.countries ?: emptyList(),
            selectedItem = data?.countries?.firstOrNull { it.id == data.countryId },
            onItemSelected = { item ->
                item?.id?.let { viewModel.selectCountry(it) }
            },
            config = materialConfig().copy(
                placeholder = "Select a country"
            ),
        )

        ComboBox(
            items = data?.buildings ?: emptyList(),
            selectedItem = data?.buildings?.firstOrNull { it.id == data.buildingId },
            onItemSelected = { item ->
                item?.id?.let { viewModel.selectBuilding(it) }
            },
            config = materialConfig().copy(
                placeholder = "Select a building"
            ),
        )

        ComboBox(
            items = data?.floors ?: emptyList(),
            selectedItem = data?.floors?.firstOrNull { it.id == data.floorId },
            onItemSelected = { item ->
                item?.id?.let { viewModel.selectFloor(it) }
            },
            config = materialConfig().copy(
                placeholder = "Select a floor"
            ),
        )
    }
}

@Composable
private fun DateTimeStep(
    viewModel: BookingContract,
    data: BookingData?,
    context: android.content.Context
) {
    val cal = Calendar.getInstance()
    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val day = cal.get(Calendar.DAY_OF_MONTH)

    Button(onClick = {
        DatePickerDialog(context, { _, y, m, d ->
            val dateStr = "%04d-%02d-%02d".format(y, m + 1, d)
            viewModel.selectDateTime(dateStr)
        }, year, month, day).show()
    }) {
        Text(text = data?.dateTime ?: "Select date")
    }
}

@Composable
private fun RoomStep(viewModel: BookingContract, data: BookingData?) {
    ComboBox(
        items = data?.rooms ?: emptyList(),
        selectedItem = data?.rooms?.firstOrNull { it.id == data.roomId },
        onItemSelected = { item ->
            item?.id?.let { viewModel.selectRoom(it) }
        },
        config = materialConfig().copy(
            placeholder = "Select a room"
        ),
    )
}

@Composable
private fun ConfirmStep(viewModel: BookingContract, data: BookingData?) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Country: ${data?.countries?.firstOrNull { it.id == data.countryId }?.name ?: data?.countryId ?: "-"}")
        Text(text = "Building: ${data?.buildings?.firstOrNull { it.id == data.buildingId }?.name ?: data?.buildingId ?: "-"}")
        Text(text = "Floor: ${data?.floors?.firstOrNull { it.id == data.floorId }?.name ?: data?.floorId ?: "-"}")
        Text(text = "Date: ${data?.dateTime ?: "-"}")
        Text(text = "Room: ${data?.rooms?.firstOrNull { it.id == data.roomId }?.name ?: data?.roomId ?: "-"}")

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = { viewModel.back() }) {
                Text("Cancel")
            }

            Button(onClick = { viewModel.confirm() }) {
                Text("Confirm")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewBookingScreen() {
    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            BookingScreen(navigator = DefaultNavigator(), viewModel = FakeBookingContract())
        }
    }
}
