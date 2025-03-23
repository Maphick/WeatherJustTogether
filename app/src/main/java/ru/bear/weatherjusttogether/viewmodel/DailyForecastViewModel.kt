package ru.bear.weatherjusttogether.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.bear.weatherjusttogether.domain.repository.WeatherRepository
import ru.bear.weatherjusttogether.domain.models.DailyWeatherDomain
import javax.inject.Inject


class DailyForecastViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {
    private val _cityName = MutableLiveData<String>()
    val cityName: LiveData<String> get() = _cityName


    private val _forecast = MutableLiveData<List<DailyWeatherDomain>>()
    val forecast: LiveData<List<DailyWeatherDomain>> get() = _forecast

    fun fetchForecast(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getDailyForecast(city) // Получаем List<DailyWeatherDomain>
                withContext(Dispatchers.Main) {
                    _forecast.value = response // Устанавливаем в LiveData
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _forecast.value = emptyList() // Безопасно обрабатываем ошибку
                }
            }
        }
    }

    fun fetchForecastWithFallback(cityArg: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val city = cityArg ?: repository.getLastSavedCity() ?: "Москва"
                val response = repository.getDailyForecast(city)

                withContext(Dispatchers.Main) {
                    _forecast.value = response
                    _cityName.value = city // <–– обновляем название города
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _forecast.value = emptyList()
                    _cityName.value = cityArg ?: "Неизвестно"
                }
            }
        }
    }


}
