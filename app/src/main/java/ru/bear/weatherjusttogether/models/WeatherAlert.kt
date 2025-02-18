package ru.bear.weatherjusttogether.models

data class WeatherAlert(
    val title: String,      // Название предупреждения
    val severity: String,   // Серьёзность (Severe, Moderate, Minor)
    val regions: String,    // Регионы
    val startTime: String,  // Время начала
    val endTime: String,    // Время окончания
    val description: String // Полное описание
)
