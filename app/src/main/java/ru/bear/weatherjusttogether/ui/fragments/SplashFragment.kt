package ru.bear.weatherjusttogether.ui.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.bear.weatherjusttogether.MainActivity
import ru.bear.weatherjusttogether.R

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firstWord: TextView = view.findViewById(R.id.app_name_first)
        val secondWord: TextView = view.findViewById(R.id.app_name_second)
        val thirdWord: TextView = view.findViewById(R.id.app_name_third)
        val background: ImageView = view.findViewById(R.id.splash_background)

        // Анимация появления фона
        val fadeInBackground = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 1500
        }
        background.startAnimation(fadeInBackground)

        // Начальные позиции для анимации (элементы находятся за пределами экрана)
        firstWord.translationX = 500f // Начинается справа
        secondWord.translationX = 500f // Начинается справа
        thirdWord.translationX = 500f // Начинается справа

        // Анимация для первого слова
        val firstWordSlide = ObjectAnimator.ofFloat(firstWord, "translationX", 0f).apply {
            duration = 1000
            interpolator = DecelerateInterpolator()
        }

        // Анимация для второго слова
        val secondWordSlide = ObjectAnimator.ofFloat(secondWord, "translationX", 0f).apply {
            duration = 1000
            interpolator = DecelerateInterpolator()
        }

        // Анимация для третьего слова
        val thirdWordSlide = ObjectAnimator.ofFloat(thirdWord, "translationX", 0f).apply {
            duration = 1000
            interpolator = DecelerateInterpolator()
        }

        // Появление текста с задержкой
        Handler(Looper.getMainLooper()).postDelayed({
            firstWord.visibility = View.VISIBLE
            firstWordSlide.start()
        }, 500) // Появляется первым, с задержкой 500 мс

        Handler(Looper.getMainLooper()).postDelayed({
            secondWord.visibility = View.VISIBLE
            secondWordSlide.start()
        }, 1500) // Появляется вторым, с задержкой 1500 мс

        Handler(Looper.getMainLooper()).postDelayed({
            thirdWord.visibility = View.VISIBLE
            thirdWordSlide.start()
        }, 2500) // Появляется третьим, с задержкой 2500 мс

        // Переход на HomeFragment через 4 секунды
        Handler(Looper.getMainLooper()).postDelayed({
            if (isAdded && !isDetached) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, TodayWeatherFragment())
                    .commit()
                (requireActivity() as? MainActivity)?.bottomNav?.visibility = View.VISIBLE
            }
        }, 4000)
    }
}