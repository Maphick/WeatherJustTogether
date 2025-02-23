package ru.bear.weatherjusttogether.models

import ru.bear.weatherjusttogether.network.models.Condition

/*
data class HourlyWeather(
    val time: String,      // Время прогноза (например, "01:00")
    val temperature: Int,  // Температура в градусах Цельсия
    val iconRes: Int,      // Ресурс иконки (R.drawable.ic_rain)
    val humidity: Int,     // Влажность (например, 80%)
    val rainChance: Int    // Вероятность дождя (например, 40%)
)
*/

data class HourlyWeather(
    val time: String,             // Время, например: "2023-03-15 14:00"
    val temp_c: Double,            // Температура в градусах Цельсия
    val temp_f: Double,            // Температура в Фаренгейтах
    val is_day: Int,               // 1 = День, 0 = Ночь
    val condition: Condition,      // Объект погодных условий
    val wind_kph: Double,          // Скорость ветра в км/ч
    val wind_mph: Double,          // Скорость ветра в миль/ч
    val wind_dir: String,          // Направление ветра
    val humidity: Int,             // Влажность в процентах
    val cloud: Int,                // Облачность в процентах
    val feelslike_c: Double,       // Ощущаемая температура в градусах Цельсия
    val will_it_rain: Int,         // 1 = Да, 0 = Нет
    val will_it_snow: Int,         // 1 = Да, 0 = Нет
    val chance_of_rain: Int,       // Вероятность дождя в процентах
    val chance_of_snow: Int        // Вероятность снега в процентах
)
