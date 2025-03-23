package ru.bear.weatherjusttogether.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.bear.weatherjusttogether.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.bear.weatherjusttogether.ui.adapters.AlertsAdapter
import ru.bear.weatherjusttogether.models.WeatherAlert


class AlertsFragment : Fragment() {

    private lateinit var alertsRecyclerView: RecyclerView
    private lateinit var noAlertsMessage: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_alerts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alertsRecyclerView = view.findViewById(R.id.alertsRecyclerView)
        noAlertsMessage = view.findViewById(R.id.no_alerts_message)

        alertsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val alertsList = listOf(
            WeatherAlert("Flood Warning", "Moderate", "Calhoun; Lexington; Richland", "05.01.2025 21:47", "07.01.2025", "Flooding expected..."),
            WeatherAlert("Storm Warning", "Severe", "New York; Brooklyn", "06.01.2025 10:30", "08.01.2025", "Heavy storms approaching...")
        )

        if (alertsList.isEmpty()) {
            alertsRecyclerView.visibility = View.GONE
            noAlertsMessage.visibility = View.VISIBLE
        } else {
            alertsRecyclerView.adapter = AlertsAdapter(alertsList) { alert ->
                showAlertDetails(alert)
            }
        }
    }

    private fun showAlertDetails(alert: WeatherAlert) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(alert.title)
            .setMessage("Описание: ${alert.description}\n\nРегион: ${alert.regions}\n\nС ${alert.startTime} по ${alert.endTime}")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
        dialog.show()
    }
}
