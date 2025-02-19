package ru.bear.weatherjusttogether.models

data class HourlyWeather(
    val time: String,      // Время прогноза (например, "01:00")
    val temperature: Int,  // Температура в градусах Цельсия
    val iconRes: Int,      // Ресурс иконки (R.drawable.ic_rain)
    val humidity: Int,     // Влажность (например, 80%)
    val rainChance: Int    // Вероятность дождя (например, 40%)
)