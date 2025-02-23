package ru.bear.weatherjusttogether.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.WeatherApp
import ru.bear.weatherjusttogether.adapters.DailyAdapter
import ru.bear.weatherjusttogether.viewmodel.ForecastViewModel
import ru.bear.weatherjusttogether.viewmodel.ForecastViewModelFactory
import ru.bear.weatherjusttogether.viewmodel.SharedViewModel
import ru.bear.weatherjusttogether.viewmodel.WeatherViewModel
import ru.bear.weatherjusttogether.viewmodel.WeatherViewModelFactory
import javax.inject.Inject

class DailyFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ForecastViewModelFactory
    private lateinit var viewModel: ForecastViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var dailyRecyclerView: RecyclerView

    private lateinit var cityNameText: TextView
    private var selectedCity: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as WeatherApp).appComponent.inject(this)

        // Инициализируем sharedViewModel
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedCity = arguments?.getString("selected_city", "Москва")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_daily, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        VMSettings()

        dailyRecyclerView = view.findViewById(R.id.dailyRecyclerView)
        cityNameText = view.findViewById(R.id.city_name)
        dailyRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        // Инициализация SharedViewModel
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        // Наблюдаем за изменениями выбранного города
        sharedViewModel.selectedCity.observe(viewLifecycleOwner) { city ->
            city?.let {
                // Отображаем название выбранного города
                cityNameText.text = it
                viewModel.fetchForecast(it)
            }
        }

        viewModel.forecast.observe(viewLifecycleOwner) { forecastResponse ->
            forecastResponse?.let {
                val dailyList = it.forecast.forecastday
                dailyRecyclerView.adapter = DailyAdapter(dailyList)
            }
        }

    }

    private fun VMSettings() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(ForecastViewModel::class.java)
    }
}
