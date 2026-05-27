package com.utsav.multiactivity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var dropdownTheme: AutoCompleteTextView
    private lateinit var dropdownLanguage: AutoCompleteTextView

    private val themeOptions = arrayOf(
        "System Default",
        "Light Mode",
        "Dark Mode"
    )

    private val languageNames = arrayOf(
        "System Default",
        "English",
        "Hindi",
        "French",
        "German",
        "Spanish",
        "Italian",
        "Japanese",
        "Chinese",
        "Russian",
        "Arabic",
        "Korean",
        "Portuguese"
    )

    private val languageCodes = arrayOf(
        "",
        "en",
        "hi",
        "fr",
        "de",
        "es",
        "it",
        "ja",
        "zh",
        "ru",
        "ar",
        "ko",
        "pt"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        preferences = getSharedPreferences("app_settings", MODE_PRIVATE)

        findViewById<TextView>(R.id.btnBackSettings).setOnClickListener {
            finish()
        }

        dropdownTheme = findViewById(R.id.dropdownTheme)
        dropdownLanguage = findViewById(R.id.dropdownLanguage)

        setupThemeDropdown()
        setupLanguageDropdown()
    }

    private fun setupThemeDropdown() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            themeOptions
        )

        dropdownTheme.setAdapter(adapter)

        val savedTheme = preferences.getInt("theme_index", 0)
        dropdownTheme.setText(themeOptions[savedTheme], false)

        dropdownTheme.setOnItemClickListener { _, _, position, _ ->
            preferences.edit().putInt("theme_index", position).apply()

            when (position) {
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
        }
    }

    private fun setupLanguageDropdown() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            languageNames
        )

        dropdownLanguage.setAdapter(adapter)

        val savedCode = preferences.getString("language_code", "") ?: ""

        val index = languageCodes.indexOf(savedCode).let {
            if (it == -1) 0 else it
        }

        dropdownLanguage.setText(languageNames[index], false)

        dropdownLanguage.setOnItemClickListener { _, _, position, _ ->
            preferences.edit()
                .putString("language_code", languageCodes[position])
                .apply()

            Toast.makeText(
                this,
                "Restart app to apply language changes",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}