package com.thomas.androidbase.features.booking

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thomas.base.navigation.Navigator
import com.thomas.base.ui.collectUIState
import java.util.Calendar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

/**
 * Booking screen showing a 4-step flow: LOCATION, DATETIME, ROOM, CONFIRM
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    navigator: Navigator,
    viewModel: BookingContract = hiltViewModel<BookingViewModel>()
) {
    // UI state
    val uiState = viewModel.collectUIState()
    val data = uiState.data

    // observe current step from ViewModel's step flow
    val step by viewModel.step.collectAsStateWithLifecycle(initialValue = viewModel.currentStep)

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Booking - ${step.name}")

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
                data = data,
                navigator = navigator
            )
        }
    }
}

@Composable
private fun LocationStep(viewModel: BookingContract, data: BookingData?) {
    var countryExpanded by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf<String?>(data?.countryId) }

    OutlinedButton(onClick = { countryExpanded = true }, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = data?.countries?.firstOrNull { it.id == selectedCountry }?.name
                ?: "Select country"
        )
    }
    DropdownMenu(expanded = countryExpanded, onDismissRequest = { countryExpanded = false }) {
        data?.countries?.forEach { c ->
            DropdownMenuItem(text = { Text(c.name) }, onClick = {
                countryExpanded = false
                selectedCountry = c.id
                viewModel.selectCountry(c.id)
            })
        }
    }

    var buildingExpanded by remember { mutableStateOf(false) }
    var selectedBuilding by remember { mutableStateOf<String?>(data?.buildingId) }
    OutlinedButton(
        onClick = { buildingExpanded = true },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = data?.buildings?.firstOrNull { it.id == selectedBuilding }?.name
                ?: "Select building"
        )
    }
    DropdownMenu(expanded = buildingExpanded, onDismissRequest = { buildingExpanded = false }) {
        data?.buildings?.forEach { b ->
            DropdownMenuItem(text = { Text(b.name) }, onClick = {
                buildingExpanded = false
                selectedBuilding = b.id
                viewModel.selectBuilding(b.id)
            })
        }
    }

    var floorExpanded by remember { mutableStateOf(false) }
    var selectedFloor by remember { mutableStateOf<String?>(data?.floorId) }
    OutlinedButton(
        onClick = { floorExpanded = true },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(text = data?.floors?.firstOrNull { it.id == selectedFloor }?.name ?: "Select floor")
    }
    DropdownMenu(expanded = floorExpanded, onDismissRequest = { floorExpanded = false }) {
        data?.floors?.forEach { f ->
            DropdownMenuItem(text = { Text(f.name) }, onClick = {
                floorExpanded = false
                selectedFloor = f.id
                viewModel.selectFloor(f.id)
            })
        }
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
    var roomExpanded by remember { mutableStateOf(false) }
    var selectedRoom by remember { mutableStateOf<String?>(data?.roomId) }
    OutlinedButton(onClick = { roomExpanded = true }, modifier = Modifier.fillMaxWidth()) {
        Text(text = data?.rooms?.firstOrNull { it.id == selectedRoom }?.name ?: "Select room")
    }
    DropdownMenu(expanded = roomExpanded, onDismissRequest = { roomExpanded = false }) {
        data?.rooms?.forEach { r ->
            DropdownMenuItem(text = { Text(r.name) }, onClick = {
                roomExpanded = false
                selectedRoom = r.id
                viewModel.selectRoom(r.id)
            })
        }
    }
}

@Composable
private fun ConfirmStep(viewModel: BookingContract, data: BookingData?, navigator: Navigator) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Country: ${data?.countries?.firstOrNull { it.id == data.countryId }?.name ?: data?.countryId ?: "-"}")
        Text(text = "Building: ${data?.buildings?.firstOrNull { it.id == data.buildingId }?.name ?: data?.buildingId ?: "-"}")
        Text(text = "Floor: ${data?.floors?.firstOrNull { it.id == data.floorId }?.name ?: data?.floorId ?: "-"}")
        Text(text = "Date: ${data?.dateTime ?: "-"}")
        Text(text = "Room: ${data?.rooms?.firstOrNull { it.id == data.roomId }?.name ?: data?.roomId ?: "-"}")

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = { navigator.back() }) {
                Text("Cancel")
            }

            Button(onClick = { viewModel.confirm() }) {
                Text("Confirm")
            }
        }
    }
}

