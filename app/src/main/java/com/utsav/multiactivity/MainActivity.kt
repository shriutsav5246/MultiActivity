package com.utsav.multiactivity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.navigation.NavigationView
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var btnMenu: TextView

    private lateinit var cardCalculator: MaterialCardView
    private lateinit var cardPrime: MaterialCardView
    private lateinit var cardEvenOdd: MaterialCardView

    private var cardNumberSystem: MaterialCardView? = null
    private var cardFibonacci: MaterialCardView? = null
    private var cardFactorial: MaterialCardView? = null
    private var cardGcdLcm: MaterialCardView? = null
    private var cardBmi: MaterialCardView? = null

    private val allCards = mutableListOf<MaterialCardView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        setupNavigationDrawer()
        setupDashboardNavigation()
        setupFloatingAnimations()
        setupBackHandling()
    }

    private fun bindViews() {
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        btnMenu = findViewById(R.id.btnMenu)

        cardCalculator = findViewById(R.id.cardCalculator)
        cardPrime = findViewById(R.id.cardPrime)
        cardEvenOdd = findViewById(R.id.cardEvenOdd)

        allCards.add(cardCalculator)
        allCards.add(cardPrime)
        allCards.add(cardEvenOdd)

        cardNumberSystem = findOptionalCard(R.id.cardNumberSystem)
        cardFibonacci = findOptionalCard(R.id.cardFibonacci)
        cardFactorial = findOptionalCard(R.id.cardFactorial)
        cardGcdLcm = findOptionalCard(R.id.cardGcdLcm)
        cardBmi = findOptionalCard(R.id.cardBmi)
    }

    private fun findOptionalCard(id: Int): MaterialCardView? {
        return try {
            val card = findViewById<MaterialCardView>(id)
            allCards.add(card)
            card
        } catch (e: Exception) {
            null
        }
    }

    private fun setupNavigationDrawer() {
        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.navHome -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.navSettings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.navAbout -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.navPrivacy -> {
                    startActivity(Intent(this, PrivacyPolicyActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.navShare -> {
                    shareApk()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                else -> false
            }
        }
    }

    private fun setupDashboardNavigation() {
        cardCalculator.setOnClickListener {
            startActivity(Intent(this, CalculatorActivity::class.java))
        }

        cardPrime.setOnClickListener {
            startActivity(Intent(this, PrimeActivity::class.java))
        }

        cardEvenOdd.setOnClickListener {
            startActivity(Intent(this, EvenOddActivity::class.java))
        }

        cardNumberSystem?.setOnClickListener {
            startActivity(Intent(this, NumberSystemActivity::class.java))
        }

        cardFibonacci?.setOnClickListener {
            startActivity(Intent(this, FibonacciActivity::class.java))
        }

        cardFactorial?.setOnClickListener {
            startActivity(Intent(this, FactorialActivity::class.java))
        }

        cardGcdLcm?.setOnClickListener {
            startActivity(Intent(this, GcdLcmActivity::class.java))
        }

        cardBmi?.setOnClickListener {
            startActivity(Intent(this, BmiActivity::class.java))
        }
    }

    private fun setupFloatingAnimations() {
        allCards.forEachIndexed { index, card ->
            card.alpha = 0f
            card.translationY = 180f
            card.scaleX = 0.92f
            card.scaleY = 0.92f

            card.animate()
                .alpha(1f)
                .translationY(0f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(800)
                .setStartDelay((index * 100).toLong())
                .start()
        }
    }

    private fun shareApk() {
        try {
            val sourceApk = File(applicationInfo.sourceDir)

            val sharedDir = File(cacheDir, "shared")
            if (!sharedDir.exists()) {
                sharedDir.mkdirs()
            }

            val sharedApk = File(sharedDir, "SmartUtilityToolkit.apk")

            FileInputStream(sourceApk).use { input ->
                FileOutputStream(sharedApk).use { output ->
                    input.copyTo(output)
                }
            }

            val uri = FileProvider.getUriForFile(
                this,
                "$packageName.provider",
                sharedApk
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/vnd.android.package-archive"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivity(Intent.createChooser(shareIntent, "Share APK"))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupBackHandling() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    finish()
                }
            }
        })
    }
}