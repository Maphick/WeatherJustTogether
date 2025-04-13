package ru.bear.weatherjusttogether

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ru.bear.weatherjusttogether.di.AppComponent
import ru.bear.weatherjusttogether.di.DaggerAppComponent
import ru.bear.weatherjusttogether.utils.SettingsManager

// Мы должны инициализировать Dagger при старте приложения.
class WeatherApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this) // Передаем `this` как `Context`

        //  применяем тему на все фрагменты
        val settingsManager = SettingsManager(this)
        AppCompatDelegate.setDefaultNightMode(settingsManager.appTheme.mode)
    }
}
