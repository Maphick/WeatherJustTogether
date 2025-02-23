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
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import okhttp3.OkHttpClient
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.WeatherApp
import ru.bear.weatherjusttogether.adapters.HourlyAdapter
import ru.bear.weatherjusttogether.network.models.Hour
import ru.bear.weatherjusttogether.network.models.Location
import ru.bear.weatherjusttogether.network.models.WeatherResponse
import ru.bear.weatherjusttogether.viewmodel.ForecastViewModel
import ru.bear.weatherjusttogether.viewmodel.ForecastViewModelFactory
import ru.bear.weatherjusttogether.viewmodel.HourlyViewModel
import ru.bear.weatherjusttogether.viewmodel.HourlyViewModelFactory
import ru.bear.weatherjusttogether.viewmodel.SharedViewModel
import ru.bear.weatherjusttogether.viewmodel.WeatherViewModel
import ru.bear.weatherjusttogether.viewmodel.WeatherViewModelFactory
import javax.inject.Inject


class HomeFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: WeatherViewModelFactory
    private lateinit var viewModel: WeatherViewModel
    @Inject
    lateinit var hourlyМiewModelFactory: HourlyViewModelFactory
    private lateinit var hourlyViewModel: HourlyViewModel
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

    private fun searchButtonSettings()
    {
        // Устанавливаем прозрачный фон для кнопки поиска
        searchButton.background = null
        searchButton.setOnClickListener {
            val query = searchInput.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.fetchCitySuggestions(query) { locations ->
                    // Адаптер для отображения кастомного списка
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
                        searchInput.setText(selectedCity.name)
                        cityNameText.text = "${selectedCity.name}, ${selectedCity.region}, ${selectedCity.country}"
                        viewModel.fetchWeather(selectedCity.name)
                        suggestionsList.visibility = View.GONE

                        // Передаем название города в SharedViewModel
                        sharedViewModel.setSelectedCity(selectedCity.name)
                    }

                }
            }
        }
    }

    private fun saveCity()
    {
        // Проверяем сохраненный город при возврате на экран
        val savedCity = sharedViewModel.getSavedCity()
        if (savedCity != null) {
            cityNameText.text = savedCity
            // Загружаем прогноз
            viewModel.fetchWeather(savedCity)
            hourlyViewModel.fetchHourlyForecast(savedCity)
        } else {
            // Если сохраненного города нет, используем город по умолчанию
            sharedViewModel.setSelectedCity(getString(R.string.default_city))
            // Загружаем прогноз
            viewModel.fetchWeather(getString(R.string.default_city))
            hourlyViewModel.fetchHourlyForecast(getString(R.string.default_city))
        }
    }


    private fun VMSettings() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)
        viewModel.weather.observe(viewLifecycleOwner) { weather ->
            if (weather != null) {
                updateUI(weather)
            }
        }
        hourlyViewModel= ViewModelProvider(this, hourlyМiewModelFactory).get(HourlyViewModel::class.java)
        // Подписка на LiveData
        hourlyViewModel.hourlyForecast.observe(viewLifecycleOwner) { hourlyData ->
            hourlyData?.let {
                Log.d("HourlyForecast", "Data: $hourlyData") // Добавлено логирование
                if (hourlyData.isNotEmpty()) {
                    updateChart(hourlyData)
                }
            }
        }


    }

    private fun updateUI(weather: WeatherResponse) {
        view?.let {
            val searchInput: EditText = it.findViewById(R.id.search_input)
            val cityNameText: TextView = it.findViewById(R.id.city_name)
            val windText: TextView = it.findViewById(R.id.wind_value)
            val humidityText: TextView = it.findViewById(R.id.humidity_value)
            val pressureText: TextView = it.findViewById(R.id.pressure_value)
            val temperatureText: TextView = it.findViewById(R.id.temperature_value)
            val weatherIcon: ImageView = it.findViewById(R.id.weather_icon)
            val conditionText: TextView = it.findViewById(R.id.condition_text)

            //searchInput.setText("")
            cityNameText.text = "${weather.location.name}, ${weather.location.region}, ${weather.location.country}"
            windText.text = "${weather.current.wind_kph} км/ч, ${weather.current.wind_dir}"
            humidityText.text = "${weather.current.humidity}%"
            pressureText.text = "${weather.current.pressure_mb} hPa"
            temperatureText.text = "${weather.current.temp_c}°C"
            conditionText.text = weather.current.condition.text

            Glide.with(this)
                .load("https:${weather.current.condition.icon}")
                .into(weatherIcon)


        }

    }

    private fun updateChart(hourlyData: List<Hour>) {
        val lineChart: LineChart = requireView().findViewById(R.id.lineChart)

        val entries = ArrayList<Entry>()
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
            entries.add(Entry(timeInHours, hour.temp_c.toFloat()))

            // Добавляем подписи только на фиксированных позициях
            if (labelPositions.contains(i)) {
                labels[timeInHours] = "${hour.temp_c}°C"
                xAxisLabels[i] = labelNames[i] ?: ""
            }
        }

        val dataSet = LineDataSet(entries, "Температура").apply {
            color = Color.CYAN
            valueTextSize = 0f // Отключаем подписи для всех точек
            setDrawCircles(true)
            setCircleColor(Color.RED)
            lineWidth = 2f
            mode = LineDataSet.Mode.CUBIC_BEZIER  // Для более плавных линий
            setDrawFilled(true)
            fillColor = Color.CYAN
            fillAlpha = 50

            // Включаем подписи только на определенных точках
            setDrawValues(true)
            valueFormatter = object : ValueFormatter() {
                override fun getPointLabel(entry: Entry?): String {
                    return labels[entry?.x] ?: "" // Подпись только если метка есть
                }
            }
        }

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        val xAxis = lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 6f
            setDrawGridLines(false)
            textColor = Color.WHITE

            // Подписи по оси X только в ключевых точках
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return xAxisLabels[value.toInt()] ?: ""
                }
            }
            labelCount = 4 // Указываем количество меток на оси X
        }

        val yAxis = lineChart.axisLeft.apply {
            textColor = Color.WHITE
            setDrawGridLines(false)
        }

        lineChart.axisRight.isEnabled = false
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = true
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.setScaleEnabled(true)

        lineChart.animateX(1000)
        lineChart.invalidate()
    }


}
