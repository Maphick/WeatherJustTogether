package ru.bear.weatherjusttogether.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.models.DailyWeather
import ru.bear.weatherjusttogether.network.models.ForecastDay

class DailyAdapter(private val dailyList: List<ForecastDay>) :
    RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {

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
        val day = dailyList[position]
        holder.dateText.text = day.date
        holder.conditionText.text = day.day.condition.text
        holder.maxTempText.text = "${day.day.maxtemp_c}°C"
        holder.minTempText.text = "${day.day.mintemp_c}°C"

        // Загрузка иконки с использованием Glide
        Glide.with(holder.itemView.context)
            .load("https:${day.day.condition.icon}")
            .into(holder.conditionIcon)

        // Применение цвета в зависимости от условий погоды
        val condition = day.day.condition.text.lowercase()
        when {

            condition.contains("слабый переохлажденный дождь")-> {
                holder.conditionIcon.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, R.color.rainy_color)
                )
            }
            condition.contains("местами дождь")-> {
                holder.conditionIcon.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, R.color.rainy_color)
                )
            }
            condition.contains("переменная облачность")-> {
                holder.conditionIcon.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, R.color.cloudy_color)
                )
            }

            condition.contains("солнечно") || condition.contains("ясно") -> {
                holder.conditionIcon.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, R.color.sunny_color)
                )
            }
            condition.contains("cloudy") || condition.contains("overcast") -> {
                holder.conditionIcon.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, R.color.cloudy_color)
                )
            }
            condition.contains("snow") -> {
                holder.conditionIcon.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, R.color.snow_color)
                )
            }
            condition.contains("rain") || condition.contains("drizzle") || condition.contains("shower") -> {
                holder.conditionIcon.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, R.color.rainy_color)
                )
            }
            else -> {
                // По умолчанию - белый цвет
                holder.conditionIcon.setColorFilter(
                    ContextCompat.getColor(holder.itemView.context, R.color.text_bright)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return dailyList.size
    }
}
