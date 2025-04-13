package ru.bear.weatherjusttogether.utils

enum class WindSpeedUnit(val displayName: String) {
    KPH("Км/ч"),
    MPH("Мили/ч");

    override fun toString(): String = displayName
}