package ru.bear.weatherjusttogether.utils

enum class PressureUnit(val displayName: String) {
    HPA("гПа"),
    MMHG("мм рт. ст."),
    ATM("Атм"),
    BAR("Бар");

    override fun toString(): String = displayName
}
