package ru.bear.weatherjusttogether.data.remote.network.models

data class TodayWeatherApi(
    val location: Location,
    val current: TodayForecast
)