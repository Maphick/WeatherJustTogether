package ru.bear.weatherjusttogether.adapters

import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.models.WeatherAlert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlertsAdapter(
    private val alertsList: List<WeatherAlert>,
    private val onItemClick: (WeatherAlert) -> Unit
) : RecyclerView.Adapter<AlertsAdapter.AlertsViewHolder>() {

    class AlertsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.alert_title)
        val severity: TextView = view.findViewById(R.id.alert_severity)
        val regions: TextView = view.findViewById(R.id.alert_regions)
        val time: TextView = view.findViewById(R.id.alert_time)
        val details: TextView = view.findViewById(R.id.alert_details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alert, parent, false)
        return AlertsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertsViewHolder, position: Int) {
        val alert = alertsList[position]
        holder.title.text = alert.title
        holder.severity.text = "Серьёзность: ${alert.severity}"
        holder.regions.text = alert.regions
        holder.time.text = "С ${alert.startTime} по ${alert.endTime}"
        holder.details.setOnClickListener { onItemClick(alert) }
    }

    override fun getItemCount(): Int = alertsList.size
}
