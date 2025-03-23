package ru.bear.weatherjusttogether

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.bear.weatherjusttogether.ui.fragments.AlertsFragment
import ru.bear.weatherjusttogether.ui.fragments.DailyFragment
import ru.bear.weatherjusttogether.ui.fragments.HourlyFragment
import ru.bear.weatherjusttogether.ui.fragments.SplashFragment
import ru.bear.weatherjusttogether.ui.fragments.TodayWeatherFragment

class MainActivity : AppCompatActivity() {

    // Панель нижней навигации
    lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNav)

        // Скрываем панель навигации на старте
        bottomNav.visibility = View.GONE

        // Показываем SplashFragment при первом запуске
        if (savedInstanceState == null) {
            showSplashFragment()
        }

        // Обработка кликов по нижней навигации
        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    openFragment(TodayWeatherFragment())
                    true
                }
                R.id.nav_hourly -> {
                    openFragment(HourlyFragment())
                    true
                }
                R.id.nav_daily -> {
                    openFragment(DailyFragment())
                    true
                }
                R.id.nav_alerts -> {
                    openFragment(AlertsFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun showSplashFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SplashFragment())
            .commit()
    }


    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
