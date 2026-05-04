package com.thomas.androidbase.features.booking

import androidx.lifecycle.SavedStateHandle
import com.thomas.androidbase.Store
import com.thomas.androidbase.data.repositories.BookingRepository
import com.thomas.base.domain.Result
import com.thomas.base.viewmodel.BaseJourneyViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookingViewModel @Inject constructor(
    private val repo: BookingRepository,
    savedStateHandle: SavedStateHandle
) : BaseJourneyViewModel<BookingStep, BookingData>(
    savedStateHandle = savedStateHandle
), BookingContract {
    override val steps
        get() = listOf(
            BookingStep.LOCATION,
            BookingStep.DATETIME,
            BookingStep.ROOM,
            BookingStep.CONFIRM
        )

    init {
        // pre-load countries for the location step
        loadCountries()
    }

    override fun onVisible() {
        updateTitle()
    }

    override fun onStepChanged(newStep: BookingStep, oldStep: BookingStep) {
        updateTitle()
        super.onStepChanged(newStep, oldStep)
    }

    override fun back() {
        if (isLastStep || isFirstStep)
            super.back()
        else
            previous()
    }

    private fun title() = "Booking ${currentIndex + 1}/${totalSteps} - ${currentStep.name} "
    private fun updateTitle() {
        Store.rootVM?.setTitle(title())
    }

    override fun initialStep(): BookingStep {
        return steps.first()
    }

    override fun initialData(): BookingData = BookingData()

    // Validate transitions so users cannot skip required selections
    override fun canTransition(from: BookingStep, to: BookingStep): Boolean {
        val data = currentData() ?: BookingData()
        return when (to) {
            BookingStep.DATETIME -> !data.countryId.isNullOrBlank() && !data.buildingId.isNullOrBlank() && !data.floorId.isNullOrBlank()
            BookingStep.ROOM -> !data.dateTime.isNullOrBlank()
            BookingStep.CONFIRM -> !data.roomId.isNullOrBlank()
            else -> true
        }
    }

    override fun selectCountry(countryId: String) {
        updateData {
            copy(
                countryId = countryId,
                buildingId = null,
                floorId = null,
                buildings = emptyList(),
                floors = emptyList()
            )
        }
        scope.launch {
            when (val buildings = repo.getBuildings(countryId)) {
                is Result.Success -> updateData { copy(buildings = buildings.data) }
                is Result.Error -> sendMessage("Failed to load buildings: ${buildings.message}")
            }
        }
    }

    override fun selectBuilding(buildingId: String) {
        updateData {
            copy(
                buildingId = buildingId,
                floorId = null,
                floors = emptyList()
            )
        }
        scope.launch {
            when (val floors = repo.getFloors(buildingId)) {
                is Result.Success -> updateData { copy(floors = floors.data) }
                is Result.Error -> sendMessage("Failed to load floors: ${floors.message}")
            }
        }
    }

    override fun selectFloor(floorId: String) {
        updateData { copy(floorId = floorId, roomId = null, rooms = emptyList()) }
        scope.launch {
            when (val rooms = repo.getRooms(floorId)) {
                is Result.Success -> updateData { copy(rooms = rooms.data) }
                is Result.Error -> sendMessage("Failed to load floors: ${rooms.message}")
            }
        }

        next()
    }

    override fun selectDateTime(dateTime: String) {
        updateData { copy(dateTime = dateTime) }
        next()
    }

    override fun selectRoom(roomId: String) {
        updateData { copy(roomId = roomId) }
        next()
    }

    // repository-backed loaders
    fun loadCountries() {
        scope.launch {
            when (val r = repo.getCountries()) {
                is Result.Success -> updateData { copy(countries = r.data) }
                is Result.Error -> sendMessage("Failed to load countries: ${r.message}")
            }
        }
    }

    override fun confirm() {
        // In a real implementation you'd call a repository to commit booking.
        val data = currentData()
        sendMessage("Booking confirmed: country=${data?.countryId}, building=${data?.buildingId}, floor=${data?.floorId}, dateTime=${data?.dateTime}, room=${data?.roomId}")
        completeJourney()
    }
}

