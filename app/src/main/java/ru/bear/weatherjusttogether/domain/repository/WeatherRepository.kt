package ru.bear.weatherjusttogether.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.bear.weatherjusttogether.data.remote.network.models.Location
import ru.bear.weatherjusttogether.domain.models.DailyWeatherDomain
import ru.bear.weatherjusttogether.domain.models.HourlyWeatherDomain
import ru.bear.weatherjusttogether.domain.models.TodayWeatherDomain

// Оптимальный вариант для Android-приложения:
// Репозиторий использует suspend
// В ViewModel используем viewModelScope.launch
// В Fragment подписываемся на LiveData
interface WeatherRepository {
    /**
     * Получение последнего сохраненного города
     */
    suspend fun getLastSavedCity(): String?

    /**
     * Сохранение последнего города в локальное хранилище
     */
    suspend fun saveLastCity(city: String)

    /**
     * Получение текущей погоды.
     * Если интернет доступен, загружаем из API и кешируем в Room.
     * Если интернета нет, загружаем данные из Room.
     */
    suspend fun getWeather(city: String): TodayWeatherDomain?

    /**
     * Очистка кэша погоды для города.
     */
    suspend fun clearWeather(city: String)

    /**
     * Получение списка предложенных городов из API.
     */
    suspend fun getCitySuggestions(query: String): List<Location>

    /**
     * Получение прогноза на 7 дней.
     * Если интернет доступен, загружаем из API и кешируем в Room.
     * Если интернета нет, загружаем данные из Room.
     */
    suspend fun getDailyForecast(city: String): List<DailyWeatherDomain>

    /**
     * Получение почасового прогноза на сегодня.
     * Если интернет доступен, загружаем из API и кешируем в Room.
     * Если интернета нет, загружаем данные из Room.
     */
    suspend fun getHourlyForecast(city: String): List<HourlyWeatherDomain>
}
