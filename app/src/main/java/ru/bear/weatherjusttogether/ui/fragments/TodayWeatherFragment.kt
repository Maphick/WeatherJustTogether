package ru.bear.weatherjusttogether.ui.fragments

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
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
import ru.bear.weatherjusttogether.utils.SettingsManager
import ru.bear.weatherjusttogether.utils.WeatherUnitConverter
import ru.bear.weatherjusttogether.viewmodel.DailyForecastViewModel

class TodayWeatherFragment : Fragment() {
    @Inject
    lateinit var todayForecastViewModelFactory: TodayForecastViewModelFactory
    private lateinit var todayForecastViewModel: TodayForecastViewModel

    // хелпер для SharedPreferences
    private lateinit var settingsManager: SettingsManager
    lateinit var searchInput: EditText
    lateinit var searchButton: Button
    lateinit var suggestionsList: ListView
    lateinit var cityNameText: TextView
    lateinit var temperatureText: TextView
    lateinit var conditionText: TextView
    lateinit var btnSettings: ImageView
    lateinit var btnDetails: ImageView
    lateinit var weatherIcon: ImageView
    lateinit var btnShare: ImageView
    lateinit var topBar: View


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as WeatherApp).appComponent.inject(this)
        settingsManager = SettingsManager(context)
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

        topBar = view.findViewById<View>(R.id.top_bar)
        searchInput = view.findViewById<EditText>(R.id.search_input)
        searchButton = view.findViewById<Button>(R.id.btnSearch)
        suggestionsList = view.findViewById<ListView>(R.id.suggestions_list)
        cityNameText = view.findViewById<TextView>(R.id.city_name)
        temperatureText = view.findViewById<TextView>(R.id.temperature_value)
        weatherIcon = view.findViewById(R.id.weather_icon)
        conditionText = view.findViewById<TextView>(R.id.condition_text)
        btnSettings = view.findViewById<ImageView>(R.id.btnSettings)
        btnDetails = view.findViewById<ImageView>(R.id.btnDetails)
        btnShare = view.findViewById<ImageView>(R.id.btnShare)

        // настройка вьюмоделей
        VMSettings()

        // настройка кнопок
        buttonsSettings()
    }


    // настройка кнопок
    private fun buttonsSettings()
    {
        // анимайи топбара
        topBar.translationY = -topBar.height.toFloat()
        topBar.alpha = 0f
        topBar.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(600)
            .setInterpolator(DecelerateInterpolator())
            .start()


        // Кнопка "Подробнее"
        btnDetails.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DetailedWeatherFragment())
                .addToBackStack(null)
                .commit()
        }

        // Кнопка "Поделиться"
        btnShare.setOnClickListener {
            val textToShare = buildString {
                append("Погода в городе: ${cityNameText.text}\n")
                append("Температура: ${temperatureText.text}\n")
                append("Состояние: ${conditionText.text}")
            }
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, textToShare)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Поделиться прогнозом погоды"))
        }


        (btnSettings.layoutParams as ViewGroup.MarginLayoutParams).apply {
            topMargin = topBar.top - 100 // Перемещение на строку выше
        }

        // Кнопка "Настройки"
        btnSettings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SettingsFragment())
                .addToBackStack(null)
                .commit()
        }

        //  Кнопка "Поиск города"
        searchButtonSettings()
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
        weather?.let {
            cityNameText.text = "${it.city}, ${it.region}, ${it.country}"
            // Использовать настройки при отображении
            temperatureText.text = WeatherUnitConverter.convertTemperature(it.temp_c, settingsManager.temperatureUnit)
            conditionText.text = it.conditionText
            Glide.with(this).load("https:${it.conditionIcon}").into(weatherIcon)

            // меняем цвет иконки погоды в зависимости от погоды
            val condition = weather.conditionText.lowercase()

            val colorRes = when {
                condition.contains("солнечно") ||   condition.contains("ясно") || condition.contains("sunny") || condition.contains("clear") ->
                    R.color.bright_yellow
                condition.contains("дым") || condition.contains("облачн") || condition.contains("cloudy") || condition.contains("overcast") ->
                    R.color.gray
                condition.contains("дожд") || condition.contains("rain") ->
                    R.color.dark_blue
                condition.contains("снег") || condition.contains("snow") ->
                    R.color.white
                condition.contains("туман") || condition.contains("mist") || condition.contains("fog") ->
                    R.color.light_gray
                else -> null
            }

            colorRes?.let {
                weatherIcon.setColorFilter(ContextCompat.getColor(requireContext(), it), PorterDuff.Mode.SRC_IN)
            } ?: weatherIcon.clearColorFilter()
        }
    }


}


