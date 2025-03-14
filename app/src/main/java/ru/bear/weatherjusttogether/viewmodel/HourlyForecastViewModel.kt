package ru.bear.weatherjusttogether.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.bear.weatherjusttogether.domain.repository.WeatherRepository
import ru.bear.weatherjusttogether.data.remote.network.models.HourlyWeatherApi
import ru.bear.weatherjusttogether.domain.models.HourlyWeatherDomain
import javax.inject.Inject


class HourlyForecastViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _hourlyForecast = MutableLiveData<List<HourlyWeatherDomain>?>()
    val hourlyForecast: LiveData<List<HourlyWeatherDomain>?> get() = _hourlyForecast

    /** Метод для обновления прогноза по новому городу */
    fun updateCity(city: String) {
        fetchHourlyForecast(city)
    }


    fun fetchHourlyForecast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val hourlyData = repository.getHourlyForecast(city) // Теперь используем доменную модель
                withContext(Dispatchers.Main) {
                    _hourlyForecast.value = hourlyData
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _hourlyForecast.value = null
                }
            }
        }
    }
}
