package ru.bear.weatherjusttogether.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.WeatherApp
import ru.bear.weatherjusttogether.ui.adapters.DailyAdapter
import ru.bear.weatherjusttogether.viewmodel.DailyForecastViewModel
import ru.bear.weatherjusttogether.viewmodel.DailyForecastViewModelFactory
import javax.inject.Inject

class DailyFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: DailyForecastViewModelFactory
    private lateinit var viewModel: DailyForecastViewModel

    private lateinit var dailyRecyclerView: RecyclerView

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
        return inflater.inflate(R.layout.fragment_daily, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        VMSettings()

        dailyRecyclerView = view.findViewById(R.id.dailyRecyclerView)
        cityNameText = view.findViewById(R.id.city_name)

        dailyRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        // ✅ Используем адаптер один раз и передаем его в RecyclerView
        val dailyAdapter = DailyAdapter()
        dailyRecyclerView.adapter = dailyAdapter


        // 🔹 Подписка на LiveData для обновления списка
        viewModel.forecast.observe(viewLifecycleOwner) { dailyList ->
            if (dailyList != null && dailyList.isNotEmpty()) { // ✅ Проверка, если список не пуст
                dailyAdapter.submitList(dailyList) // ✅ Используем submitList()
            } else {
                Toast.makeText(requireContext(), "Нет данных о прогнозе", Toast.LENGTH_SHORT).show()
            }
        }


    }


    private fun VMSettings() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(DailyForecastViewModel::class.java)
    }
}
