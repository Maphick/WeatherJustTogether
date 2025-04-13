package ru.bear.weatherjusttogether.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.domain.models.DailyWeatherDomain
import ru.bear.weatherjusttogether.utils.SettingsManager
import ru.bear.weatherjusttogether.utils.TemperatureUnit

class DailyAdapter(
    private val settingsManager: SettingsManager
) : ListAdapter<DailyWeatherDomain, DailyAdapter.DailyViewHolder>(DailyDiffCallback()) {

    class DailyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val conditionText: TextView = itemView.findViewById(R.id.conditionText)
        val maxTempText: TextView = itemView.findViewById(R.id.maxTempText)
        val minTempText: TextView = itemView.findViewById(R.id.minTempText)
        val conditionIcon: ImageView = itemView.findViewById(R.id.conditionIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daily, parent, false)
        return DailyViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val day = getItem(position)
        val context = holder.itemView.context

        holder.dateText.text = day.date
        holder.conditionText.text = day.conditionText

        val unit = when (settingsManager.temperatureUnit) {
            TemperatureUnit.CELSIUS -> "°C"
            TemperatureUnit.FAHRENHEIT -> "°F"
            TemperatureUnit.KELVIN -> "°K"
        }

        val maxTemp = formatTemperature(day.maxtemp_c)
        val minTemp = formatTemperature(day.mintemp_c)

        holder.maxTempText.text = "$maxTemp$unit"
        holder.minTempText.text = "$minTemp$unit"

        Glide.with(context)
            .load("https:${day.conditionIcon}")
            .placeholder(R.drawable.ic_sunny)
            .into(holder.conditionIcon)


        // Цвет иконки в зависимости от текста
        when {
            day.conditionText.contains("дождь", ignoreCase = true) -> {
                holder.conditionIcon.setColorFilter(ContextCompat.getColor(context, R.color.rainy_color))
            }
            day.conditionText.contains("облачно", ignoreCase = true) -> {
                holder.conditionIcon.setColorFilter(ContextCompat.getColor(context, R.color.cloudy_color))
            }
            day.conditionText.contains("солнечно", ignoreCase = true) ||
                    day.conditionText.contains("ясно", ignoreCase = true) -> {
                holder.conditionIcon.setColorFilter(ContextCompat.getColor(context, R.color.sunny_color))
            }
            day.conditionText.contains("снег", ignoreCase = true) -> {
                holder.conditionIcon.setColorFilter(ContextCompat.getColor(context, R.color.snow_color))
            }
            else -> holder.conditionIcon.clearColorFilter()
        }

        // Заполняем параметры
        bindInfo(holder.itemView, R.id.info_humidity, "Влажность", "${day.avghumidity}%")
        bindInfo(holder.itemView, R.id.info_wind, "Ветер", "${day.maxwind_kph} км/ч")
        bindInfo(holder.itemView, R.id.info_pressure, "Давление", "1013 гПа") // Используй нужное поле, если добавишь
        bindInfo(holder.itemView, R.id.info_visibility, "Видимость", "10 км") // Нет поля — можешь добавить
        bindInfo(holder.itemView, R.id.info_feels_like, "Ощущается как", "${(day.avgtemp_c).toInt()}$unit")
        bindInfo(holder.itemView, R.id.info_precipitation, "Осадки", "${day.totalprecip_mm} мм")
        bindInfo(holder.itemView, R.id.info_uv, "УФ индекс", "—") // Нет поля — можно убрать или заменить
    }

    private fun bindInfo(view: View, id: Int, label: String, value: String) {
        val parent = view.findViewById<View>(id)
        val labelView = parent.findViewById<TextView>(R.id.info_label)
        val valueView = parent.findViewById<TextView>(R.id.info_value)

        labelView.text = "$label:"
        valueView.text = value
    }


    private fun formatTemperature(celsius: Double): Int {
        return when (settingsManager.temperatureUnit) {
            TemperatureUnit.CELSIUS -> celsius.toInt()
            TemperatureUnit.FAHRENHEIT -> (celsius * 9 / 5 + 32).toInt()
            TemperatureUnit.KELVIN -> (celsius + 273.15).toInt()
        }
    }
}

class DailyDiffCallback : DiffUtil.ItemCallback<DailyWeatherDomain>() {
    override fun areItemsTheSame(oldItem: DailyWeatherDomain, newItem: DailyWeatherDomain): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: DailyWeatherDomain, newItem: DailyWeatherDomain): Boolean {
        return oldItem == newItem
    }
}
