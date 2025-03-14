package ru.bear.weatherjusttogether.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.bear.weatherjusttogether.data.local.dao.WeatherDao
import ru.bear.weatherjusttogether.data.local.entities.DailyWeathertDto
import ru.bear.weatherjusttogether.data.local.entities.HourlyWeatherDto
import ru.bear.weatherjusttogether.data.local.entities.TodayWeatherDto
import ru.bear.weatherjusttogether.data.local.entities.LastSavedCityDto

@Database(
    entities = [TodayWeatherDto::class, HourlyWeatherDto::class, DailyWeathertDto::class, LastSavedCityDto::class], // Добавляем все DTO
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        fun getDatabase(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_database"
                ).fallbackToDestructiveMigration() // Удаляет старые данные при изменении схемы
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}