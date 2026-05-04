package com.thomas.androidbase.features.booking

import com.thomas.androidbase.data.repositories.Building
import com.thomas.androidbase.data.repositories.Country
import com.thomas.androidbase.data.repositories.Floor
import com.thomas.androidbase.data.repositories.Room
import com.thomas.base.viewmodel.BaseDataContract

/** UI contract exposed to Booking UI */
interface BookingContract : BaseDataContract<BookingData> {
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


