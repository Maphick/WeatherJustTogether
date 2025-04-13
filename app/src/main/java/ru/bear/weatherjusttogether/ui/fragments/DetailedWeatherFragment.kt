package ru.bear.weatherjusttogether.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.WeatherApp
import ru.bear.weatherjusttogether.domain.models.HourlyWeatherDomain
import ru.bear.weatherjusttogether.domain.models.TodayWeatherDomain
import ru.bear.weatherjusttogether.utils.PressureUnit
import ru.bear.weatherjusttogether.utils.SettingsManager
import ru.bear.weatherjusttogether.utils.TemperatureUnit
import ru.bear.weatherjusttogether.utils.WeatherUnitConverter
import ru.bear.weatherjusttogether.utils.WindSpeedUnit
import ru.bear.weatherjusttogether.viewmodel.HourlyForecastViewModel
import ru.bear.weatherjusttogether.viewmodel.HourlyForecastViewModelFactory
import ru.bear.weatherjusttogether.viewmodel.TodayForecastViewModel
import ru.bear.weatherjusttogether.viewmodel.TodayForecastViewModelFactory
import javax.inject.Inject
import kotlin.math.roundToInt

class DetailedWeatherFragment : Fragment() {
    @Inject
    lateinit var todayForecastViewModelFactory: TodayForecastViewModelFactory
    private lateinit var todayForecastViewModel: TodayForecastViewModel
    @Inject
    lateinit var hourlyМiewModelFactory: HourlyForecastViewModelFactory
    private lateinit var hourlyForecastViewModel: HourlyForecastViewModel

    // хелпер для SharedPreferences
    private lateinit var settingsManager: SettingsManager

