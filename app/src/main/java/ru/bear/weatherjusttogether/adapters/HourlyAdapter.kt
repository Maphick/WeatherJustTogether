package ru.bear.weatherjusttogether.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.models.HourlyWeather
import ru.bear.weatherjusttogether.network.models.Hour

class HourlyAdapter(private val hourlyData: List<Hour>) : RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hourly, parent, false)
        return HourlyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val hourData = hourlyData[position]
        holder.time.text = hourData.time.substring(11) // Формат: "HH:MM"
        holder.temp.text = "${hourData.temp_c}°C"

        // Загрузка иконки с использованием Glide
        Glide.with(holder.itemView)
            .load("https:${hourData.condition.icon}")
            .into(holder.icon)

        // Установка влажности
        holder.humidityValue.text = "${hourData.humidity}%"

        // Установка вероятности дождя
        holder.rainChanceValue.text = "${hourData.chance_of_rain}%"
    }

    override fun getItemCount(): Int = hourlyData.size
}
