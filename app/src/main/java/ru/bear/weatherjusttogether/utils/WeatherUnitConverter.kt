package ru.bear.weatherjusttogether.utils

import kotlin.math.roundToInt

object WeatherUnitConverter {

    fun convertTemperature(celsius: Double, unit: TemperatureUnit): String {
        return when (unit) {
            TemperatureUnit.CELSIUS -> "${celsius.roundToInt()}°C"
            TemperatureUnit.FAHRENHEIT -> "${(celsius * 9 / 5 + 32).roundToInt()}°F"
            TemperatureUnit.KELVIN -> "${(celsius + 273.15).roundToInt()}K"
        }
    }

    fun convertWind(kph: Double, direction: String, unit: WindSpeedUnit): String {
        return when (unit) {
            WindSpeedUnit.KPH -> "${kph.roundToInt()} км/ч, $direction"
            WindSpeedUnit.MPH -> "${(kph / 1.609).roundToInt()} миль/ч, $direction"
        }
    }

    fun convertPressure(hPa: Double, unit: PressureUnit): String {
        return when (unit) {
            PressureUnit.HPA -> "${hPa.roundToInt()} гПа"
            PressureUnit.MMHG -> "${(hPa * 0.750063755).roundToInt()} мм рт. ст."
            PressureUnit.ATM -> "%.2f атм".format(hPa / 1013.25)
            PressureUnit.BAR -> "%.2f бар".format(hPa / 1000)
        }
    }
}
