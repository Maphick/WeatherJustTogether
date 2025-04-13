package ru.bear.weatherjusttogether.utils

import androidx.appcompat.app.AppCompatDelegate

enum class AppTheme(val mode: Int) {
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
    DARK(AppCompatDelegate.MODE_NIGHT_YES),
    SYSTEM(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

    override fun toString(): String = when (this) {
        LIGHT -> "Светлая"
        DARK -> "Тёмная"
        SYSTEM -> "Системная"
    }
}
