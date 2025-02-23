package ru.bear.weatherjusttogether.data

import ru.bear.weatherjusttogether.models.HourlyWeather
import ru.bear.weatherjusttogether.network.models.ForecastResponse
import ru.bear.weatherjusttogether.network.models.Hour
import ru.bear.weatherjusttogether.network.models.Location
import ru.bear.weatherjusttogether.network.models.WeatherResponse

// Оптимальный вариант для Android-приложения:
// Репозиторий использует suspend
// В ViewModel используем viewModelScope.launch
// В Fragment подписываемся на LiveData:
interface WeatherRepository {
    suspend fun getWeather(city: String): WeatherResponse?
    suspend fun getCitySuggestions(query: String): List<Location>
    suspend fun getForecast(city: String): ForecastResponse?
    suspend fun getHourlyForecast(city: String): List<Hour>?
}
