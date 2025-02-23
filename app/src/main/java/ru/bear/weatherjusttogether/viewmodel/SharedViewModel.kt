package ru.bear.weatherjusttogether.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject


// SharedViewModel будет хранить название города и будет доступен как в HomeFragment, так и в DailyFragment.
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _selectedCity = MutableLiveData<String>()
    val selectedCity: LiveData<String> get() = _selectedCity

    //  savedCity для сохранения выбранного города
    private var savedCity: String? = null

    fun setSelectedCity(city: String) {
        _selectedCity.value = city
        savedCity = city
    }

    fun getSavedCity(): String? {
        return savedCity
    }
}
