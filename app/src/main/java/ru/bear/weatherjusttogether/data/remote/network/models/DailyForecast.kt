package ru.bear.weatherjusttogether.data.remote.network.models

data class DailyForecast(
    val forecastday: List<DailyWeatherApi>
)