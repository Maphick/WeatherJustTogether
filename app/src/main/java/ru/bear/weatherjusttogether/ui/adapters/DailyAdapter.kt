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

        Glide.with(holder.itemView.context)
            .load(day.conditionIcon)
            .placeholder(R.drawable.ic_sunny)
            .into(holder.conditionIcon)

        // Цветовая обработка иконок в зависимости от текста
        val context = holder.itemView.context
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
            else -> {
                holder.conditionIcon.clearColorFilter()
            }
        }
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
