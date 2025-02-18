package ru.bear.weatherjusttogether.data

import ru.bear.weatherjusttogether.network.models.WeatherResponse

// Оптимальный вариант для Android-приложения:
// Репозиторий использует suspend
// В ViewModel используем viewModelScope.launch
// В Fragment подписываемся на LiveData:
interface WeatherRepository {
    suspend fun getWeather(city: String): WeatherResponse?
}

/*
interface WeatherRepository {
  fun getWeather(city: String, callback: (WeatherResponse?) -> Unit)
}
 */