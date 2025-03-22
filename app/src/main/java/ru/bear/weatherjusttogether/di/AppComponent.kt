package ru.bear.weatherjusttogether.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.bear.weatherjusttogether.ui.fragments.DailyFragment
import ru.bear.weatherjusttogether.ui.fragments.DetailedWeatherFragment
import ru.bear.weatherjusttogether.ui.fragments.HourlyFragment
import ru.bear.weatherjusttogether.ui.fragments.SettingsFragment
import ru.bear.weatherjusttogether.ui.fragments.TodayWeatherFragment
import javax.inject.Singleton

// Этот интерфейс будет главным DI-компонентом.
//В нем мы укажем, какие зависимости нужно предоставлять.


@Singleton
@Component(modules = [AppModule::class]) // Добавляем модуль для ViewModel
interface AppComponent {
    // Инъекция во фрагменты
    fun inject(fragment: TodayWeatherFragment) // Для основного экрана с текущей погодой
    fun inject(fragment: DetailedWeatherFragment) // Для детальной информации о погоде
    fun inject(fragment: DailyFragment) // Прогноз на 14 дней
    fun inject(fragment: HourlyFragment) // Почасовой прогноз
    fun inject(fragment: SettingsFragment) // Экран настроек (если есть)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent // Передаем `Context` вручную
    }
}