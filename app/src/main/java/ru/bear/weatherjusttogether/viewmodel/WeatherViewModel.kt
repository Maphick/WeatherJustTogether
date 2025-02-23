package ru.bear.weatherjusttogether.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.bear.weatherjusttogether.data.WeatherRepository
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.bear.weatherjusttogether.network.models.Location
import ru.bear.weatherjusttogether.network.models.WeatherResponse

class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    // для хранения текущей погоды
    private val _weather = MutableLiveData<WeatherResponse?>()
    val weather: LiveData<WeatherResponse?> get() = _weather

    //  для хранения списка городов
    private val _citySuggestions = MutableLiveData<List<Location>>()
    val citySuggestions: LiveData<List<Location>> get() = _citySuggestions


    // fetchWeather() выполняет запрос погоды по текущему городу
    fun fetchWeather(city: String) {
        // Dispatchers.IO для сетевого запроса, чтобы не блокировать UI-поток.
        viewModelScope.launch(Dispatchers.IO) {
            // обработка ошибок через try-catch, чтобы приложение не крашилось при ошибках сети.
            try {
                val response = repository.getWeather(city)
                withContext(Dispatchers.Main) {
                    _weather.value = response
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _weather.value = null // Можно обработать ошибку через другой LiveData
                }
            }
        }
    }


    //  выполняет запрос для получения списка городов с помощью WeatherAPI:
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
