package com.utsav.multiactivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FibonacciActivity : AppCompatActivity() {

    private lateinit var etFibonacciInput: EditText
    private lateinit var tvFibonacciResult: TextView
    private lateinit var tvNthTerm: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fibonacci)

        findViewById<TextView>(R.id.btnBackFibonacci).setOnClickListener {
            finish()
        }

        etFibonacciInput = findViewById(R.id.etFibonacciInput)
        tvFibonacciResult = findViewById(R.id.tvFibonacciResult)
        tvNthTerm = findViewById(R.id.tvNthTerm)

        findViewById<Button>(R.id.btnClearFibonacci).setOnClickListener {
            etFibonacciInput.setText("")
            resetOutputs()
        }

        etFibonacciInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                generateFibonacci()
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

    private fun generateFibonacci() {
        val input = etFibonacciInput.text.toString().trim()

        if (input.isEmpty()) {
            resetOutputs()
            return
        }

        try {
            val n = input.toInt()

            if (n <= 0) {
                tvFibonacciResult.text = "Fibonacci Series: Invalid Input"
                tvNthTerm.text = "Nth Term: Invalid Input"
                return
            }

            if (n > 1000) {
                tvFibonacciResult.text = "Fibonacci Series: Limit is 100"
                tvNthTerm.text = "Nth Term: Too Large"
                return
            }

            val series = mutableListOf<Long>()

            var a = 0L
            var b = 1L

            for (i in 0 until n) {
                series.add(a)
                val next = a + b
                a = b
                b = next
            }

            tvFibonacciResult.text =
                "Fibonacci Series: ${series.joinToString(", ")}"

            tvNthTerm.text =
                "Nth Term: ${series.last()}"

        } catch (e: Exception) {
            tvFibonacciResult.text = "Fibonacci Series: Invalid Input"
            tvNthTerm.text = "Nth Term: Invalid Input"
        }
    }

    private fun resetOutputs() {
        tvFibonacciResult.text = "Fibonacci Series: -"
        tvNthTerm.text = "Nth Term: -"
    }
}