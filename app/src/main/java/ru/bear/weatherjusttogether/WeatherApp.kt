package ru.bear.weatherjusttogether

import android.app.Application
import ru.bear.weatherjusttogether.di.AppComponent
import ru.bear.weatherjusttogether.di.DaggerAppComponent

// Мы должны инициализировать Dagger при старте приложения.
class WeatherApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this) // 🔹 Передаем `this` как `Context`
    }
}
