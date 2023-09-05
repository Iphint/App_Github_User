package com.arifin.myapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.arifin.myapplication.R
import com.arifin.myapplication.databinding.ActivitySplashScreenBinding
import com.arifin.myapplication.model.SettingPreferences
import com.arifin.myapplication.model.dataStore
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private lateinit var settingsManager: SettingPreferences

    private val splashTimeOut: Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.duration = splashTimeOut
        fadeOut.fillAfter = true
        binding.logoImageView.startAnimation(fadeOut)
        binding.textLogo.startAnimation(fadeOut)

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTimeOut)


        settingsManager = SettingPreferences.getInstance(applicationContext.dataStore)
        lifecycleScope.launch {
            settingsManager.getThemeSetting().collect { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.textLogo.setTextColor(resources.getColor(R.color.white))
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.textLogo.setTextColor(resources.getColor(R.color.black))
                }
            }
        }
    }
}