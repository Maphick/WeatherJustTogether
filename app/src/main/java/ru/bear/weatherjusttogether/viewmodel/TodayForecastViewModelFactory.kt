package ru.bear.weatherjusttogether.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.bear.weatherjusttogether.domain.repository.WeatherRepository
import javax.inject.Inject

//  для передачи зависимостей
// Dagger не умеет сам создавать ViewModel'ы, поэтому нужно передать WeatherRepository вручную.
// WeatherViewModelFactory будет создавать WeatherViewModel с нужной зависимостью.
class TodayForecastViewModelFactory @Inject constructor(
    private val repository: WeatherRepository,
    private val application: Application // Добавляем Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodayForecastViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodayForecastViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
