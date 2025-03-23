package ru.bear.weatherjusttogether.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.bear.weatherjusttogether.domain.repository.WeatherRepository
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.bear.weatherjusttogether.data.remote.network.models.Location
import ru.bear.weatherjusttogether.data.remote.network.models.TodayWeatherApi
import ru.bear.weatherjusttogether.domain.models.TodayWeatherDomain
import ru.bear.weatherjusttogether.R
import kotlinx.coroutines.flow.collect
class TodayForecastViewModel @Inject constructor(
    private val repository: WeatherRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _weather = MutableLiveData<TodayWeatherDomain?>()
    val weather: LiveData<TodayWeatherDomain?> get() = _weather

    private val _citySuggestions = MutableLiveData<List<Location>>()

    private val _currentCity = MutableLiveData<String>()

    private val defaultCity = application.getString(R.string.default_city)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val lastCity = repository.getLastSavedCity() ?: defaultCity
            withContext(Dispatchers.Main) {
                _currentCity.value = lastCity
                fetchWeather(lastCity)
            }
        }
    }

    fun saveCityToRoom(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_currentCity.value == city) return@launch
            repository.saveLastCity(city)
            withContext(Dispatchers.Main) {
                _currentCity.value = city
                fetchWeather(city) // После сохранения сразу обновляем погоду
            }
        }
    }

    fun fetchWeather(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getWeather(city)
                withContext(Dispatchers.Main) {
                    if (_weather.value != response) {
                        _weather.postValue(response) // Устанавливаем новые данные
                    }
                    if (_currentCity.value != city) {
                        _currentCity.value = city
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _weather.postValue(null) // Сброс предыдущих данных
                }
            }
        }
    }

    fun fetchCitySuggestions(city: String, callback: (List<Location>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getCitySuggestions(city)
                withContext(Dispatchers.Main) {
                    _citySuggestions.value = response
                    callback(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _citySuggestions.value = emptyList()
                    callback(emptyList())
                }
            }
        }
    }
}
