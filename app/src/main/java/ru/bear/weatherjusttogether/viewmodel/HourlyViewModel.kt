package ru.bear.weatherjusttogether.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.bear.weatherjusttogether.data.WeatherRepository
import ru.bear.weatherjusttogether.models.HourlyWeather
import ru.bear.weatherjusttogether.network.models.Hour
import javax.inject.Inject

class HourlyViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _hourlyForecast = MutableLiveData<List<Hour>?>()
    val hourlyForecast: LiveData<List<Hour>?> get() = _hourlyForecast

    fun fetchHourlyForecast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val hourlyData = repository.getHourlyForecast(city)
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
