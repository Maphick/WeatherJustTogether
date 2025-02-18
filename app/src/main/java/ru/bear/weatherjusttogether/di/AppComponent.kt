package ru.bear.weatherjusttogether.di

import dagger.Component
import ru.bear.weatherjusttogether.data.WeatherRepository
import ru.bear.weatherjusttogether.ui.fragments.HomeFragment
import javax.inject.Singleton

// Этот интерфейс будет главным DI-компонентом.
//В нем мы укажем, какие зависимости нужно предоставлять.
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(fragment: HomeFragment) // Добавляем инъекцию в HomeFragment
}
