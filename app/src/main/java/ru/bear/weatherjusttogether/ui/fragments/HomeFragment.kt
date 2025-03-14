package ru.bear.weatherjusttogether.ui.fragments

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.WeatherApp
import ru.bear.weatherjusttogether.data.remote.network.models.Location
import ru.bear.weatherjusttogether.domain.models.HourlyWeatherDomain
import ru.bear.weatherjusttogether.domain.models.TodayWeatherDomain
import ru.bear.weatherjusttogether.viewmodel.HourlyForecastViewModel
import ru.bear.weatherjusttogether.viewmodel.HourlyForecastViewModelFactory
import ru.bear.weatherjusttogether.viewmodel.SharedViewModel
import ru.bear.weatherjusttogether.viewmodel.TodayForecastViewModel
import ru.bear.weatherjusttogether.viewmodel.TodayForecastViewModelFactory
import javax.inject.Inject


class HomeFragment : Fragment() {
    @Inject
    lateinit var todayForecastViewModelFactory: TodayForecastViewModelFactory
    private lateinit var todayForecastViewModel: TodayForecastViewModel

    @Inject
    lateinit var hourlyМiewModelFactory: HourlyForecastViewModelFactory
    private lateinit var hourlyForecastViewModel: HourlyForecastViewModel
    private lateinit var sharedViewModel: SharedViewModel

