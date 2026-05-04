package com.thomas.androidbase.features.booking

import com.thomas.androidbase.data.repositories.Building
import com.thomas.androidbase.data.repositories.Country
import com.thomas.androidbase.data.repositories.Floor
import com.thomas.androidbase.data.repositories.Room
import com.thomas.base.viewmodel.BaseJourneyContract
import com.thomas.base.viewmodel.DefaultJourneyContract
import com.thomas.base.viewmodel.Status
import kotlin.String

/** UI contract exposed to Booking UI */
interface BookingContract : BaseJourneyContract<BookingStep, BookingData> {
    // Minimal UI surface: selection actions and confirm.
    fun selectCountry(countryId: String)
    fun selectBuilding(buildingId: String)
    fun selectFloor(floorId: String)

    fun selectDateTime(dateTime: String)
    fun selectRoom(roomId: String)
    fun confirm()
}


/**
 * Booking steps and data for a room booking journey.
 */
enum class BookingStep {
    LOCATION, DATETIME, ROOM, CONFIRM
}

data class BookingData(
    val countryId: String? = null,
    val buildingId: String? = null,
    val floorId: String? = null,
    val dateTime: String? = null,
    val roomId: String? = null,

    // lists populated by repository
    val countries: List<Country> = emptyList(),
    val buildings: List<Building> = emptyList(),
    val floors: List<Floor> = emptyList(),
    val rooms: List<Room> = emptyList()
)

class FakeBookingContract : DefaultJourneyContract<BookingStep, BookingData>(), BookingContract {
    override val steps: List<BookingStep>
        get() = BookingStep.entries.toList()

    override fun initialStatus() = Status.SUCCESS
    override fun initialData() = BookingData(
        countryId = "1",
        buildingId = "1",
        floorId = "1",
        dateTime = "2026-05-06",
        roomId = "1",
        countries = listOf(Country("1", "France"), Country("2", "Germany")),
        buildings = (0..10).map { Building(it.toString(), "Building $it") },
        floors = (0..10).map { Floor(it.toString(), "Floor $it") },
        rooms = (0..10).map { Room(it.toString(), "Room $it", 20) }
    )

    override fun selectCountry(countryId: String) {
        updateData { copy(countryId = countryId) }
    }

    override fun selectBuilding(buildingId: String) {
        updateData { copy(buildingId = buildingId) }
    }

    override fun selectFloor(floorId: String) {
        updateData { copy(floorId = floorId) }
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

    override fun confirm() {}

    override fun back() {
        previous()
    }
}
