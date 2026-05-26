package com.utsav.multiactivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigInteger

class FactorialActivity : AppCompatActivity() {

    private lateinit var etFactorialInput: EditText
    private lateinit var tvFactorialResult: TextView
    private lateinit var tvFactorialSteps: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factorial)

        findViewById<TextView>(R.id.btnBackFactorial).setOnClickListener {
            finish()
        }

        etFactorialInput = findViewById(R.id.etFactorialInput)
        tvFactorialResult = findViewById(R.id.tvFactorialResult)
        tvFactorialSteps = findViewById(R.id.tvFactorialSteps)

        findViewById<Button>(R.id.btnClearFactorial).setOnClickListener {
            etFactorialInput.setText("")
            resetOutputs()
        }

        etFactorialInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calculateFactorial()
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

    private fun calculateFactorial() {
        val input = etFactorialInput.text.toString().trim()

        if (input.isEmpty()) {
            resetOutputs()
            return
        }

        try {
            val n = input.toInt()

            if (n > 2000) {
                tvFactorialResult.text = "Factorial: Limit exceeded (max 2000)"
                tvFactorialSteps.text = "Steps: Too large"
                return
            }

            if (n == 0) {
                tvFactorialResult.text = "Factorial: 1"
                tvFactorialSteps.text = "Steps: 0! = 1"
                return
            }

            var factorial = BigInteger.ONE
            val steps = StringBuilder()

            for (i in n downTo 1) {
                factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))

                if (n <= 25) {
                    steps.append(i)
                    if (i != 1) {
                        steps.append(" × ")
                    }
                }
            }

            tvFactorialResult.text = "Factorial: $factorial"

            if (n <= 25) {
                tvFactorialSteps.text = "Steps: $steps"
            } else {
                tvFactorialSteps.text =
                    "Steps: Step breakdown hidden for large inputs"
            }

        } catch (e: Exception) {
            tvFactorialResult.text = "Factorial: Invalid Input"
            tvFactorialSteps.text = "Steps: Invalid Input"
        }
    }

    private fun resetOutputs() {
        tvFactorialResult.text = "Factorial: -"
        tvFactorialSteps.text = "Steps: -"
    }
}