package ru.bear.weatherjusttogether.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.bear.weatherjusttogether.data.local.entities.DailyWeathertDto
import ru.bear.weatherjusttogether.data.local.entities.HourlyWeatherDto
import ru.bear.weatherjusttogether.data.local.entities.LastSavedCityDto
import ru.bear.weatherjusttogether.data.local.entities.TodayWeatherDto

@Dao
interface WeatherDao {

    /*** 🔹 Храним последний сохраненный город ***/
    @Query("SELECT city FROM last_saved_city LIMIT 1")
    fun getLastSavedCityFlow(): Flow<String?> // ✅ Теперь это Flow

    @Query("SELECT city FROM last_saved_city LIMIT 1")
    suspend fun getLastSavedCity(): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLastCity(city: LastSavedCityDto)

    /*** 🔹 Методы работы с погодой ***/
    @Query("SELECT * FROM today_forecast WHERE city = :city LIMIT 1")
    suspend fun getWeather(city: String): TodayWeatherDto?

    @Query("SELECT * FROM hourly_forecast")
    suspend fun getHourlyForecast(): List<HourlyWeatherDto>

    @Query("SELECT * FROM daily_forecast")
    suspend fun getDailyForecast(): List<DailyWeathertDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: TodayWeatherDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecast(hourlyForecast: List<HourlyWeatherDto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyForecast(forecast: List<DailyWeathertDto>)

    /*** 🔹 Методы удаления данных ***/
    @Query("DELETE FROM today_forecast WHERE city = :city")
    suspend fun deleteWeather(city: String)

    @Query("DELETE FROM hourly_forecast")
    suspend fun deleteHourlyForecast()

    @Query("DELETE FROM daily_forecast")
    suspend fun deleteForecast()

    @Query("DELETE FROM last_saved_city") // ✅ Очистка последнего сохраненного города
    suspend fun deleteLastCity()
}
