package ru.bear.weatherjusttogether.di

import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.bear.weatherjusttogether.data.WeatherRepository
import ru.bear.weatherjusttogether.data.WeatherRepositoryImpl
import ru.bear.weatherjusttogether.network.api.WeatherApi
import ru.bear.weatherjusttogether.viewmodel.WeatherViewModelFactory
import javax.inject.Singleton

// Этот модуль будет создавать зависимости (Retrofit, API и Repository).

@Module
object AppModule {

    private const val BASE_URL = "https://api.weatherapi.com/"


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
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

    @Provides
    @Singleton
    fun provideWeatherRepository(api: WeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideWeatherViewModelFactory(repository: WeatherRepository): WeatherViewModelFactory {
        return WeatherViewModelFactory(repository)
    }


}
