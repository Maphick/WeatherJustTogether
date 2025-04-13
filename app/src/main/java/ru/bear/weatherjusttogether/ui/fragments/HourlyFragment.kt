package ru.bear.weatherjusttogether.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.WeatherApp
import ru.bear.weatherjusttogether.ui.adapters.HourlyAdapter
import ru.bear.weatherjusttogether.utils.SettingsManager
import ru.bear.weatherjusttogether.viewmodel.HourlyForecastViewModel
import ru.bear.weatherjusttogether.viewmodel.HourlyForecastViewModelFactory
import javax.inject.Inject
import kotlin.collections.isNotEmpty

class HourlyFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: HourlyForecastViewModelFactory
    private lateinit var viewModel: HourlyForecastViewModel
    private lateinit var hourlyRecyclerView: RecyclerView
    private lateinit var cityNameText: TextView
    private var selectedCity: String? = null

    private lateinit var btnSettings: ImageButton

    private lateinit var settingsManager: SettingsManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as WeatherApp).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedCity = arguments?.getString("selected_city", "Москва")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_hourly, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Инициализация здесь
        settingsManager = SettingsManager(requireContext())

        btnSettings = view.findViewById<ImageButton>(R.id.btnSettings)
        cityNameText = view.findViewById(R.id.city_name)
        hourlyRecyclerView = view.findViewById(R.id.hourlyRecyclerView)
        hourlyRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        // Создаем адаптер один раз
        val hourlyAdapter = HourlyAdapter(settingsManager)
        hourlyRecyclerView.adapter = hourlyAdapter

        buttonsSettings()

        // настройка вью-модели
        VMSettings(hourlyAdapter)
    }

    private fun buttonsSettings() {
        btnSettings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SettingsFragment())
                .addToBackStack(null)
                .commit()
        }
    }


    private fun VMSettings(hourlyAdapter: HourlyAdapter) {
        viewModel = ViewModelProvider(this, viewModelFactory).
            get(HourlyForecastViewModel::class.java)

        // Подписка на LiveData для обновления списка
        viewModel.hourlyForecast.observe(viewLifecycleOwner) { hourlyData ->
            if (hourlyData!= null && hourlyData.isNotEmpty()) { // Проверка, если список не пуст
                hourlyAdapter.submitList(hourlyData) // Обновляем список
            } else {
                Toast.makeText(requireContext(),
                    "Нет данных о прогнозе", Toast.LENGTH_SHORT).show()
            }
        }
        // подписка на изменение названия города
        viewModel.cityName.observe(viewLifecycleOwner) { city ->
            cityNameText.text = city

        }

        // Запуск загрузки
        viewModel.fetchForecastWithFallback(selectedCity)
    }
}
