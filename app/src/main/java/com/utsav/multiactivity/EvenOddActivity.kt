package com.utsav.multiactivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EvenOddActivity : AppCompatActivity() {

    private lateinit var etNumber: EditText
    private lateinit var tvStatus: TextView
    private lateinit var tvResult: TextView
    private lateinit var btnClear: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_even_odd)

        findViewById<TextView>(R.id.btnBackEvenOdd).setOnClickListener {
            finish()
        }

        etNumber = findViewById(R.id.etNumber)
        tvStatus = findViewById(R.id.tvStatus)
        tvResult = findViewById(R.id.tvResult)
        btnClear = findViewById(R.id.btnClearEvenOdd)

        etNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkEvenOdd(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnClear.setOnClickListener {
            etNumber.text.clear()
            tvStatus.text = "Waiting for Input"
            tvResult.text = "..."
        }
    }

    private fun checkEvenOdd(input: String) {
        if (input.isEmpty() || input == "-") {
            tvStatus.text = "Waiting for Input"
            tvResult.text = "..."
            return
        }

        try {
            val number = input.toInt()

            tvStatus.text = "Analysis Complete"

            if (number % 2 == 0) {
                tvResult.text = "EVEN"
                tvResult.setTextColor(getColor(R.color.successGreen))
            } else {
                tvResult.text = "ODD"
                tvResult.setTextColor(getColor(R.color.calculatorOperator))
            }

        } catch (e: Exception) {
            tvStatus.text = "Invalid Input"
            tvResult.text = "ERROR"
            tvResult.setTextColor(getColor(R.color.errorRed))
        }
    }
}