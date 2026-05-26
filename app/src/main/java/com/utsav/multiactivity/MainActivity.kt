package com.utsav.multiactivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    private lateinit var cardCalculator: MaterialCardView
    private lateinit var cardPrime: MaterialCardView
    private lateinit var cardEvenOdd: MaterialCardView
    private lateinit var cardNumberSystem: MaterialCardView
    private lateinit var cardFibonacci: MaterialCardView
    private lateinit var cardFactorial: MaterialCardView
    private lateinit var cardGcdLcm: MaterialCardView
    private lateinit var cardBmi: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardCalculator = findViewById(R.id.cardCalculator)
        cardPrime = findViewById(R.id.cardPrime)
        cardEvenOdd = findViewById(R.id.cardEvenOdd)
        cardNumberSystem = findViewById(R.id.cardNumberSystem)
        cardFibonacci = findViewById(R.id.cardFibonacci)
        cardFactorial = findViewById(R.id.cardFactorial)
        cardGcdLcm = findViewById(R.id.cardGcdLcm)
        cardBmi = findViewById(R.id.cardBmi)

        cardCalculator.setOnClickListener {
            startActivity(Intent(this, CalculatorActivity::class.java))
        }

        cardPrime.setOnClickListener {
            startActivity(Intent(this, PrimeActivity::class.java))
        }

        cardEvenOdd.setOnClickListener {
            startActivity(Intent(this, EvenOddActivity::class.java))
        }

        cardNumberSystem.setOnClickListener {
            startActivity(Intent(this, NumberSystemActivity::class.java))
        }

        cardFibonacci.setOnClickListener {
            startActivity(Intent(this, FibonacciActivity::class.java))
        }

        cardFactorial.setOnClickListener {
            startActivity(Intent(this, FactorialActivity::class.java))
        }

        cardGcdLcm.setOnClickListener {
            startActivity(Intent(this, GcdLcmActivity::class.java))
        }

        cardBmi.setOnClickListener {
            startActivity(Intent(this, BmiActivity::class.java))
        }
    }
}