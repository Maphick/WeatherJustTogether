package ru.bear.weatherjusttogether.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.adapters.DailyAdapter
import ru.bear.weatherjusttogether.models.DailyWeather


class DailyFragment : Fragment() {

    private lateinit var dailyRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_daily, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dailyRecyclerView = view.findViewById(R.id.dailyRecyclerView)
        dailyRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dailyList = listOf(
            DailyWeather("Сегодня", 25, 16, R.drawable.ic_cloudy, 30, "10 км/ч"),
            DailyWeather("Завтра", 27, 18, R.drawable.ic_sunny, 10, "15 км/ч"),
            DailyWeather("1 февраля", 25, 16, R.drawable.ic_cloudy, 30, "10 км/ч"),
            DailyWeather("2 февраля", 27, 18, R.drawable.ic_sunny, 10, "15 км/ч"),
            DailyWeather("3 февраля", 25, 16, R.drawable.ic_cloudy, 30, "10 км/ч"),
            DailyWeather("4 февраля", 27, 18, R.drawable.ic_sunny, 10, "15 км/ч"),
            DailyWeather("5 февраля", 25, 16, R.drawable.ic_cloudy, 30, "10 км/ч"),
            DailyWeather("6 февраля", 27, 18, R.drawable.ic_sunny, 10, "15 км/ч"),
            DailyWeather("7 февраля", 25, 16, R.drawable.ic_cloudy, 30, "10 км/ч"),
            DailyWeather("8 февраля", 27, 18, R.drawable.ic_sunny, 10, "15 км/ч"),
            DailyWeather("9 февраля", 25, 16, R.drawable.ic_cloudy, 30, "10 км/ч"),
            DailyWeather("10 февраля", 27, 18, R.drawable.ic_sunny, 10, "15 км/ч"),
            DailyWeather("11 февраля", 25, 16, R.drawable.ic_cloudy, 30, "10 км/ч"),
            DailyWeather("12 февраля", 27, 18, R.drawable.ic_sunny, 10, "15 км/ч"),
            DailyWeather("13 февраля", 25, 16, R.drawable.ic_cloudy, 30, "10 км/ч"),
            DailyWeather("14 февраля", 27, 18, R.drawable.ic_sunny, 10, "15 км/ч"),
            DailyWeather("15 февраля", 25, 16, R.drawable.ic_cloudy, 30, "10 км/ч"),
            DailyWeather("16 февраля", 27, 18, R.drawable.ic_sunny, 10, "15 км/ч"),
            DailyWeather("17 февраля", 25, 16, R.drawable.ic_cloudy, 30, "10 км/ч"),
            DailyWeather("18 февраля", 27, 18, R.drawable.ic_sunny, 10, "15 км/ч"),
            DailyWeather("19 февраля", 25, 16, R.drawable.ic_cloudy, 30, "10 км/ч"),
            DailyWeather("20 февраля", 27, 18, R.drawable.ic_sunny, 10, "15 км/ч"),
            DailyWeather("21 февраля", 25, 16, R.drawable.ic_cloudy, 30, "10 км/ч"),
            DailyWeather("22 февраля", 27, 18, R.drawable.ic_sunny, 10, "15 км/ч"),
            DailyWeather("23 февраля", 25, 16, R.drawable.ic_cloudy, 30, "10 км/ч"),
            DailyWeather("24 февраля", 27, 18, R.drawable.ic_sunny, 10, "15 км/ч"),
            DailyWeather("25 февраля", 25, 16, R.drawable.ic_cloudy, 30, "10 км/ч"),
            DailyWeather("26 февраля", 27, 18, R.drawable.ic_sunny, 10, "15 км/ч"),
            DailyWeather("27 февраля", 25, 16, R.drawable.ic_cloudy, 30, "10 км/ч"),
            DailyWeather("28 февраля", 27, 18, R.drawable.ic_sunny, 10, "15 км/ч"),
            DailyWeather("29 февраля", 25, 16, R.drawable.ic_cloudy, 30, "10 км/ч"),
            DailyWeather("30 февраля", 27, 18, R.drawable.ic_sunny, 10, "15 км/ч"),


            )

        dailyRecyclerView.adapter = DailyAdapter(dailyList)
    }
}
