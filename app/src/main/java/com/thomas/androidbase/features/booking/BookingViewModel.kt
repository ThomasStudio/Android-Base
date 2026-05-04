package com.thomas.androidbase.features.booking

import androidx.lifecycle.SavedStateHandle
import com.thomas.androidbase.data.repositories.BookingRepository
import com.thomas.base.domain.Result
import com.thomas.base.viewmodel.BaseJourneyViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
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

    override fun onJourneyStepChanged(index: Int, step: BookingStep) {
        // Update UI state when step changes (mark success and keep data)
        val data = currentData() ?: BookingData()
        updateData { data }

        // load lists as user navigates
        when (step) {
            BookingStep.LOCATION -> loadCountries()
            BookingStep.DATETIME -> {
                // ensure buildings/floors are present for selected country/building
                data.countryId?.let { loadBuildings(it) }
            }

            BookingStep.ROOM -> data.floorId?.let {
                loadRooms(
                    data.countryId ?: "",
                    data.buildingId ?: "",
                    it
                )
            }

            BookingStep.CONFIRM -> {
                // nothing to load
            }
        }
    }

    override fun selectCountry(countryId: String) {
        updateData { copy(countryId = countryId) }
    }

    override fun selectBuilding(buildingId: String) {
        updateData { copy(buildingId = buildingId) }
    }

    override fun selectFloor(floorId: String) {
        updateData { copy(floorId = floorId) }
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
            when (val r = bookingRepository.getCountries()) {
                is Result.Success -> updateData { copy(countries = r.data) }
                is Result.Error -> sendMessage("Failed to load countries: ${r.message}")
            }
        }
    }

    fun loadBuildings(countryId: String) {
        scope.launch {
            when (val r = bookingRepository.getBuildings(countryId)) {
                is Result.Success -> updateData { copy(buildings = r.data) }
                is Result.Error -> sendMessage("Failed to load buildings: ${r.message}")
            }
        }
    }

    fun loadFloors(countryId: String, buildingId: String) {
        scope.launch {
            when (val r = bookingRepository.getFloors(countryId, buildingId)) {
                is Result.Success -> updateData { copy(floors = r.data) }
                is Result.Error -> sendMessage("Failed to load floors: ${r.message}")
            }
        }
    }

    fun loadRooms(countryId: String, buildingId: String, floorId: String) {
        scope.launch {
            when (val r = bookingRepository.getRooms(countryId, buildingId, floorId)) {
                is Result.Success -> updateData { copy(rooms = r.data) }
                is Result.Error -> sendMessage("Failed to load rooms: ${r.message}")
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

