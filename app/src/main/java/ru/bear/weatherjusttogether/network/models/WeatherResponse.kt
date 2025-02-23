package ru.bear.weatherjusttogether.network.models

data class WeatherResponse(
    val location: Location,
    val current: CurrentWeather
)