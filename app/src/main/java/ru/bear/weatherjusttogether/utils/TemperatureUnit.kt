package ru.bear.weatherjusttogether.utils

enum class TemperatureUnit(val displayName: String) {
    CELSIUS("Цельсий"),
    FAHRENHEIT("Фаренгейт"),
    KELVIN("Кельвин");

    override fun toString(): String = displayName
}