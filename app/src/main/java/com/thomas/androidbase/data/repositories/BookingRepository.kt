package com.thomas.androidbase.data.repositories

import com.thomas.base.domain.Result
import javax.inject.Inject

/**
 * Repository contract that provides lists required for booking a room.
 * This implementation below returns fake static data suitable for tests and previews.
 */
interface BookingRepository {
    suspend fun getCountries(): Result<List<Country>>
    suspend fun getBuildings(countryId: String): Result<List<Building>>
    suspend fun getFloors(buildingId: String): Result<List<Floor>>
    suspend fun getRooms(floorId: String): Result<List<Room>>
}

class BookingRepositoryImpl @Inject constructor() : BookingRepository {
    private val fakeData by lazy { createFakeData() }

    override suspend fun getCountries(): Result<List<Country>> {
        return Result.Success(fakeData.countries)
    }

    override suspend fun getBuildings(countryId: String): Result<List<Building>> {
        return Result.Success(fakeData.buildings)
    }

    override suspend fun getFloors(buildingId: String): Result<List<Floor>> {
        return Result.Success(fakeData.floors)
    }

    override suspend fun getRooms(floorId: String): Result<List<Room>> {
        return Result.Success(fakeData.rooms)
    }

    private data class FakeData(
        val countries: List<Country>,
        val buildings: List<Building>,
        val floors: List<Floor>,
        val rooms: List<Room>,
    )

    private fun createFakeData(): FakeData {
        val countries = listOf(
            Country("us", "United States"),
            Country("cn", "China"),
            Country("uk", "United Kingdom")
        )

        val buildings = (1..20).map { Building("b-$it", "Building $it") }

        val floors = (1..20).map { Floor("f-$it", "Floor $it") }

        val rooms = (1..20).map { Room("r-$it", "Room $it", 20) }

        return FakeData(countries, buildings, floors, rooms)
    }
}

