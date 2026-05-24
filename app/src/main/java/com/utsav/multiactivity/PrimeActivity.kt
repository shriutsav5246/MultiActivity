package com.utsav.multiactivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class PrimeActivity : AppCompatActivity() {

    private lateinit var etPrimeNumber: EditText
    private lateinit var tvPrimeStatus: TextView
    private lateinit var tvPrimeResult: TextView
    private lateinit var tvPrimeList: TextView
    private lateinit var btnClearPrime: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prime)

        findViewById<TextView>(R.id.btnBackPrime).setOnClickListener {
            finish()
        }

        etPrimeNumber = findViewById(R.id.etPrimeNumber)
        tvPrimeStatus = findViewById(R.id.tvPrimeStatus)
        tvPrimeResult = findViewById(R.id.tvPrimeResult)
        tvPrimeList = findViewById(R.id.tvPrimeList)
        btnClearPrime = findViewById(R.id.btnClearPrime)

        etPrimeNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                analyzePrime(s.toString())
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {}
        })

        btnClearPrime.setOnClickListener {
            etPrimeNumber.text.clear()
            resetUI()
        }
    }

    private fun analyzePrime(input: String) {
        if (input.isEmpty()) {
            resetUI()
            return
        }

        try {
            val n = input.toInt()

            if (n < 0) {
                tvPrimeStatus.text = "Invalid Input"
                tvPrimeResult.text = "ERROR"
                tvPrimeResult.setTextColor(getColor(R.color.errorRed))
                tvPrimeList.text = "Negative numbers not allowed."
                return
            }

            if (isPrime(n)) {
                tvPrimeStatus.text = "Analysis Complete"
                tvPrimeResult.text = "PRIME"
                tvPrimeResult.setTextColor(getColor(R.color.successGreen))
            } else {
                tvPrimeStatus.text = "Analysis Complete"
                tvPrimeResult.text = "NOT PRIME"
                tvPrimeResult.setTextColor(getColor(R.color.calculatorOperator))
            }

            tvPrimeList.text = generatePrimeList(n)

        } catch (e: Exception) {
            tvPrimeStatus.text = "Invalid Input"
            tvPrimeResult.text = "ERROR"
            tvPrimeResult.setTextColor(getColor(R.color.errorRed))
            tvPrimeList.text = "Enter valid integer."
        }
    }

    private fun resetUI() {
        tvPrimeStatus.text = "Waiting for Input"
        tvPrimeResult.text = "..."
        tvPrimeResult.setTextColor(getColor(R.color.white))
        tvPrimeList.text = "..."
    }

    private fun isPrime(n: Int): Boolean {
        if (n < 2) return false
        if (n == 2) return true
        if (n % 2 == 0) return false

        for (i in 3..sqrt(n.toDouble()).toInt() step 2) {
            if (n % i == 0) return false
        }

        return true
    }

    private fun generatePrimeList(n: Int): String {
        if (n < 2) return "No prime numbers available."

        val sieve = BooleanArray(n + 1) { true }
        sieve[0] = false
        sieve[1] = false

        var p = 2
        while (p * p <= n) {
            if (sieve[p]) {
                var i = p * p
                while (i <= n) {
                    sieve[i] = false
                    i += p
                }
            }
            p++
        }

        val primes = mutableListOf<String>()

        for (i in 2..n) {
            if (sieve[i]) {
                primes.add(i.toString())
            }
        }

        return primes.joinToString(", ")
    }
}