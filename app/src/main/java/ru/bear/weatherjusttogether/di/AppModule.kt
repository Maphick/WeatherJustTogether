package ru.bear.weatherjusttogether.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.bear.weatherjusttogether.data.local.WeatherDatabase
import ru.bear.weatherjusttogether.data.local.dao.WeatherDao
import ru.bear.weatherjusttogether.domain.repository.WeatherRepository
import ru.bear.weatherjusttogether.data.repository.WeatherRepositoryImpl
import ru.bear.weatherjusttogether.data.remote.network.api.WeatherApi
import ru.bear.weatherjusttogether.utils.LoggingInterceptor
import ru.bear.weatherjusttogether.viewmodel.DailyForecastViewModelFactory
import ru.bear.weatherjusttogether.viewmodel.HourlyForecastViewModelFactory
import ru.bear.weatherjusttogether.viewmodel.SharedViewModel
import ru.bear.weatherjusttogether.viewmodel.TodayForecastViewModelFactory
import javax.inject.Singleton

// Этот модуль будет создавать зависимости (Retrofit, API и Repository).

@Module
object AppModule {

    private const val BASE_URL = "https://api.weatherapi.com/"

    @Provides
    @Singleton
    fun provideApplication(context: Context): Application {
        return context.applicationContext as Application
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(LoggingInterceptor())  // Добавляем свой Interceptor
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }


    /*** 🔹 Подключаем Room ***/
    @Provides
    @Singleton
    fun provideDatabase(context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java,
            "weather_database"
        ).fallbackToDestructiveMigration()
            .build()
    }


    //  Используем единый WeatherDao вместо нескольких
    @Provides
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao {
        return database.weatherDao()
    }

    /*** 🔹 Подключаем WeatherRepository в Dagger ***/
    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: WeatherApi,
        weatherDao: WeatherDao
    ): WeatherRepository {
        return WeatherRepositoryImpl(api, weatherDao)
    }

    @Provides
    @Singleton
    fun provideWeatherViewModelFactory(
        repository: WeatherRepository,
        application: Application
    ): TodayForecastViewModelFactory {
        return TodayForecastViewModelFactory(repository, application)
    }
    @Provides
    @Singleton
    fun provideForecastViewModelFactory(repository: WeatherRepository): DailyForecastViewModelFactory {
        return DailyForecastViewModelFactory(repository)
    }

    @Provides
    @Singleton
    fun provideHourlyViewModelFactory(repository: WeatherRepository): HourlyForecastViewModelFactory {
        return HourlyForecastViewModelFactory(repository)
    }

    @Provides
    @Singleton
    fun provideSharedViewModel(): SharedViewModel {
        return SharedViewModel()
    }
}
