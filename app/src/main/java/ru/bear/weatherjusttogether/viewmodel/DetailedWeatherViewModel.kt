package ru.bear.weatherjusttogether.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.bear.weatherjusttogether.domain.repository.WeatherRepository
import javax.inject.Inject
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.bear.weatherjusttogether.domain.models.TodayWeatherDomain
import ru.bear.weatherjusttogether.R

class DetailedWeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _weatherDetails = MutableLiveData<TodayWeatherDomain?>()
    val weatherDetails: LiveData<TodayWeatherDomain?> get() = _weatherDetails

    private val _currentCity = MutableLiveData<String>()
    val currentCity: LiveData<String> = _currentCity.distinctUntilChanged()

    private val defaultCity = application.getString(R.string.default_city)

    init {
        viewModelScope.launch {
            val lastCity = repository.getLastSavedCity()
            val city = lastCity ?: defaultCity
            _currentCity.postValue(city)
            fetchWeatherDetails(city)
        }
    }

    fun fetchWeatherDetails(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getWeather(city)
                withContext(Dispatchers.Main) {
                    response?.let {
                        _weatherDetails.postValue(it)
                        _currentCity.value = city
                    } ?: Log.e("fetchWeather", "Ошибка: Данные погоды null для города $city")
                }


                /*
                val response = repository.getWeather(city)
                withContext(Dispatchers.Main) {
                    if (_weatherDetails.value != response) {
                        _weatherDetails.value = response
                    }
                    if (_currentCity.value != city) {
                        _currentCity.value = city
                    }
                }
                */
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _weatherDetails.value = null
                }
            }
        }
    }
}
