package ru.bear.weatherjusttogether.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.bear.weatherjusttogether.data.WeatherRepository
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.bear.weatherjusttogether.network.models.WeatherResponse

// Внедряем WeatherRepository в ViewModel
class WeatherViewModel @Inject constructor(
    // WeatherViewModel подключён к Dagger
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weather = MutableLiveData<WeatherResponse?>()
    val weather: LiveData<WeatherResponse?> get() = _weather

    fun loadWeather(city: String) {
        // Оптимальный вариант для Android-приложения:
        // Репозиторий использует suspend
        // В ViewModel используем viewModelScope.launch
        // В Fragment подписываемся на LiveData:
        viewModelScope.launch {
            _weather.value = repository.getWeather(city)
        }
    }
}