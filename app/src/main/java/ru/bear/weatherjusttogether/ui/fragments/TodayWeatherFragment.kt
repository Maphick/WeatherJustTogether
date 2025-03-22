package ru.bear.weatherjusttogether.ui.fragments

import android.content.Context
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
import com.github.mikephil.charting.charts.LineChart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.bear.weatherjusttogether.WeatherApp
import ru.bear.weatherjusttogether.viewmodel.TodayForecastViewModel
import ru.bear.weatherjusttogether.viewmodel.TodayForecastViewModelFactory
import javax.inject.Inject
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.data.remote.network.models.Location
import ru.bear.weatherjusttogether.domain.models.HourlyWeatherDomain
import ru.bear.weatherjusttogether.domain.models.TodayWeatherDomain
import ru.bear.weatherjusttogether.viewmodel.DailyForecastViewModel

class TodayWeatherFragment : Fragment() {
    @Inject
    lateinit var todayForecastViewModelFactory: TodayForecastViewModelFactory
    private lateinit var todayForecastViewModel: TodayForecastViewModel

    lateinit var searchInput: EditText
    lateinit var searchButton: Button
    lateinit var suggestionsList: ListView
    lateinit var cityNameText: TextView
    lateinit var temperatureText: TextView

    lateinit var conditionText: TextView
//lateinit var humidityText: TextView
//lateinit var windText: TextView
    lateinit var weatherIcon: ImageView


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as WeatherApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_today_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        VMSettings()

        searchInput = view.findViewById<EditText>(R.id.search_input)
        searchButton = view.findViewById<Button>(R.id.btnSearch)
        suggestionsList = view.findViewById<ListView>(R.id.suggestions_list)
        cityNameText = view.findViewById<TextView>(R.id.city_name)
        temperatureText = view.findViewById<TextView>(R.id.temperature_value)
        weatherIcon = view.findViewById(R.id.weather_icon)
        conditionText = view.findViewById<TextView>(R.id.condition_text)
        //humidityText = view.findViewById<TextView>(R.id.humidity_value)
        //windText = view.findViewById<TextView>(R.id.wind_value)

        val btnDetails: Button = view.findViewById(R.id.btnDetails)

        btnDetails.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DetailedWeatherFragment())
                .addToBackStack(null)
                .commit()
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

        searchButtonSettings()
        //saveCity()
    }

    // кнопка поиска города по части слова
    private fun searchButtonSettings() {
        searchButton.background = null

        searchButton.setOnClickListener {
            val query = searchInput.text.toString().trim()
            if (query.isNotEmpty()) {
                // получение списка городов из API
                todayForecastViewModel.fetchCitySuggestions(query) { locations ->
                    val adapter = object : ArrayAdapter<Location>(
                        requireContext(),
                        R.layout.custom_list_item,
                        locations
                    ) {
                        // настраиваем адаптер для ресайклер вью
                        override fun getView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            // вьюха для одного элемента списка - города
                            val view = convertView ?: LayoutInflater.from(context)
                                .inflate(R.layout.custom_list_item, parent, false)
                            // название города
                            val cityName = view.findViewById<TextView>(R.id.city_name)
                            // регион
                            val cityRegion = view.findViewById<TextView>(R.id.city_region_country)
                            val location = getItem(position)

                            location?.let {
                                cityName.text = it.name
                                cityRegion.text = "${it.region}, ${it.country}"
                            }

                            return view
                        }
                    }

                    // задаем адаптер для списка городов
                    suggestionsList.adapter = adapter
                    // делаем его видимым
                    suggestionsList.visibility = View.VISIBLE

                    // если кликнули по городу
                    suggestionsList.setOnItemClickListener { _, _, position, _ ->
                        // выбранный город
                        val selectedCity = locations[position]

                        lifecycleScope.launch {
                            // Сохранится в базе
                            todayForecastViewModel.saveCityToRoom(selectedCity.name)
                        }

                        searchInput.setText(selectedCity.name)
                        cityNameText.text =
                            "${selectedCity.name}, ${selectedCity.region}, ${selectedCity.country}"
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
                    // Не обновляем, если город уже установлен
                    if (!lastCity.isNullOrEmpty() && lastCity != todayForecastViewModel.currentCity.value) {
                        cityNameText.text = lastCity

                        todayForecastViewModel.fetchWeather(lastCity)
                        //hourlyForecastViewModel.fetchHourlyForecast(lastCity)
                    }
                }
            }
        }
    }

    // настройка вью-модели
    private fun VMSettings() {
        // вью-модель создается через фабрику
        todayForecastViewModel = ViewModelProvider(this, todayForecastViewModelFactory)
            .get(TodayForecastViewModel::class.java)
        // Подписка на LiveData
        // когда меняется вьюмодель - меняем UI
        todayForecastViewModel.weather.observe(viewLifecycleOwner) { weather ->
            if (weather != null) {
                // обновляем отображение
                updateUI(weather)
            }
        }
    }

    // обновляем отображение
    private fun updateUI(weather: TodayWeatherDomain) {
        todayForecastViewModel.weather.observe(viewLifecycleOwner) { weather ->
            weather?.let {
                cityNameText.text = "${it.city}, ${it.region}, ${it.country}"
                temperatureText.text = "${it.temp_c}°C"
                conditionText.text = it.conditionText
                //humidityText.text = "Влажность: ${it.humidity}%"
                //windText.text = "Ветер: ${it.wind_kph} км/ч, ${it.wind_dir}"

                Glide.with(this).load("https:${it.conditionIcon}").into(weatherIcon)
            }
        }
    }


}


