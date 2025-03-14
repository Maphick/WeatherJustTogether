package ru.bear.weatherjusttogether.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.bear.weatherjusttogether.data.local.dao.WeatherDao
import ru.bear.weatherjusttogether.data.local.entities.LastSavedCityDto
import ru.bear.weatherjusttogether.data.mapper.DailyWeathertMapper
import ru.bear.weatherjusttogether.data.mapper.HourlyWeathertMapper
import ru.bear.weatherjusttogether.data.mapper.TodayWeathertMapper
import ru.bear.weatherjusttogether.data.repository.API.API_KEY
import ru.bear.weatherjusttogether.domain.repository.WeatherRepository
import ru.bear.weatherjusttogether.data.remote.network.api.WeatherApi
import ru.bear.weatherjusttogether.data.remote.network.models.Location
import ru.bear.weatherjusttogether.domain.models.DailyWeatherDomain
import ru.bear.weatherjusttogether.domain.models.HourlyWeatherDomain
import ru.bear.weatherjusttogether.domain.models.TodayWeatherDomain
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao
) : WeatherRepository {

    override suspend fun saveLastCity(city: String) {
        weatherDao.saveLastCity(LastSavedCityDto(city))
    }

    override suspend fun getLastSavedCity(): String? {
        return weatherDao.getLastSavedCity()
    }


    /**
     * Получение последнего сохраненного города как `Flow`.
     * Теперь поток обновляется автоматически.
     */
    override fun getLastSavedCityFlow(): Flow<String?> =
        weatherDao.getLastSavedCityFlow()
            .flowOn(Dispatchers.IO)
            .catch { e -> Log.e("WeatherRepository", "Error fetching last city: ${e.message}") }

    /**
     *  Получение текущей погоды.
     * - Если есть интернет — загружаем из API и кешируем в Room.
     * - Если нет интернета — загружаем из Room.
     */
    override suspend fun getWeather(city: String): TodayWeatherDomain? {
        return try {
            val weatherResponse = weatherApi.getCurrentWeather(API_KEY, city, "ru")
            val todayWeather = TodayWeathertMapper.mapApiToDomain(weatherResponse)

            // Сохраняем в Room
            weatherDao.insertWeather(TodayWeathertMapper.mapDomainToDto(todayWeather))
            saveLastCity(city) // ✅ Сохраняем город в Room только здесь

            todayWeather
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error fetching weather from API: ${e.message}")

            // Если нет интернета, загружаем данные из Room
            weatherDao.getWeather(city)?.let { TodayWeathertMapper.mapDtoToDomain(it) }
        }
    }

    /**
     * Получение списка предложенных городов.
     */
    override suspend fun getCitySuggestions(query: String): List<Location> {
        return try {
            weatherApi.getCitySuggestions(API_KEY, query, "ru")
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error fetching city suggestions: ${e.message}")
            emptyList()
        }
    }

    /**
     *   Получение прогноза на 7 дней.
     * - Если есть интернет — загружаем из API и кешируем в Room.
     * - Если нет интернета — загружаем данные из Room.
     */
    override suspend fun getDailyForecast(city: String): List<DailyWeatherDomain> {
        return try {
            val forecastResponse = weatherApi.getForecast(API_KEY, city, "7", "ru")
            val forecastDays = forecastResponse.forecast.forecastday.map {
                DailyWeathertMapper.mapApiToDailyWeather(it)
            }

            weatherDao.insertDailyForecast(forecastDays.map { DailyWeathertMapper.mapDomainToDto(it) })
            forecastDays
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error fetching daily forecast: ${e.message}")
            weatherDao.getDailyForecast().map { DailyWeathertMapper.mapDtoToDomain(it) }
        }
    }

    /**
     *   Получение почасового прогноза.
     * - Если есть интернет — загружаем из API и кешируем в Room.
     * - Если нет интернета — загружаем данные из Room.
     */
    override suspend fun getHourlyForecast(city: String): List<HourlyWeatherDomain> {
        return try {
            val forecastResponse = weatherApi.getForecast(API_KEY, city, "1", "ru")
            val hourlyData = forecastResponse.forecast.forecastday[0].hour
                .map { HourlyWeathertMapper.mapApiToDomain(it) }

            weatherDao.insertHourlyForecast(hourlyData.map { HourlyWeathertMapper.mapDomainToDto(it) })
            hourlyData
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error fetching hourly forecast: ${e.message}")
            weatherDao.getHourlyForecast().map { HourlyWeathertMapper.mapDtoToDomain(it) }
        }
    }

    /**
     * Очистка кеша погоды.
     */
    override suspend fun clearWeather(city: String) {
        weatherDao.deleteWeather(city)
        weatherDao.deleteHourlyForecast()
        weatherDao.deleteForecast()
    }
}
