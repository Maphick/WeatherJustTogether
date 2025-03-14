package ru.bear.weatherjusttogether.data.remote.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.bear.weatherjusttogether.data.remote.network.models.ForecastResponse
import ru.bear.weatherjusttogether.data.remote.network.models.Location
import ru.bear.weatherjusttogether.data.remote.network.models.TodayWeatherApi

// API-интерфейс
interface WeatherApi {
    /**
     * Получение текущей погоды по названию города
     * @param apiKey - Ключ API
     * @param city - Название города или координаты (широта, долгота)
     * @param lang - Язык ответа (по умолчанию "ru" - русский)
     */
    @GET("v1/current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") city: String,
        @Query("lang") lang: String = "ru"
    ): TodayWeatherApi

    /**
     * Поиск городов по введенному запросу
     * Используется для автозаполнения поисковой строки
     * @param apiKey - Ключ API
     * @param query - Текст запроса (например, название города)
     * @param lang - Язык ответа (по умолчанию "ru" - русский)
     */
    @GET("v1/search.json")
    suspend fun getCitySuggestions(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("lang") lang: String = "ru"
    ): List<Location>

    /**
     * Получение прогноза погоды
     * Включает:
     * - Прогноз на 1-14 дней (включая почасовой прогноз)
     * - Астрономические данные
     * - Информацию о качестве воздуха
     * @param apiKey - Ключ API
     * @param city - Название города или координаты (широта, долгота)
     * @param days - Количество дней прогноза (по умолчанию "14")
     * @param lang - Язык ответа (по умолчанию "ru" - русский)
     * @param alerts - Флаг для получения данных об оповещениях (по умолчанию false)
     * @param aqi - Флаг для получения данных о качестве воздуха (по умолчанию нет)
     */
    @GET("v1/forecast.json")
    suspend fun getForecast(
        @Query("key") apiKey: String,
        @Query("q") city: String,
        @Query("days") days: String = "14",
        @Query("lang") lang: String = "ru",
        @Query("alerts") alerts: String = "no",
        @Query("aqi") aqi: String = "no"
    ): ForecastResponse
}




