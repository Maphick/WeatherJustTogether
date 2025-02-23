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
import ru.bear.weatherjusttogether.adapters.HourlyAdapter
import ru.bear.weatherjusttogether.models.HourlyWeather
import ru.bear.weatherjusttogether.viewmodel.HourlyViewModel
import ru.bear.weatherjusttogether.viewmodel.HourlyViewModelFactory
import ru.bear.weatherjusttogether.viewmodel.SharedViewModel
import javax.inject.Inject

class HourlyFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: HourlyViewModelFactory
    private lateinit var viewModel: HourlyViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var hourlyRecyclerView: RecyclerView
    private lateinit var cityNameText: TextView
    private var selectedCity: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as WeatherApp).appComponent.inject(this)

        // Инициализация SharedViewModel
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
        return inflater.inflate(R.layout.fragment_hourly, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        VMSettings()

        hourlyRecyclerView = view.findViewById(R.id.hourlyRecyclerView)
        cityNameText = view.findViewById(R.id.city_name)
        hourlyRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        // Наблюдаем за изменениями выбранного города
        sharedViewModel.selectedCity.observe(viewLifecycleOwner) { city ->
            city?.let {
                cityNameText.text = it
                viewModel.fetchHourlyForecast(it)
            }
        }

        viewModel.hourlyForecast.observe(viewLifecycleOwner) { hourlyData ->
            hourlyData?.let {
                hourlyRecyclerView.adapter = HourlyAdapter(it)
            }
        }
    }

    private fun VMSettings() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(HourlyViewModel::class.java)
    }
}
