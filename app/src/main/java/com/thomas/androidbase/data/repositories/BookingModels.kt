package com.thomas.androidbase.data.repositories

/** Simple models used by BookingRepository fake implementation */
data class Country(val id: String, val name: String) {
    override fun toString() = name
}

data class Building(val id: String, val name: String) {
    override fun toString() = name
}

data class Floor(val id: String, val name: String) {
    override fun toString() = name
}

data class Room(val id: String, val name: String, val capacity: Int) {
    override fun toString() = name
}

