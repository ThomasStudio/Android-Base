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
        return Result.Success(fakeData.buildings[countryId] ?: emptyList())
    }

    override suspend fun getFloors(buildingId: String): Result<List<Floor>> {
        return Result.Success(fakeData.floors[buildingId] ?: emptyList())
    }

    override suspend fun getRooms(floorId: String): Result<List<Room>> {
        return Result.Success(fakeData.rooms[floorId] ?: emptyList())
    }

    private data class FakeData(
        val countries: List<Country>,
        val buildings: Map<String, List<Building>>,
        val floors: Map<String, List<Floor>>,
        val rooms: Map<String, List<Room>>
    )

    private fun createFakeData(): FakeData {
        val countries = listOf(
            Country("us", "United States"),
            Country("cn", "China"),
            Country("uk", "United Kingdom")
        )

        val buildingsUs = listOf(
            Building("us-a", "US - Tower A"),
            Building("us-b", "US - Tower B")
        )
        val buildingsCn = listOf(
            Building("cn-a", "CN - Office 1"),
            Building("cn-b", "CN - Office 2")
        )

        val floorsUsA = listOf(Floor("us-a-1", "Floor 1"), Floor("us-a-2", "Floor 2"))
        val floorsUsB = listOf(Floor("us-b-1", "Floor 1"))

        val roomsUsA1 = listOf(Room("r101", "Room 101", 4), Room("r102", "Room 102", 8))
        val roomsUsA2 = listOf(Room("r201", "Room 201", 6))
        val roomsUsB1 = listOf(Room("rB101", "Room B101", 10))

        val buildingsMap = mapOf(
            "us" to buildingsUs,
            "cn" to buildingsCn,
            "uk" to listOf(Building("uk-a", "UK - HQ"))
        )

        val floorsMap = mapOf(
            "us-a" to floorsUsA,
            "us-b" to floorsUsB,
            "cn-a" to listOf(Floor("cn-a-1", "Floor 1")),
            "cn-b" to listOf(Floor("cn-b-1", "Floor 1"))
        )

        val roomsMap = mapOf(
            "us-a-1" to roomsUsA1,
            "us-a-2" to roomsUsA2,
            "us-b-1" to roomsUsB1,
            "cn-a-1" to listOf(Room("cn-a-101", "CN Room 101", 4))
        )

        return FakeData(countries, buildingsMap, floorsMap, roomsMap)
    }
}

