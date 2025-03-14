package ru.bear.weatherjusttogether.domain.models

data class TodayWeatherDomain(
    val city: String,           // Город
    val region: String,         // Регион
    val country: String,        // Страна
    val lat: Double,            // Широта
    val lon: Double,            // Долгота
    val localtime: String,      // Локальное время
    val temp_c: Double,         // Температура (°C)
    val temp_f: Double,         // Температура (°F)
    val is_day: Int,            // 1 = День, 0 = Ночь
    val conditionText: String,  // Описание погоды
    val conditionIcon: String,  // URL иконки
    val conditionCode: Int,     // Код состояния погоды
    val wind_mph: Double,       // Скорость ветра (миль/ч)
    val wind_kph: Double,       // Скорость ветра (км/ч)
    val wind_degree: Int,       // Направление ветра в градусах
    val wind_dir: String,       // Направление ветра (N, S, E, W)
    val pressure_mb: Double,    // Давление (мбар)
    val pressure_in: Double,    // Давление (дюймы рт. ст.)
    val precip_mm: Double,      // Осадки (мм)
    val precip_in: Double,      // Осадки (дюймы)
    val humidity: Int,          // Влажность (%)
    val cloud: Int,             // Облачность (%)
    val feelslike_c: Double,    // Ощущаемая температура (°C)
    val feelslike_f: Double,    // Ощущаемая температура (°F)
    val vis_km: Double,         // Видимость (км)
    val vis_miles: Double,      // Видимость (мили)
    val uv: Double,             // УФ-индекс
    val gust_mph: Double,       // Порывы ветра (миль/ч)
    val gust_kph: Double,       // Порывы ветра (км/ч)
    val lastUpdated: String       // Время последнего обновления (timestamp)
)