    // UI
    lateinit var cityNameText: TextView
    lateinit var humidityText: TextView
    lateinit var windText: TextView
    lateinit var pressureText: TextView
    lateinit var uvText: TextView
    lateinit var visibilityText: TextView
    lateinit var feelsLikeText: TextView
    lateinit var precipitationText: TextView
    lateinit var btnBack: ImageButton
    lateinit var btnSettings: ImageButton
    lateinit var topBar: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as WeatherApp).appComponent.inject(this)
        settingsManager = SettingsManager(context) //
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_detailed_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cityNameText = view.findViewById(R.id.city_name)
        humidityText = view.findViewById(R.id.info_humidity)
        windText = view.findViewById(R.id.info_wind)
        pressureText = view.findViewById(R.id.info_pressure)
        uvText = view.findViewById(R.id.info_uv)
        visibilityText = view.findViewById(R.id.info_visibility)
        feelsLikeText = view.findViewById(R.id.info_feels_like)
        precipitationText = view.findViewById(R.id.info_pressure)
        btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnSettings = view.findViewById<ImageButton>(R.id.btnSettings)
        topBar = view.findViewById<View>(R.id.top_bar)

        VMSettings()
        buttonsSettings()
    }

    // настройка кнопок
    private fun buttonsSettings()
    {
        (btnSettings.layoutParams as ViewGroup.MarginLayoutParams).apply {
            topMargin = topBar.top - 100 // Перемещение на строку выше
        }
        btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        btnSettings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SettingsFragment())
                .addToBackStack(null)
                .commit()
        }
    }


    // настройка вью-модели
    private fun VMSettings() {
        todayForecastViewModel = ViewModelProvider(this, todayForecastViewModelFactory)
            .get(TodayForecastViewModel::class.java)
        // Подписка на LiveData
        todayForecastViewModel.weather.observe(viewLifecycleOwner) { weather ->
            if (weather != null) {
                // обновляем погоду
                updateUI(weather)
                // запрашиваем почасовую погоду
                hourlyForecastViewModel.fetchForecastWithFallback(weather.city)
            }
        }
        hourlyForecastViewModel = ViewModelProvider(this, hourlyМiewModelFactory)
            .get(HourlyForecastViewModel::class.java)
        // Подписка на LiveData
        hourlyForecastViewModel.hourlyForecast.observe(viewLifecycleOwner) { hourlyData ->
            hourlyData?.let {
                if (hourlyData.isNotEmpty()) {
                    updateChart(hourlyData)
                }
            }
        }
    }



    private fun updateUI(weather: TodayWeatherDomain) {
        view?.let {
            cityNameText.text = "${weather.city}, ${weather.region}, ${weather.country}"
            humidityText.text = "${weather.humidity}%"
            uvText.text = weather.uv.toString()
            visibilityText.text = "${weather.vis_km} км"
            precipitationText.text = "${weather.precip_mm} мм"

            // Температура (с учётом настроек)
            feelsLikeText.text = WeatherUnitConverter.convertTemperature(weather.feelslike_c, settingsManager.temperatureUnit)
            // Ветер
            windText.text = WeatherUnitConverter.convertWind(weather.wind_kph, weather.wind_dir, settingsManager.windSpeedUnit)
            // Давление
            pressureText.text = WeatherUnitConverter.convertPressure(weather.pressure_mb, settingsManager.pressureUnit)

        }
    }


    private fun updateChart(hourlyData: List<HourlyWeatherDomain>) {
        // Получаем цвета из ресурсов
        val lineColor = ContextCompat.getColor(requireContext(), R.color.bright_purple_dark)
        val circleColor = ContextCompat.getColor(requireContext(), R.color.sun)
        var textColor = ContextCompat.getColor(requireContext(), R.color.text_bright)
        var fillColor = ContextCompat.getColor(requireContext(), R.color.bright_pink_medium) // Цвет под графиком
        var gridColor = ContextCompat.getColor(requireContext(), R.color.bright_purple_light)

        val lineChart: LineChart = requireView().findViewById(R.id.lineChart)

        val labels = mutableMapOf<Float, String>()
        val xAxisLabels = mutableMapOf<Int, String>()

        // Задаем фиксированные позиции для подписей
        val labelPositions = listOf(0, 6, 12, 18, 24)
        val labelNames = mapOf(
            0 to "Ночь",
            6 to "Утро",
            12 to "День",
            18 to "Вечер",
            24 to "Ночь"
        )

        for (i in hourlyData.indices) {
            val hour = hourlyData[i]
            val timeInHours = i.toFloat()

            // Добавляем подписи только на фиксированных позициях
            if (labelPositions.contains(i)) {
                labels[timeInHours] = "${hour.temp_c}°C"
                xAxisLabels[i] = labelNames[i] ?: ""
            }
        }

        // Данные для графика: температура в разное время дня
        val timeLabels = arrayOf("Утро", "День", "Вечер", "Ночь") // Метки оси X
        val timePositions = listOf(6, 12, 18, 0) // Утро, День, Вечер, Ночь (полночь)

        val entries = mutableListOf<Entry>()
        for ((index, time) in timePositions.withIndex()) {
            val hourData = hourlyData.find {
                it.time.substring(11, 13).toInt() == time // Формат времени: "YYYY-MM-DD HH:MM"
            }
            if (hourData != null) {
                entries.add(Entry(index.toFloat(), hourData.temp_c.toFloat()))
            }
        }

        // Проверяем, есть ли данные перед обновлением графика
        if (entries.isEmpty()) return

        val dataSet = LineDataSet(entries, "Температура").apply {
            color = lineColor
            valueTextSize = 12f
            lineWidth = 3f
            circleRadius = 7f
            setDrawValues(true)
            setCircleColor(circleColor)
            setDrawCircles(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER  // Плавные линии
            setDrawFilled(true)
            fillColor = Color.CYAN
            fillAlpha = 50

            valueFormatter = object : ValueFormatter() {
                override fun getPointLabel(entry: Entry?): String {
                    return labels[entry?.x] ?: "" // Подпись только если метка есть
                }
            }
        }

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        // Настройки графика
        lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)
            setBackgroundColor(Color.TRANSPARENT)
            setGridBackgroundColor(Color.TRANSPARENT)
            setDrawGridBackground(false)
            animateX(1000)
            xAxis.apply {
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
                textColor = textColor
                textSize = 16f
                granularity = 1f
                labelCount = timeLabels.size  // Количество меток по оси X
                valueFormatter = IndexAxisValueFormatter(timeLabels)
            }

            axisLeft.apply {
                setDrawGridLines(true)
                gridColor = gridColor
                textColor = textColor
                textSize = 16f
            }
            axisRight.isEnabled = false // Отключаем правую ось
        }
    }
}