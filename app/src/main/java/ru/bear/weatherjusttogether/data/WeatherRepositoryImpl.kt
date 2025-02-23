package ru.bear.weatherjusttogether.data

import ru.bear.weatherjusttogether.data.API.API_KEY
import ru.bear.weatherjusttogether.network.models.Hour
import ru.bear.weatherjusttogether.network.api.WeatherApi
import ru.bear.weatherjusttogether.network.models.ForecastResponse
import ru.bear.weatherjusttogether.network.models.Location
import ru.bear.weatherjusttogether.network.models.WeatherResponse
import javax.inject.Inject

// Конструктор с @Inject: Позволяет Dagger автоматически создать экземпляр WeatherRepositoryImpl,
// используя WeatherApi, без необходимости прописывать @Provides в AppModule.
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {


    override suspend fun getWeather(city: String): WeatherResponse? {
        return try {
            weatherApi.getCurrentWeather(API_KEY, city,  "ru")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    override suspend fun getCitySuggestions(query: String): List<Location> {
        return try {
            weatherApi.getCitySuggestions(API_KEY, query,  "ru")
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getForecast(city: String): ForecastResponse? {
        return try {
            weatherApi.getForecast(API_KEY, city, days = "14",  lang = "ru")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Получение почасового прогноза на текущий день
     * Данные берутся из первого элемента forecastday -> hour
     */
    override suspend fun getHourlyForecast(city: String): List<Hour>? {
        return try {
            val forecastResponse = weatherApi.getForecast(API_KEY, city, days = "1", lang = "ru")
            forecastResponse?.forecast?.forecastday?.get(0)?.hour
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}