package ru.bear.weatherjusttogether.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.bear.weatherjusttogether.network.api.WeatherApi
import ru.bear.weatherjusttogether.network.models.WeatherResponse
import javax.inject.Inject

// Конструктор с @Inject: Позволяет Dagger автоматически создать экземпляр WeatherRepositoryImpl,
// используя WeatherApi, без необходимости прописывать @Provides в AppModule.
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {


    override suspend fun getWeather(city: String): WeatherResponse? {
        return try {
            weatherApi.getCurrentWeather(API_KEY, city)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    /*
    override fun getWeather(city: String, callback: (WeatherResponse?) -> Unit) {
        // CoroutineScope(Dispatchers.IO).launch: Используется для выполнения запроса в фоновом
        // потоке
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = weatherApi.getCurrentWeather(API_KEY, city)
                // withContext(Dispatchers.Main): Переключает поток обратно на главный, чтобы
                // обновить UI
                withContext(Dispatchers.Main) {
                    callback(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback(null) // Возвращаем null в случае ошибки
                }
            }
        }
    }
    */

    companion object {
        private const val API_KEY = "api_key"
    }
}