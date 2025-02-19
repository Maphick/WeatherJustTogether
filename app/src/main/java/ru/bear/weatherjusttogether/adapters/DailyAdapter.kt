package ru.bear.weatherjusttogether.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.models.DailyWeather

class DailyAdapter(private val dailyList: List<DailyWeather>) :
    RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {

    class DailyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById(R.id.daily_date)
        val temp: TextView = view.findViewById(R.id.daily_temp)
        val icon: ImageView = view.findViewById(R.id.daily_icon)
        val precipLabel: TextView = view.findViewById(R.id.daily_precip_label)
        val precipValue: TextView = view.findViewById(R.id.daily_precip_value)
        val windLabel: TextView = view.findViewById(R.id.daily_wind_label)
        val windValue: TextView = view.findViewById(R.id.daily_wind_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daily, parent, false)
        return DailyViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val weather = dailyList[position]
        holder.date.text = weather.date
        holder.temp.text = "Макс ${weather.maxTemp}°, Мин ${weather.minTemp}°"
        holder.icon.setImageResource(weather.iconRes)
        holder.precipValue.text = "${weather.precipChance}%"
        holder.windValue.text = weather.windSpeed
    }

    override fun getItemCount(): Int = dailyList.size
}