    lateinit var searchInput: EditText
    lateinit var searchButton: Button
    lateinit var suggestionsList: ListView
    lateinit var cityNameText: TextView


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as WeatherApp).appComponent.inject(this)

        // Инициализируем sharedViewModel
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        VMSettings()

        // При возврате на HomeFragment можно явно подписаться на SharedViewModel.selectedCity и
        // обновлять cityNameText, чтобы использовать найденный город.
        // если selectedCity уже отображается, fetchWeather() не будет вызываться снова.

        sharedViewModel.selectedCity.observe(viewLifecycleOwner) { selectedCity ->
            if (!selectedCity.isNullOrEmpty() && selectedCity != cityNameText.text.toString()) {
                cityNameText.text = selectedCity

                // Проверяем, действительно ли нужно загружать данные
                if (todayForecastViewModel.currentCity.value != selectedCity) {
                    todayForecastViewModel.fetchWeather(selectedCity)
                    hourlyForecastViewModel.fetchHourlyForecast(selectedCity)
                }
            }
        }





        val btnSettings = view.findViewById<View>(R.id.btnSettings)
        val topBar = view.findViewById<View>(R.id.top_bar)
        (btnSettings.layoutParams as ViewGroup.MarginLayoutParams).apply {
            topMargin = topBar.top - 100 // Перемещение на строку выше
        }

        btnSettings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SettingsFragment())
                .addToBackStack(null)
                .commit()
        }

        searchInput = view.findViewById<EditText>(R.id.search_input)
        searchButton = view.findViewById<Button>(R.id.btnSearch)
        suggestionsList = view.findViewById<ListView>(R.id.suggestions_list)
        cityNameText = view.findViewById<TextView>(R.id.city_name)


        searchButtonSettings()
        saveCity()


    }

    private fun searchButtonSettings() {
        searchButton.background = null

        searchButton.setOnClickListener {
            val query = searchInput.text.toString().trim()
            if (query.isNotEmpty()) {
                todayForecastViewModel.fetchCitySuggestions(query) { locations ->
                    val adapter = object : ArrayAdapter<Location>(
                        requireContext(),
                        R.layout.custom_list_item,
                        locations
                    ) {
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val view = convertView ?: LayoutInflater.from(context)
                                .inflate(R.layout.custom_list_item, parent, false)

                            val cityName = view.findViewById<TextView>(R.id.city_name)
                            val cityRegion = view.findViewById<TextView>(R.id.city_region_country)
                            val location = getItem(position)

                            location?.let {
                                cityName.text = it.name
                                cityRegion.text = "${it.region}, ${it.country}"
                            }

                            return view
                        }
                    }

                    suggestionsList.adapter = adapter
                    suggestionsList.visibility = View.VISIBLE

                    suggestionsList.setOnItemClickListener { _, _, position, _ ->
                        val selectedCity = locations[position]

                        lifecycleScope.launch {
                            todayForecastViewModel.saveCityToRoom(selectedCity.name) // ✅ Сохранится в базе
                            sharedViewModel.setSelectedCity(selectedCity.name)       // ✅ Это вызовет `fetchWeather()` через `observe`
                        }

                        searchInput.setText(selectedCity.name)
                        cityNameText.text = "${selectedCity.name}, ${selectedCity.region}, ${selectedCity.country}"
                        suggestionsList.visibility = View.GONE
                    }

                }
            }
        }
    }


    private fun saveCity() {
        lifecycleScope.launch(Dispatchers.IO) {
            todayForecastViewModel.getSavedCity()?.let { lastCity ->
                withContext(Dispatchers.Main) {
                    // Если `currentCity` уже установлен, НЕ вызываем `fetchWeather()`
                    if (lastCity != todayForecastViewModel.currentCity.value) {
                        cityNameText.text = lastCity

                        todayForecastViewModel.fetchWeather(lastCity)
                        hourlyForecastViewModel.fetchHourlyForecast(lastCity)

                        sharedViewModel.setSelectedCity(lastCity) // Обновляем только если город изменился
                    }
                }
            }
        }
    }










    private fun VMSettings() {
        todayForecastViewModel = ViewModelProvider(this, todayForecastViewModelFactory).get(TodayForecastViewModel::class.java)
        // Подписка на LiveData
        todayForecastViewModel.weather.observe(viewLifecycleOwner) { weather ->
            if (weather != null) {
                updateUI(weather)
            }
        }

        // при изменении последнего сохраненного города
        todayForecastViewModel.currentCity.observe(viewLifecycleOwner) { city ->
            if (sharedViewModel.selectedCity.value != city) { // Обновляем только если город действительно изменился
                sharedViewModel.setSelectedCity(city)
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
            val searchInput: EditText = it.findViewById(R.id.search_input)
            val cityNameText: TextView = it.findViewById(R.id.city_name)
            val windText: TextView = it.findViewById(R.id.wind_value)
            val humidityText: TextView = it.findViewById(R.id.humidity_value)
            val pressureText: TextView = it.findViewById(R.id.pressure_value)
            val temperatureText: TextView = it.findViewById(R.id.temperature_value)
            val weatherIcon: ImageView = it.findViewById(R.id.weather_icon)
            val conditionText: TextView = it.findViewById(R.id.condition_text)

            // Обновляем UI с помощью данных из доменной модели
            //  только изменённые данные обновляют UI
            if (cityNameText.text.toString() != "${weather.city}, ${weather.region}, ${weather.country}") {
                cityNameText.text = "${weather.city}, ${weather.region}, ${weather.country}"
            }
            if (windText.text.toString() != "${weather.wind_kph} км/ч, ${weather.wind_dir}") {
                windText.text = "${weather.wind_kph} км/ч, ${weather.wind_dir}"
            }
            if (humidityText.text.toString() != "${weather.humidity}%") {
                humidityText.text = "${weather.humidity}%"
            }
            if (pressureText.text.toString() != "${weather.pressure_mb} hPa") {
                pressureText.text = "${weather.pressure_mb} hPa"
            }
            if (temperatureText.text.toString() != "${weather.temp_c}°C") {
                temperatureText.text = "${weather.temp_c}°C"
            }
            if (conditionText.text.toString() != weather.conditionText) {
                conditionText.text = weather.conditionText
            }

            // Загружаем иконку погоды только если она изменилась
            if (!weather.conditionIcon.isNullOrEmpty() && weatherIcon.tag != weather.conditionIcon) {
                Glide.with(this)
                    .load("https:${weather.conditionIcon}")
                    .into(weatherIcon)
                weatherIcon.tag = weather.conditionIcon // ✅ Запоминаем текущий URL иконки
            }

            // Загружаем иконку погоды с помощью Glide
            if (!weather.conditionIcon.isNullOrEmpty()) {
                Glide.with(this)
                    .load("https:${weather.conditionIcon}")
                    .into(weatherIcon)
            } else {
                Log.e("Glide", "Иконка погоды отсутствует")
            }

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
