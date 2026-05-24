package com.utsav.multiactivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    private lateinit var cardCalculator: MaterialCardView
    private lateinit var cardPrime: MaterialCardView
    private lateinit var cardEvenOdd: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardCalculator = findViewById(R.id.cardCalculator)
        cardPrime = findViewById(R.id.cardPrime)
        cardEvenOdd = findViewById(R.id.cardEvenOdd)

        cardCalculator.setOnClickListener {
            startActivity(Intent(this, CalculatorActivity::class.java))
        }

        cardPrime.setOnClickListener {
            startActivity(Intent(this, PrimeActivity::class.java))
        }

        cardEvenOdd.setOnClickListener {
            startActivity(Intent(this, EvenOddActivity::class.java))
        }
    }
}