package ru.bear.weatherjusttogether.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.bear.weatherjusttogether.ui.fragments.DailyFragment
import ru.bear.weatherjusttogether.ui.fragments.HomeFragment
import ru.bear.weatherjusttogether.ui.fragments.HourlyFragment
import javax.inject.Singleton

// Этот интерфейс будет главным DI-компонентом.
//В нем мы укажем, какие зависимости нужно предоставлять.
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(fragment: HomeFragment) // Добавляем инъекцию в HomeFragment
    fun inject(fragment: DailyFragment) // Для DailyFragment (Прогноз на 14 дней)
    fun inject(fragment: HourlyFragment) // Для HourlyFragment (Почасового прогноза)


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent // 🔹 Передаем `Context` вручную
    }
}
