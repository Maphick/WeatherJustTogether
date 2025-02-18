package ru.bear.weatherjusttogether.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.bear.weatherjusttogether.network.models.WeatherResponse

// API-интерфейс
interface WeatherApi {
    @GET("v1/current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") city: String
    ): WeatherResponse
}