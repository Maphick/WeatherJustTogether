package ru.bear.weatherjusttogether.data.remote.network.models

data class DailyWeatherApi(
    val date: String,
    val date_epoch: Long,
    val day: OneDay,
    val astro: Astro,
    val hour: List<HourlyWeatherApi>
)
