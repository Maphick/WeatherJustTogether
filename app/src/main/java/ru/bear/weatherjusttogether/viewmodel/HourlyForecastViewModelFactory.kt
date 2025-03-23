package ru.bear.weatherjusttogether.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.bear.weatherjusttogether.domain.repository.WeatherRepository
import javax.inject.Inject

//  для передачи зависимостей
// Dagger не умеет сам создавать ViewModel'ы, поэтому нужно передать WeatherRepository вручную.
// WeatherViewModelFactory будет создавать WeatherViewModel с нужной зависимостью.
class HourlyForecastViewModelFactory @Inject constructor(
    private val repository: WeatherRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HourlyForecastViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HourlyForecastViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}