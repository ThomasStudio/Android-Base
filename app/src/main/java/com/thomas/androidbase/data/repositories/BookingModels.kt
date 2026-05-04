package com.thomas.androidbase.data.repositories

/** Simple models used by BookingRepository fake implementation */
data class Country(val id: String, val name: String)
data class Building(val id: String, val name: String)
data class Floor(val id: String, val name: String)
data class Room(val id: String, val name: String, val capacity: Int)

