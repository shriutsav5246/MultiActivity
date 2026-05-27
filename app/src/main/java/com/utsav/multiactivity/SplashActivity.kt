package com.utsav.multiactivity

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        applySavedSettings()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }

    private fun applySavedSettings() {
        val preferences: SharedPreferences =
            getSharedPreferences("app_settings", MODE_PRIVATE)

        when (preferences.getInt("theme_index", 0)) {
            0 -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )

            1 -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )

            2 -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
        }

        val languageCode = preferences.getString("language_code", "") ?: ""

        if (languageCode.isNotEmpty()) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)

            val config = Configuration()
            config.setLocale(locale)

            resources.updateConfiguration(
                config,
                resources.displayMetrics
            )
        }
    }
}