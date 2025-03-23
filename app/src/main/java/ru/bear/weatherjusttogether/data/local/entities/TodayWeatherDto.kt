package ru.bear.weatherjusttogether.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "today_forecast")
data class TodayWeatherDto(
    @PrimaryKey val city: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val localtime: String,
    val last_updated: String,
    val temp_c: Double,
    val temp_f: Double,
    val is_day: Int,
    val condition_code: Int,
    val condition_text: String,
    val condition_icon: String,
    val wind_mph: Double,
    val wind_kph: Double,
    val wind_degree: Int,
    val wind_dir: String,
    val pressure_mb: Double,
    val pressure_in: Double,
    val precip_mm: Double,
    val precip_in: Double,
    val humidity: Int,
    val cloud: Int,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val vis_km: Double,
    val vis_miles: Double,
    val uv: Double,
    val gust_mph: Double,
    val gust_kph: Double,
)
