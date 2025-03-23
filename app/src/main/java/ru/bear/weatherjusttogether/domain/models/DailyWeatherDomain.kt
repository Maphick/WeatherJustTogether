package ru.bear.weatherjusttogether.domain.models

data class DailyWeatherDomain(
    val date: String,               // Дата (например, "2024-03-10")
    val date_epoch: Long,           // Временная метка даты
    val maxtemp_c: Double,          // Максимальная температура (°C)
    val mintemp_c: Double,          // Минимальная температура (°C)
    val avgtemp_c: Double,          // Средняя температура (°C)
    val maxwind_kph: Double,        // Максимальная скорость ветра (км/ч)
    val totalprecip_mm: Double,     // Общее количество осадков (мм)
    val avghumidity: Int,           // Средняя влажность (%)
    val conditionText: String,      // Описание погоды (например, "Ясно")
    val conditionIcon: String,      // URL иконки
    val conditionCode: Int,         // Код погодного состояния
    val sunrise: String,            // Время восхода солнца
    val sunset: String,             // Время захода солнца
    val moon_phase: String,         // Фаза луны
    val moon_illumination: String   // Освещенность луны (%)
)