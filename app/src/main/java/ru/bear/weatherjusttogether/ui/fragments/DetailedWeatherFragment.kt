package ru.bear.weatherjusttogether.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.WeatherApp
import ru.bear.weatherjusttogether.domain.models.HourlyWeatherDomain
import ru.bear.weatherjusttogether.domain.models.TodayWeatherDomain
import ru.bear.weatherjusttogether.utils.SettingsManager
import ru.bear.weatherjusttogether.utils.WeatherUnitConverter
import ru.bear.weatherjusttogether.viewmodel.HourlyForecastViewModel
import ru.bear.weatherjusttogether.viewmodel.HourlyForecastViewModelFactory
import ru.bear.weatherjusttogether.viewmodel.TodayForecastViewModel
import ru.bear.weatherjusttogether.viewmodel.TodayForecastViewModelFactory
import javax.inject.Inject

class DetailedWeatherFragment : Fragment() {
    @Inject
    lateinit var todayForecastViewModelFactory: TodayForecastViewModelFactory
    private lateinit var todayForecastViewModel: TodayForecastViewModel

    @Inject
    lateinit var hourlyViewModelFactory: HourlyForecastViewModelFactory
    private lateinit var hourlyForecastViewModel: HourlyForecastViewModel

    private lateinit var settingsManager: SettingsManager

    private lateinit var cityNameText: TextView
    private lateinit var btnBack: ImageButton
    private lateinit var btnSettings: ImageButton
    private lateinit var lineChart: LineChart

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as WeatherApp).appComponent.inject(this)
        settingsManager = SettingsManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_detailed_weather, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cityNameText = view.findViewById(R.id.city_name)
        btnBack = view.findViewById(R.id.btnBack)
        btnSettings = view.findViewById(R.id.btnSettings)
        lineChart = view.findViewById(R.id.lineChart)

        setupViewModels()

        bindInfoCard(view, R.id.info_humidity, "Влажность", "%") { it.humidity }
        bindInfoCard(view, R.id.info_pressure, "Давление") {
            WeatherUnitConverter.convertPressure(it.pressure_mb, settingsManager.pressureUnit)
        }
        bindInfoCard(view, R.id.info_uv, "УФ") { it.uv.toString() }
        bindInfoCard(view, R.id.info_visibility, "Видимость", " км") { it.vis_km }
        bindInfoCard(view, R.id.info_feels_like, "Ощущается") {
            WeatherUnitConverter.convertTemperature(it.feelslike_c, settingsManager.temperatureUnit)
        }
        bindInfoCard(view, R.id.info_precipitation, "Осадки", " мм") { it.precip_mm }
        bindInfoCard(view, R.id.info_wind, "Ветер") {
            WeatherUnitConverter.convertWind(it.wind_kph, it.wind_dir, settingsManager.windSpeedUnit)
        }

        btnBack.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
        btnSettings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SettingsFragment())
                .addToBackStack(null)
                .commit()
        }


    }

    private fun bindInfoCard(
        view: View,
        cardId: Int,
        label: String,
        suffix: String = "",
        valueExtractor: (TodayWeatherDomain) -> Any?
    ) {
        val card = view.findViewById<View>(cardId)
        val labelView = card.findViewById<TextView>(R.id.info_label)
        val valueView = card.findViewById<TextView>(R.id.info_value)
        labelView.text = label

        todayForecastViewModel.weather.observe(viewLifecycleOwner) { weather ->
            valueView.text = weather?.let { valueExtractor(it)?.toString()?.plus(suffix) } ?: "-"
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setupViewModels() {
        todayForecastViewModel = ViewModelProvider(this, todayForecastViewModelFactory)
            .get(TodayForecastViewModel::class.java)
        hourlyForecastViewModel = ViewModelProvider(this, hourlyViewModelFactory)
            .get(HourlyForecastViewModel::class.java)

        todayForecastViewModel.weather.observe(viewLifecycleOwner) { weather ->
            cityNameText.text = "${weather?.city}, ${weather?.region}, ${weather?.country}"
            hourlyForecastViewModel.fetchForecastWithFallback(weather?.city)
        }

        hourlyForecastViewModel.hourlyForecast.observe(viewLifecycleOwner) { hourlyData ->
            if (!hourlyData.isNullOrEmpty()) updateChart(hourlyData)
        }
    }

    private fun updateChart(hourlyData: List<HourlyWeatherDomain>) {
        val timeLabels = listOf("Утро", "День", "Вечер", "Ночь")
        val timePositions = listOf(6, 12, 18, 0)
        val entries = mutableListOf<Entry>()
        val labels = mutableMapOf<Float, String>()

        for ((index, hour) in timePositions.withIndex()) {
            val hourData = hourlyData.find {
                it.time.substring(11, 13).toIntOrNull() == hour
            }
            if (hourData != null) {
                entries.add(Entry(index.toFloat(), hourData.temp_c.toFloat()))
                labels[index.toFloat()] = WeatherUnitConverter.convertTemperature(
                    hourData.temp_c,
                    settingsManager.temperatureUnit
                )
            }
        }

        val dataSet = LineDataSet(entries, "Температура").apply {
            color = ContextCompat.getColor(requireContext(), R.color.bright_purple_dark)
            valueTextSize = 12f
            lineWidth = 3f
            circleRadius = 7f
            setDrawValues(true)
            setCircleColor(ContextCompat.getColor(requireContext(), R.color.sun))
            setDrawCircles(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillColor = Color.CYAN
            fillAlpha = 50
            valueFormatter = object : ValueFormatter() {
                override fun getPointLabel(entry: Entry?): String = labels[entry?.x] ?: ""
            }
        }

        lineChart.apply {
            data = LineData(dataSet)
            description.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)
            setBackgroundColor(Color.TRANSPARENT)
            setGridBackgroundColor(Color.TRANSPARENT)
            setDrawGridBackground(false)
            animateX(1000)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = ContextCompat.getColor(requireContext(), R.color.text_bright)
                textSize = 16f
                granularity = 1f
                labelCount = timeLabels.size
                setDrawGridLines(false)
                valueFormatter = IndexAxisValueFormatter(timeLabels)
            }

            axisLeft.apply {
                setDrawGridLines(true)
                gridColor = ContextCompat.getColor(requireContext(), R.color.bright_purple_light)
                textColor = ContextCompat.getColor(requireContext(), R.color.text_bright)
                textSize = 16f
            }

            axisRight.isEnabled = false
        }
    }
}
