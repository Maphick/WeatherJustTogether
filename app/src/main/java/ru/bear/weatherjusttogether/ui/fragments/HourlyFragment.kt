package ru.bear.weatherjusttogether.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import ru.bear.weatherjusttogether.viewmodel.HourlyForecastViewModel
import ru.bear.weatherjusttogether.viewmodel.HourlyForecastViewModelFactory
import javax.inject.Inject

class HourlyFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: HourlyForecastViewModelFactory
    private lateinit var viewModel: HourlyForecastViewModel

    private lateinit var hourlyRecyclerView: RecyclerView
    private lateinit var cityNameText: TextView
    private var selectedCity: String? = null

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

        VMSettings()

        hourlyRecyclerView = view.findViewById(R.id.hourlyRecyclerView)
        cityNameText = view.findViewById(R.id.city_name)
        hourlyRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        // Создаем адаптер один раз
        val hourlyAdapter = HourlyAdapter()
        hourlyRecyclerView.adapter = hourlyAdapter


        // Подписываемся на LiveData
        viewModel.hourlyForecast.observe(viewLifecycleOwner) { hourlyData ->
            if (!hourlyData.isNullOrEmpty()) {
                hourlyAdapter.submitList(hourlyData) // ✅ Обновляем список
            }
        }
    }



    private fun VMSettings() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(HourlyForecastViewModel::class.java)
    }
}
