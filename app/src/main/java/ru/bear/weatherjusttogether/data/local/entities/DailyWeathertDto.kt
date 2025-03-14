package ru.bear.weatherjusttogether.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_forecast")
data class DailyWeathertDto(
    @PrimaryKey val date_epoch: Long,
    val date: String,
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val avgtemp_c: Double,
    val maxwind_kph: Double,
    val totalprecip_mm: Double,
    val avghumidity: Int,
    val condition_text: String,
    val condition_icon: String,
    val condition_code: Int, // Добавлен код погодного состояния
    val sunrise: String,
    val sunset: String,
    val moon_phase: String,
    val moon_illumination: String
)
