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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import ru.bear.weatherjusttogether.data.remote.network.models.Location
import ru.bear.weatherjusttogether.data.remote.network.models.TodayWeatherApi
import ru.bear.weatherjusttogether.domain.models.TodayWeatherDomain
import ru.bear.weatherjusttogether.R

class TodayForecastViewModel @Inject constructor(
    private val repository: WeatherRepository,
    application: Application // Добавляем доступ к ресурсам
) : AndroidViewModel(application) { // Наследуемся от AndroidViewModel

    private val _weather = MutableLiveData<TodayWeatherDomain?>()
    val weather: LiveData<TodayWeatherDomain?> get() = _weather


    private val _citySuggestions = MutableLiveData<List<Location>>()
    val citySuggestions: LiveData<List<Location>> get() = _citySuggestions

    private val _currentCity = MutableLiveData<String>()
    val currentCity: LiveData<String> = _currentCity.distinctUntilChanged() // Фильтруем дубли


    private val defaultCity = application.getString(R.string.default_city) // Получаем "Москва"

    init {
        viewModelScope.launch {
            repository.getLastSavedCityFlow()
                .collect { lastCity ->
                    val city = lastCity ?: defaultCity

                    // Загружаем город в `currentCity`, но НЕ вызываем `fetchWeather()`
                    if (_currentCity.value.isNullOrEmpty()) {
                        _currentCity.postValue(city)
                    }
                }
        }
    }






    suspend fun getSavedCity(): String? {
        return repository.getLastSavedCity() // Теперь `Fragment` не обращается к `Repository` напрямую
    }

    fun saveCityToRoom(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveLastCity(city)
            withContext(Dispatchers.Main) {
                _currentCity.value = city  // Обновляем LiveData
            }
        }
    }


    fun fetchWeather(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_currentCity.value == city) return@launch // Предотвращаем повторные запросы
            if (_weather.value != null && _currentCity.value == city) return@launch // Если погода уже есть, не запрашиваем повторно

            try {
                val response = repository.getWeather(city)
                withContext(Dispatchers.Main) {
                    if (_weather.value != response) {
                        _weather.value = response
                        if (_currentCity.value != city) { // Проверяем перед обновлением
                            _currentCity.postValue(city)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _weather.value = null
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
