package ru.bear.weatherjusttogether.ui.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.bear.weatherjusttogether.R
import ru.bear.weatherjusttogether.utils.AppTheme
import ru.bear.weatherjusttogether.utils.SettingsManager
import ru.bear.weatherjusttogether.utils.TemperatureUnit
import ru.bear.weatherjusttogether.utils.WindSpeedUnit
import ru.bear.weatherjusttogether.utils.PressureUnit
import kotlin.math.hypot

class SettingsFragment : Fragment() {

    private lateinit var settingsManager: SettingsManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsManager = SettingsManager(requireContext())

        val btnBack: ImageButton = view.findViewById(R.id.btnBack)
        val spinnerTemperature: Spinner = view.findViewById(R.id.spinner_temperature)
        val spinnerWind: Spinner = view.findViewById(R.id.spinner_wind_speed)
        val spinnerPressure: Spinner = view.findViewById(R.id.spinner_pressure)
        val spinnerTheme: Spinner = view.findViewById(R.id.spinner_theme)

        btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        spinnerTemperature.adapter = createStyledAdapter(TemperatureUnit.values())
        spinnerWind.adapter = createStyledAdapter(WindSpeedUnit.values())
        spinnerPressure.adapter = createStyledAdapter(PressureUnit.values())
        spinnerTheme.adapter = createStyledAdapter(AppTheme.values())

        spinnerTemperature.setSelection(settingsManager.temperatureUnit.ordinal)
        spinnerWind.setSelection(settingsManager.windSpeedUnit.ordinal)
        spinnerPressure.setSelection(settingsManager.pressureUnit.ordinal)
        spinnerTheme.setSelection(settingsManager.appTheme.ordinal)

        spinnerTemperature.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                settingsManager.temperatureUnit = TemperatureUnit.values()[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerWind.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                settingsManager.windSpeedUnit = WindSpeedUnit.values()[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerPressure.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                settingsManager.pressureUnit = PressureUnit.values()[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerTheme.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedTheme = AppTheme.values()[position]
                if (selectedTheme != settingsManager.appTheme) {
                    applyThemeWithReveal(selectedTheme, requireView())
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun <T> createStyledAdapter(items: Array<T>): ArrayAdapter<T> {
        return object : ArrayAdapter<T>(
            requireContext(), R.layout.custom_spinner_item, items
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = LayoutInflater.from(context).inflate(R.layout.custom_spinner_item, parent, false)
                val textView = view.findViewById<TextView>(R.id.spinner_item_text)
                textView.text = getItem(position).toString()
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = LayoutInflater.from(context).inflate(R.layout.custom_spinner_item, parent, false)
                val textView = view.findViewById<TextView>(R.id.spinner_item_text)
                textView.text = getItem(position).toString()
                return view
            }
        }
    }

    private fun applyThemeWithReveal(theme: AppTheme, rootView: View) {
        val cx = rootView.width / 2
        val cy = rootView.height / 2
        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(rootView, cx, cy, 0f, finalRadius)
        anim.duration = 500
        rootView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bright_purple_light))

        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // Сохраняем выбранную тему
                settingsManager.appTheme = theme

                // Устанавливаем новую тему
                AppCompatDelegate.setDefaultNightMode(theme.mode)

                // Перезапуск активности для применения темы и восстановления всех элементов (в том числе меню)
                activity?.recreate()
            }
        })

        anim.start()
        // Это закроет все фрагменты и гарантированно пересоздаст Activity и все, что в ней.
        activity?.let {
            it.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            it.recreate()
        }

    }

}