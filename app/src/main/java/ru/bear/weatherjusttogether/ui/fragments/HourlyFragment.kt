package ru.bear.weatherjusttogether.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.adapters.HourlyAdapter
import ru.bear.weatherjusttogether.models.HourlyWeather

class HourlyFragment : Fragment() {

    private lateinit var hourlyRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_hourly, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализируем RecyclerView
        hourlyRecyclerView = view.findViewById(R.id.hourlyRecyclerView)
        hourlyRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Загружаем данные
        makeHourlyList()
    }

    private fun makeHourlyList() {
        // Генерируем список почасового прогноза
        val hourlyList = listOf(
            HourlyWeather("01:00", 15, R.drawable.ic_drop, 80, 40),
            HourlyWeather("02:00", 14, R.drawable.ic_sunny, 85, 50),
            HourlyWeather("03:00", 14, R.drawable.ic_cloudy, 75, 20),
            HourlyWeather("04:00", 13, R.drawable.ic_snowing, 70, 10),
            HourlyWeather("05:00", 15, R.drawable.ic_drop, 80, 40),
            HourlyWeather("06:00", 14, R.drawable.ic_sunny, 85, 50),
            HourlyWeather("07:00", 14, R.drawable.ic_cloudy, 75, 20),
            HourlyWeather("08:00", 13, R.drawable.ic_snowing, 70, 10),
            HourlyWeather("09:00", 15, R.drawable.ic_drop, 80, 40),
            HourlyWeather("10:00", 14, R.drawable.ic_sunny, 85, 50),
            HourlyWeather("11:00", 14, R.drawable.ic_cloudy, 75, 20),
            HourlyWeather("12:00", 13, R.drawable.ic_snowing, 70, 10),
            HourlyWeather("13:00", 15, R.drawable.ic_drop, 80, 40),
            HourlyWeather("14:00", 14, R.drawable.ic_sunny, 85, 50),
            HourlyWeather("15:00", 14, R.drawable.ic_cloudy, 75, 20),
            HourlyWeather("16:00", 13, R.drawable.ic_snowing, 70, 10),
            HourlyWeather("17:00", 15, R.drawable.ic_drop, 80, 40),
            HourlyWeather("18:00", 14, R.drawable.ic_sunny, 85, 50),
            HourlyWeather("19:00", 14, R.drawable.ic_cloudy, 75, 20),
            HourlyWeather("20:00", 13, R.drawable.ic_snowing, 70, 10),
            HourlyWeather("21:00", 15, R.drawable.ic_drop, 80, 40),
            HourlyWeather("22:00", 14, R.drawable.ic_sunny, 85, 50),
            HourlyWeather("23:00", 14, R.drawable.ic_cloudy, 75, 20),
            HourlyWeather("00:00", 13, R.drawable.ic_snowing, 70, 10)
        )

        // Проверяем, что RecyclerView инициализирован
        if (::hourlyRecyclerView.isInitialized) {
            hourlyRecyclerView.adapter = HourlyAdapter(hourlyList)
        }
    }
}
