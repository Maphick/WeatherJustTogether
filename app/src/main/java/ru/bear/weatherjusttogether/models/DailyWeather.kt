package ru.bear.weatherjusttogether.models

data class DailyWeather(
    val date: String,      // Дата (например, "28 Янв")
    val maxTemp: Int,      // Максимальная температура
    val minTemp: Int,      // Минимальная температура
    val iconRes: Int,      // Ресурс иконки (R.drawable.ic_sunny)
    val precipChance: Int, // Вероятность осадков
    val windSpeed: String  // Скорость ветра (например, "10 км/ч")
)