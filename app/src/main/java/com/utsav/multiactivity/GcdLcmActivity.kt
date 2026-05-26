package com.utsav.multiactivity

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigInteger
import kotlin.math.abs

class GcdLcmActivity : AppCompatActivity() {

    private lateinit var etNumbers: EditText
    private lateinit var tvGcdResult: TextView
    private lateinit var tvLcmResult: TextView
    private lateinit var tvSteps: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gcd_lcm)

        findViewById<TextView>(R.id.btnBackGcdLcm).setOnClickListener {
            finish()
        }

        etNumbers = findViewById(R.id.etNumbers)
        tvGcdResult = findViewById(R.id.tvGcdResult)
        tvLcmResult = findViewById(R.id.tvLcmResult)
        tvSteps = findViewById(R.id.tvSteps)

        etNumbers.filters = arrayOf(
            InputFilter { source, start, end, _, _, _ ->
                val input = source.subSequence(start, end).toString()
                if (input.matches(Regex("[0-9,]*"))) source else ""
            }
        )

        findViewById<Button>(R.id.btnClearGcdLcm).setOnClickListener {
            etNumbers.setText("")
            resetOutputs()
        }

        etNumbers.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calculateGcdLcm()
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

        resetOutputs()
    }

    private fun calculateGcdLcm() {
        val rawInput = etNumbers.text.toString().trim()

        if (rawInput.isEmpty()) {
            resetOutputs()
            return
        }

        if (
            rawInput.startsWith(",") ||
            rawInput.endsWith(",") ||
            rawInput.contains(",,")
        ) {
            showInvalid("Malformed input. Use comma-separated integers only")
            return
        }

        try {
            val parsedNumbers = rawInput.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { it.toLong() }

            if (parsedNumbers.isEmpty()) {
                resetOutputs()
                return
            }

            if (parsedNumbers.size > 500) {
                showInvalid("Maximum 500 numbers allowed")
                return
            }

            val numbers = parsedNumbers.distinct()

            if (numbers.all { it == 0L }) {
                tvGcdResult.text = "HCF / GCD: Undefined"
                tvLcmResult.text = "LCM: Undefined"
                tvSteps.text = "Steps: All numbers cannot be zero"
                return
            }

            val stepsBuilder = StringBuilder()

            var gcd = abs(numbers[0])

            for (i in 1 until numbers.size) {
                gcd = calculateGcd(
                    gcd,
                    abs(numbers[i]),
                    stepsBuilder
                )
            }

            var lcm = BigInteger.valueOf(abs(numbers[0]))

            for (i in 1 until numbers.size) {
                lcm = calculateLcmBig(
                    lcm,
                    BigInteger.valueOf(abs(numbers[i]))
                )
            }

            tvGcdResult.text = "HCF / GCD: $gcd"
            tvLcmResult.text = "LCM: $lcm"
            tvSteps.text = "Steps:\n$stepsBuilder"

        } catch (e: Exception) {
            showInvalid("Use valid comma-separated integers")
        }
    }

    private fun calculateGcd(
        aInput: Long,
        bInput: Long,
        stepsBuilder: StringBuilder
    ): Long {
        var a = aInput
        var b = bInput

        while (b != 0L) {
            val remainder = a % b
            stepsBuilder.append("$a mod $b = $remainder\n")
            a = b
            b = remainder
        }

        stepsBuilder.append("HCF/GCD so far = $a\n\n")
        return a
    }

    private fun calculateLcmBig(
        a: BigInteger,
        b: BigInteger
    ): BigInteger {
        if (a == BigInteger.ZERO || b == BigInteger.ZERO) {
            return BigInteger.ZERO
        }

        val gcd = a.gcd(b)
        return a.multiply(b).divide(gcd)
    }

    private fun showInvalid(message: String) {
        tvGcdResult.text = "HCF / GCD: Invalid Input"
        tvLcmResult.text = "LCM: Invalid Input"
        tvSteps.text = "Steps: $message"
    }

    private fun resetOutputs() {
        tvGcdResult.text = "HCF / GCD: -"
        tvLcmResult.text = "LCM: -"
        tvSteps.text = "Steps: -"
    }
}