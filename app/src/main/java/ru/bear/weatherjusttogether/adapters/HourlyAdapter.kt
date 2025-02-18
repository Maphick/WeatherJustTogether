package ru.bear.weatherjusttogether.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.models.HourlyWeather


class HourlyAdapter(private val hourlyList: List<HourlyWeather>) :
    RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

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
        val weather = hourlyList[position]

        // Устанавливаем данные
        holder.time.text = weather.time
        holder.temp.text = "${weather.temperature}°C"
        holder.icon.setImageResource(weather.iconRes)

        // Влажность
        holder.humidityLabel.text = "Влажность:"
        holder.humidityValue.text = "${weather.humidity}%"

        // Вероятность дождя
        holder.rainChanceLabel.text = "Вероятность дождя:"
        holder.rainChanceValue.text = "${weather.rainChance}%"
    }

    override fun getItemCount(): Int {
        return hourlyList.size
    }
}
