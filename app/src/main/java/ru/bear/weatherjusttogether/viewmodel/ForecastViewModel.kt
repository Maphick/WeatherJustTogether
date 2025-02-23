package ru.bear.weatherjusttogether.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.bear.weatherjusttogether.data.WeatherRepository
import ru.bear.weatherjusttogether.network.models.ForecastResponse
import javax.inject.Inject


class ForecastViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _forecast = MutableLiveData<ForecastResponse?>()
    val forecast: LiveData<ForecastResponse?> get() = _forecast

    fun fetchForecast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getForecast(city)
                withContext(Dispatchers.Main) {
                    _forecast.value = response
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _forecast.value = null
                }
            }
        }
    }
}