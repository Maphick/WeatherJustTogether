package ru.bear.weatherjusttogether.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.domain.models.HourlyWeatherDomain

class HourlyAdapter : ListAdapter<HourlyWeatherDomain, HourlyAdapter.HourlyViewHolder>(HourlyDiffCallback()) {

    class HourlyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val time: TextView = view.findViewById(R.id.hourly_time)
        val temp: TextView = view.findViewById(R.id.hourly_temp)
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
        val weather = getItem(position) // ✅ Теперь получаем элемент через `getItem()`

        holder.time.text = weather.time.substring(11) // Форматируем время: "2024-03-10 15:00" → "15:00"
        holder.temp.text = "${weather.temp_c}°C"

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
        holder.rainChanceLabel.text = "Вероятность дождя:"
        holder.rainChanceValue.text = "${weather.chance_of_rain}%"
    }
}


/** 🔹 DiffUtil для оптимизированного обновления списка */
class HourlyDiffCallback : DiffUtil.ItemCallback<HourlyWeatherDomain>() {
    override fun areItemsTheSame(oldItem: HourlyWeatherDomain, newItem: HourlyWeatherDomain): Boolean {
        return oldItem.time == newItem.time // ✅ Проверяем, что время одинаковое
    }

    override fun areContentsTheSame(oldItem: HourlyWeatherDomain, newItem: HourlyWeatherDomain): Boolean {
        return oldItem == newItem // ✅ Проверяем, что объекты полностью идентичны
    }
}
