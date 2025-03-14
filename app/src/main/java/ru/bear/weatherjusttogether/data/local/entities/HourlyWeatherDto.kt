package ru.bear.weatherjusttogether.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hourly_forecast")
data class HourlyWeatherDto(
    @PrimaryKey val time_epoch: Long,  // Временная метка (уникальный идентификатор)
    val time: String,                  // Время в формате "YYYY-MM-DD HH:MM"
    val temp_c: Double,                 // Температура (°C)
    val temp_f: Double,                 // Температура (°F)
    val is_day: Int,                    // 1 = День, 0 = Ночь
    val condition_text: String,         // Описание погоды
    val condition_icon: String,         // URL иконки
    val condition_code: Int,            // Код состояния погоды
    val wind_mph: Double,               // Скорость ветра (миль/ч)
    val wind_kph: Double,               // Скорость ветра (км/ч)
    val wind_degree: Int,               // Направление ветра в градусах
    val wind_dir: String,               // Направление ветра (N, S, E, W)
    val pressure_mb: Double,            // Давление (мбар)
    val pressure_in: Double,            // Давление (дюймы рт. ст.)
    val precip_mm: Double,              // Осадки (мм)
    val precip_in: Double,              // Осадки (дюймы)
    val snow_cm: Double,                // Снег (см)
    val humidity: Int,                  // Влажность (%)
    val cloud: Int,                     // Облачность (%)
    val feelslike_c: Double,            // Ощущаемая температура (°C)
    val feelslike_f: Double,            // Ощущаемая температура (°F)
    val windchill_c: Double,            // Ощущаемая температура из-за ветра (°C)
    val windchill_f: Double,            // Ощущаемая температура из-за ветра (°F)
    val heatindex_c: Double,            // Индекс жары (°C)
    val heatindex_f: Double,            // Индекс жары (°F)
    val dewpoint_c: Double,             // Точка росы (°C)
    val dewpoint_f: Double,             // Точка росы (°F)
    val will_it_rain: Int,              // 1 = Да, 0 = Нет (будет ли дождь)
    val chance_of_rain: Int,            // Вероятность дождя (%)
    val will_it_snow: Int,              // 1 = Да, 0 = Нет (будет ли снег)
    val chance_of_snow: Int,            // Вероятность снега (%)
    val vis_km: Double,                 // Видимость (км)
    val vis_miles: Double,              // Видимость (мили)
    val gust_mph: Double,               // Порывы ветра (миль/ч)
    val gust_kph: Double,               // Порывы ветра (км/ч)
    val uv: Double                      // УФ-индекс
)

