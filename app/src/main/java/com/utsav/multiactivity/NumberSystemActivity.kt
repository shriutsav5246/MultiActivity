package com.utsav.multiactivity

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs
import kotlin.math.floor

class NumberSystemActivity : AppCompatActivity() {

    private lateinit var spinnerInputType: Spinner
    private lateinit var etInputNumber: EditText
    private lateinit var tvBinary: TextView
    private lateinit var tvOctal: TextView
    private lateinit var tvDecimal: TextView
    private lateinit var tvHex: TextView

    private val inputTypes = arrayOf(
        "Decimal",
        "Binary",
        "Octal",
        "Hexadecimal"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_system)

        findViewById<TextView>(R.id.btnBackNumberSystem).setOnClickListener {
            finish()
        }

        spinnerInputType = findViewById(R.id.spinnerInputType)
        etInputNumber = findViewById(R.id.etInputNumber)
        tvBinary = findViewById(R.id.tvBinary)
        tvOctal = findViewById(R.id.tvOctal)
        tvDecimal = findViewById(R.id.tvDecimal)
        tvHex = findViewById(R.id.tvHex)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            inputTypes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerInputType.adapter = adapter

        findViewById<Button>(R.id.btnClearNumberSystem).setOnClickListener {
            etInputNumber.setText("")
            resetOutputs()
        }

        etInputNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                convertNumber()
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

        spinnerInputType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    etInputNumber.setText("")
                    applyInputRestriction()
                    resetOutputs()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        applyInputRestriction()
        resetOutputs()
    }

    private fun applyInputRestriction() {
        when (spinnerInputType.selectedItem.toString()) {

            "Decimal" -> {
                etInputNumber.hint = "Enter Decimal Number"
                setFilteredInput("[0-9.]*")
            }

            "Binary" -> {
                etInputNumber.hint = "Enter Binary Number (0,1)"
                setFilteredInput("[01.]*")
            }

            "Octal" -> {
                etInputNumber.hint = "Enter Octal Number (0-7)"
                setFilteredInput("[0-7.]*")
            }

            "Hexadecimal" -> {
                etInputNumber.hint = "Enter Hex Number (0-9, A-F)"
                setFilteredInput("[0-9a-fA-F.]*")
            }
        }
    }

    private fun setFilteredInput(pattern: String) {
        etInputNumber.filters = arrayOf(
            InputFilter { source, start, end, dest, _, _ ->
                val input = source.subSequence(start, end).toString()
                val current = dest.toString()

                if (!input.matches(Regex(pattern))) return@InputFilter ""

                if (input == "." && current.contains(".")) {
                    ""
                } else {
                    source
                }
            }
        )
    }

    private fun convertNumber() {
        val input = etInputNumber.text.toString().trim()

        if (input.isEmpty()) {
            resetOutputs()
            return
        }

        try {
            val decimalValue = when (spinnerInputType.selectedItem.toString()) {
                "Decimal" -> input.toDouble()
                "Binary" -> parseFractionalBaseToDecimal(input, 2)
                "Octal" -> parseFractionalBaseToDecimal(input, 8)
                "Hexadecimal" -> parseFractionalBaseToDecimal(input, 16)
                else -> 0.0
            }

            tvBinary.text = "Binary: ${convertDecimalToBase(decimalValue, 2)}"
            tvOctal.text = "Octal: ${convertDecimalToBase(decimalValue, 8)}"
            tvDecimal.text = "Decimal: ${formatDecimal(decimalValue)}"
            tvHex.text = "Hexadecimal: ${convertDecimalToBase(decimalValue, 16)}"

        } catch (e: Exception) {
            tvBinary.text = "Binary: Invalid Input"
            tvOctal.text = "Octal: Invalid Input"
            tvDecimal.text = "Decimal: Invalid Input"
            tvHex.text = "Hexadecimal: Invalid Input"
        }
    }

    private fun parseFractionalBaseToDecimal(input: String, base: Int): Double {
        val parts = input.uppercase().split(".")

        val integerPart = if (parts[0].isEmpty()) 0.0
        else parts[0].toLong(base).toDouble()

        if (parts.size == 1) return integerPart

        var fractionalValue = 0.0
        val fraction = parts[1]

        for (i in fraction.indices) {
            val digit = digitValue(fraction[i])
            fractionalValue += digit / Math.pow(base.toDouble(), (i + 1).toDouble())
        }

        return integerPart + fractionalValue
    }

    private fun digitValue(ch: Char): Int {
        return when {
            ch in '0'..'9' -> ch - '0'
            ch in 'A'..'F' -> ch - 'A' + 10
            else -> throw IllegalArgumentException("Invalid digit")
        }
    }

    private fun convertDecimalToBase(number: Double, base: Int): String {
        val digits = "0123456789ABCDEF"

        val integerPart = floor(number).toLong()
        var fractionalPart = abs(number - integerPart)

        val integerConverted =
            if (integerPart == 0L) "0"
            else integerPart.toString(base).uppercase()

        if (fractionalPart == 0.0) return integerConverted

        val fractionBuilder = StringBuilder()

        repeat(8) {
            fractionalPart *= base
            val digit = fractionalPart.toInt()
            fractionBuilder.append(digits[digit])
            fractionalPart -= digit

            if (fractionalPart == 0.0) return@repeat
        }

        return "$integerConverted.${fractionBuilder}"
    }

    private fun formatDecimal(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toLong().toString()
        } else {
            value.toString()
        }
    }

    private fun resetOutputs() {
        tvBinary.text = "Binary: -"
        tvOctal.text = "Octal: -"
        tvDecimal.text = "Decimal: -"
        tvHex.text = "Hexadecimal: -"
    }
}