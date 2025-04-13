package ru.bear.weatherjusttogether.utils

import android.content.Context
import androidx.core.text.util.LocalePreferences

// хелпер для SharedPreferences
class SettingsManager(context: Context) {
    private val prefs = context.getSharedPreferences("weather_settings", Context.MODE_PRIVATE)

    var temperatureUnit: TemperatureUnit
        get() = TemperatureUnit.valueOf(prefs.getString("temperature_unit",
            TemperatureUnit.CELSIUS.name)!!)
        set(value) = prefs.edit().putString("temperature_unit", value.name).apply()

    var windSpeedUnit: WindSpeedUnit
        get() = WindSpeedUnit.valueOf(prefs.getString("wind_speed_unit",
            WindSpeedUnit.KPH.name)!!)
        set(value) = prefs.edit().putString("wind_speed_unit", value.name).apply()

    var pressureUnit: PressureUnit
        get() = PressureUnit.valueOf(prefs.getString("pressure_unit",
            PressureUnit.HPA.name)!!)
        set(value) = prefs.edit().putString("pressure_unit", value.name).apply()


    var appTheme: AppTheme
        get() {
            val name = prefs.getString("theme", null)
            return if (name != null) AppTheme.valueOf(name)
            else AppTheme.SYSTEM // если не задано, возвращаем системную тему по умолчанию
        }
        set(value) {
            prefs.edit().putString("theme", value.name).apply()
        }

}
