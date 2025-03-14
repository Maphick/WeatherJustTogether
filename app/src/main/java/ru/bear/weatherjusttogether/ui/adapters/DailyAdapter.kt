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

class DailyAdapter : ListAdapter<DailyWeatherDomain, DailyAdapter.DailyViewHolder>(DailyDiffCallback()) {

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
        holder.maxTempText.text = "${day.maxtemp_c}°C"
        holder.minTempText.text = "${day.mintemp_c}°C"

        // ✅ Glide для загрузки иконки погоды
        Glide.with(holder.itemView.context)
            .load(day.conditionIcon)
            .placeholder(R.drawable.ic_sunny)
            .into(holder.conditionIcon)

        // ✅ Применение цвета в зависимости от условий погоды
        when {
            day.conditionText.contains("дождь", ignoreCase = true) -> {
                holder.conditionIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.rainy_color))
            }
            day.conditionText.contains("облачно", ignoreCase = true) -> {
                holder.conditionIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.cloudy_color))
            }
            day.conditionText.contains("солнечно", ignoreCase = true) ||
                    day.conditionText.contains("ясно", ignoreCase = true) -> {
                holder.conditionIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.sunny_color))
            }
            day.conditionText.contains("снег", ignoreCase = true) -> {
                holder.conditionIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.snow_color))
            }
            else -> {
                holder.conditionIcon.clearColorFilter()
            }
        }
    }
}

/** 🔹 DiffUtil для эффективного обновления списка */
class DailyDiffCallback : DiffUtil.ItemCallback<DailyWeatherDomain>() {
    override fun areItemsTheSame(oldItem: DailyWeatherDomain, newItem: DailyWeatherDomain): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: DailyWeatherDomain, newItem: DailyWeatherDomain): Boolean {
        return oldItem == newItem
    }
}
