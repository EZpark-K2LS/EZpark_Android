package com.ezpark.parking.data.model

data class ParkingLot(
    val id: String,
    val name: String,
    val address: String,
    val distanceMeters: Int,
    val totalSpaces: Int,
    val availableSpaces: Int,
    val floors: List<Floor>,
    val latitude: Double,
    val longitude: Double,
)
