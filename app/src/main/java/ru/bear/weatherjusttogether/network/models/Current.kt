package ru.bear.weatherjusttogether.network.models

data class Current(
    val temp_c: Float,
    val condition: Condition,
    val wind_kph: Float,
    val humidity: Int
)