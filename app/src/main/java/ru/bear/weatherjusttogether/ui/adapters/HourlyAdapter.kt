package ru.bear.weatherjusttogether.ui.adapters

import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.domain.models.HourlyWeatherDomain
import ru.bear.weatherjusttogether.utils.SettingsManager
import ru.bear.weatherjusttogether.utils.WeatherUnitConverter

class HourlyAdapter(
    private val settingsManager: SettingsManager
) : ListAdapter<HourlyWeatherDomain, HourlyAdapter.HourlyViewHolder>(HourlyDiffCallback()) {

    class HourlyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val time: TextView = view.findViewById(R.id.hourly_time)
        val temp: TextView = view.findViewById(R.id.hourly_temp)

        val conditionText: TextView = view.findViewById(R.id.hourly_condition)
        val icon: ImageView = view.findViewById(R.id.hourly_icon)

        val humidityLabel: TextView = view.findViewById(R.id.hourly_humidity_label)
        val humidityValue: TextView = view.findViewById(R.id.hourly_humidity_value)

        val rainChanceLabel: TextView = view.findViewById(R.id.hourly_rain_chance_label)
        val rainChanceValue: TextView = view.findViewById(R.id.hourly_rain_chance_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hourly, parent, false)
        return HourlyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val context = holder.itemView.context
        // получаем элемент через `getItem()`
        val weather = getItem(position)
        // Форматируем время: "2024-03-10 15:00" → "15:00"
        holder.time.text = weather.time.substring(11)
        //  конвертация  температуры в зависимости от настроек
        holder.temp.text = WeatherUnitConverter.convertTemperature(
            weather.temp_c, settingsManager.temperatureUnit
        )
        holder.conditionText.text = weather.conditionText

        if (!weather.conditionIcon.isNullOrEmpty()) {
            //  Загружаем иконку с URL с помощью Glide (или Picasso)
            Glide.with(holder.itemView.context)
                .load("https:${weather.conditionIcon}") // Убедитесь, что URL начинается с `https:`
                .placeholder(R.drawable.ic_sunny) // Иконка-заглушка
                .into(holder.icon)
        } else {
            Log.e("Glide", "Иконка погоды отсутствует")
        }

        // Влажность
        holder.humidityLabel.text = "Влажность:"
        holder.humidityValue.text = "${weather.humidity}%"

        // Вероятность дождя
        holder.rainChanceLabel.text = "Дождь:"
        holder.rainChanceValue.text = "${weather.chance_of_rain}%"

        // меняем цвет иконки погоды в зависимости от погоды
        val condition = weather.conditionText.lowercase()

        val colorRes = when {
            condition.contains("солнечно") ||   condition.contains("ясно") || condition.contains("sunny") || condition.contains("clear") ->
                R.color.bright_yellow
            condition.contains("пасмурно") || condition.contains("облачн") || condition.contains("cloudy") || condition.contains("overcast") ->
                R.color.light_gray

            condition.contains("гроз") || condition.contains("storm") ->
                R.color.dark_blue
            condition.contains("дожд") || condition.contains("rain") ->
                R.color.rainy_color
            condition.contains("снег") || condition.contains("snow") ->
                R.color.white
            condition.contains("дым") ||condition.contains("туман") || condition.contains("mist") || condition.contains("fog") ->
                R.color.gray
            else -> null
        }


        colorRes?.let {
            holder.icon.setColorFilter(ContextCompat.getColor(context, it), PorterDuff.Mode.SRC_IN)
        } ?: holder.icon.clearColorFilter()
    }
}
/** DiffUtil для оптимизированного обновления списка */
class HourlyDiffCallback : DiffUtil.ItemCallback<HourlyWeatherDomain>() {
    override fun areItemsTheSame(oldItem: HourlyWeatherDomain, newItem: HourlyWeatherDomain): Boolean {
        return oldItem.time == newItem.time // Проверяем, что время одинаковое
    }

    override fun areContentsTheSame(oldItem: HourlyWeatherDomain, newItem: HourlyWeatherDomain): Boolean {
        return oldItem == newItem // Проверяем, что объекты полностью идентичны
    }
}
