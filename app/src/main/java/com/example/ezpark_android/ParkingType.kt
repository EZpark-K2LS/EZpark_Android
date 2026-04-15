package com.example.ezpark_android

enum class ParkingType(val iconRes: Int? = null) {
    GENERAL(),
    WOMAN(R.drawable.woman_icon),
    ELECTRIC(R.drawable.electric_car_icon),
    DISABLED(R.drawable.disabled_person_icon),
    LIGHT_CAR(R.drawable.light_car_icon)
}
