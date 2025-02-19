package ru.bear.weatherjusttogether.ui.fragments

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.WeatherApp
import ru.bear.weatherjusttogether.network.models.WeatherResponse
import ru.bear.weatherjusttogether.viewmodel.WeatherViewModel
import ru.bear.weatherjusttogether.viewmodel.WeatherViewModelFactory
import javax.inject.Inject


class HomeFragment : Fragment() {
    // Внедряем зависимости в Fragment
    @Inject
    lateinit var viewModelFactory: WeatherViewModelFactory
    private lateinit var viewModel: WeatherViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as WeatherApp).appComponent.inject(this)  // Подключаем Dagger
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
        // настраиваем view model
        VMSettings();

        // Переход на страницу настроек
        val btnSettings = view.findViewById<View>(R.id.btnSettings)
        btnSettings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SettingsFragment())
                .addToBackStack(null)
                .commit()
        }

        // Загружаем данные о погоде
        updateWeatherInfo(view)

        // Отрисовка графика температуры
        drawTemperatureChart(view)
    }

    // настраиваем view model
    private fun VMSettings()
    {
        // viewModelFactory получает зависимость WeatherRepository из Dagger.
        viewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)

        //viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        // Оптимальный вариант для Android-приложения:
        // Репозиторий использует suspend
        // В ViewModel используем viewModelScope.launch
        // В Fragment подписываемся на LiveData:
        viewModel.weather.observe(viewLifecycleOwner) { weather ->
            // Обновляем UI
        }
    }

    // Обновляем UI
    private fun updateUI(weather: WeatherResponse) {
        // Обновляем UI
    }


    private fun updateWeatherInfo(view: View) {
        val windText: TextView = view.findViewById(R.id.wind_value)
        val humidityText: TextView = view.findViewById(R.id.humidity_value)
        val pressureText: TextView = view.findViewById(R.id.pressure_value)

        // Подставляем данные (примерные значения)
        windText.text = "12 км/ч, SE"
        humidityText.text = "80%"
        pressureText.text = "1012 hPa"
    }

    private fun drawTemperatureChart(view: View) {
        val lineChart: LineChart = view.findViewById(R.id.lineChart)

        // Данные для графика: температура в разное время дня
        val timeLabels = arrayOf("Утро", "День", "Вечер", "Ночь") // Метки оси X
        val entries = mutableListOf<Entry>().apply {
            add(Entry(0f, 22f))  // Утро (0)
            add(Entry(1f, 25f))  // День (1)
            add(Entry(2f, 20f))  // Вечер (2)
            add(Entry(3f, 15f))  // Ночь (3)
        }

        // Проверяем, есть ли данные перед обновлением графика
        if (entries.isEmpty()) return

        // Получаем цвета из ресурсов
        val lineColor = ContextCompat.getColor(requireContext(), R.color.bright_purple_dark)
        val circleColor = ContextCompat.getColor(requireContext(), R.color.sun)
        var textColor = ContextCompat.getColor(requireContext(), R.color.text_bright)
        var fillColor = ContextCompat.getColor(requireContext(), R.color.bright_pink_medium) // Цвет под графиком
        var gridColor = ContextCompat.getColor(requireContext(), R.color.bright_purple_light)

        // Настройка линии графика
        val dataSet = LineDataSet(entries, "Температура").apply {
            color = lineColor
            setCircleColor(circleColor)
            valueTextColor = textColor
            lineWidth = 3f
            circleRadius = 7f
            setDrawValues(true)
            setDrawCircles(true)
            setDrawFilled(true)
            fillColor = fillColor
            fillAlpha = 110
            mode = LineDataSet.Mode.CUBIC_BEZIER  // Плавные линии
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

        lineChart.invalidate() // Обновление графика
    }
}
