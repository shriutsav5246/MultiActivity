package com.utsav.multiactivity

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import kotlin.math.pow

class BmiActivity : AppCompatActivity() {

    private lateinit var rgGender: RadioGroup
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton

    private lateinit var etAge: EditText
    private lateinit var etWeight: EditText
    private lateinit var etHeightCm: EditText
    private lateinit var etFeet: EditText
    private lateinit var etInches: EditText

    private lateinit var spinnerHeightMode: Spinner
    private lateinit var spinnerActivity: Spinner
    private lateinit var spinnerGoal: Spinner

    private lateinit var tvBmi: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvHealthyRange: TextView
    private lateinit var tvBmr: TextView
    private lateinit var tvMaintenance: TextView
    private lateinit var tvGoalCalories: TextView
    private lateinit var tvProtein: TextView
    private lateinit var tvAdvice: TextView

    private val heightModes = arrayOf(
        "Centimeters (cm)",
        "Feet + Inches"
    )

    private val activityLevels = arrayOf(
        "Sedentary",
        "Lightly Active",
        "Moderately Active",
        "Very Active"
    )

    private val goals = arrayOf(
        "Maintain",
        "Bulk / Muscle Gain",
        "Lean / Fat Loss"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        bindViews()
        setupFilters()
        setupSpinners()
        setupListeners()
        resetOutputs()
    }

    private fun bindViews() {
        findViewById<TextView>(R.id.btnBackBmi).setOnClickListener {
            finish()
        }

        rgGender = findViewById(R.id.rgGender)
        rbMale = findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)

        etAge = findViewById(R.id.etAge)
        etWeight = findViewById(R.id.etWeight)
        etHeightCm = findViewById(R.id.etHeightCm)
        etFeet = findViewById(R.id.etFeet)
        etInches = findViewById(R.id.etInches)

        spinnerHeightMode = findViewById(R.id.spinnerHeightMode)
        spinnerActivity = findViewById(R.id.spinnerActivity)
        spinnerGoal = findViewById(R.id.spinnerGoal)

        tvBmi = findViewById(R.id.tvBmi)
        tvCategory = findViewById(R.id.tvCategory)
        tvHealthyRange = findViewById(R.id.tvHealthyRange)
        tvBmr = findViewById(R.id.tvBmr)
        tvMaintenance = findViewById(R.id.tvMaintenance)
        tvGoalCalories = findViewById(R.id.tvGoalCalories)
        tvProtein = findViewById(R.id.tvProtein)
        tvAdvice = findViewById(R.id.tvAdvice)

        findViewById<Button>(R.id.btnClearBmi).setOnClickListener {
            rgGender.clearCheck()
            etAge.setText("")
            etWeight.setText("")
            etHeightCm.setText("")
            etFeet.setText("")
            etInches.setText("")
            spinnerHeightMode.setSelection(0)
            spinnerActivity.setSelection(0)
            spinnerGoal.setSelection(0)
            switchHeightMode()
            resetOutputs()
        }
    }

    private fun setupFilters() {
        setupDecimalFilter(etWeight)
        setupDecimalFilter(etHeightCm)
        setupDecimalFilter(etInches)
    }

    private fun setupSpinners() {
        setupSpinner(spinnerHeightMode, heightModes)
        setupSpinner(spinnerActivity, activityLevels)
        setupSpinner(spinnerGoal, goals)
    }

    private fun setupSpinner(spinner: Spinner, items: Array<String>) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            items
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupListeners() {
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calculateFitness()
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
        }

        etAge.addTextChangedListener(watcher)
        etWeight.addTextChangedListener(watcher)
        etHeightCm.addTextChangedListener(watcher)
        etFeet.addTextChangedListener(watcher)
        etInches.addTextChangedListener(watcher)

        rgGender.setOnCheckedChangeListener { _, _ ->
            calculateFitness()
        }

        spinnerHeightMode.onItemSelectedListener = spinnerListener {
            switchHeightMode()
            calculateFitness()
        }

        spinnerActivity.onItemSelectedListener = spinnerListener {
            calculateFitness()
        }

        spinnerGoal.onItemSelectedListener = spinnerListener {
            calculateFitness()
        }
    }

    private fun spinnerListener(action: () -> Unit): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                action()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun switchHeightMode() {
        if (spinnerHeightMode.selectedItem.toString() == "Centimeters (cm)") {
            etHeightCm.visibility = View.VISIBLE
            etFeet.visibility = View.GONE
            etInches.visibility = View.GONE
        } else {
            etHeightCm.visibility = View.GONE
            etFeet.visibility = View.VISIBLE
            etInches.visibility = View.VISIBLE
        }
    }

    private fun setupDecimalFilter(editText: EditText) {
        editText.filters = arrayOf(
            InputFilter { source, start, end, dest, _, _ ->
                val input = source.subSequence(start, end).toString()
                val current = dest.toString()

                if (!input.matches(Regex("[0-9.]*"))) return@InputFilter ""

                if (input == "." && current.contains(".")) "" else source
            }
        )
    }

    private fun calculateFitness() {
        try {
            if (rgGender.checkedRadioButtonId == -1) {
                resetOutputs()
                return
            }

            val ageText = etAge.text.toString().trim()

            if (ageText.isEmpty()) {
                resetOutputs()
                return
            }

            val age = ageText.toInt()

            if (age !in 18..80) {
                showInvalid("Age must be 18–80")
                return
            }

            val heightCm = getHeightInCm()

            if (heightCm <= 0.0 || heightCm !in 50.0..300.0) {
                showInvalid("Valid height required")
                return
            }

            val heightM = heightCm / 100.0
            val lowerWeight = 18.5 * heightM.pow(2)
            val upperWeight = 24.9 * heightM.pow(2)

            tvHealthyRange.text =
                "Healthy Weight Range: ${format(lowerWeight)} kg - ${format(upperWeight)} kg"

            val weightText = etWeight.text.toString().trim()

            if (weightText.isEmpty()) {
                tvBmi.text = "BMI: Height-only mode"
                tvCategory.text = "Category: Weight required"
                tvBmr.text = "BMR: Weight required"
                tvMaintenance.text = "Maintenance Calories: Weight required"
                tvGoalCalories.text = "Goal Calories: Weight required"
                tvProtein.text = "Protein Target: Weight required"
                tvAdvice.text = "Advice: Enter weight for full analysis"
                return
            }

            val weight = weightText.toDouble()

            if (weight !in 10.0..500.0) {
                showInvalid("Weight must be 10–500 kg")
                return
            }

            val bmi = weight / heightM.pow(2)

            val category = when {
                bmi < 18.5 -> "Underweight"
                bmi < 25 -> "Normal"
                bmi < 30 -> "Overweight"
                else -> "Obese"
            }

            val isMale = rbMale.isChecked

            val bmr = if (isMale) {
                10 * weight + 6.25 * heightCm - 5 * age + 5
            } else {
                10 * weight + 6.25 * heightCm - 5 * age - 161
            }

            val multiplier = when (spinnerActivity.selectedItem.toString()) {
                "Sedentary" -> 1.2
                "Lightly Active" -> 1.375
                "Moderately Active" -> 1.55
                else -> 1.725
            }

            val maintenance = bmr * multiplier

            val goalCalories = when (spinnerGoal.selectedItem.toString()) {
                "Bulk / Muscle Gain" -> maintenance + 300
                "Lean / Fat Loss" -> maintenance - 400
                else -> maintenance
            }

            val proteinLow = weight * 1.6
            val proteinHigh = weight * 2.2

            val advice = when {
                bmi < 18.5 && spinnerGoal.selectedItem.toString() == "Bulk / Muscle Gain" ->
                    "Lean bulk recommended"

                bmi >= 30 && spinnerGoal.selectedItem.toString() == "Bulk / Muscle Gain" ->
                    "Fat loss recommended before bulking"

                bmi >= 25 && spinnerGoal.selectedItem.toString() == "Lean / Fat Loss" ->
                    "Structured calorie deficit recommended"

                bmi < 18.5 ->
                    "Increase calorie intake gradually"

                bmi < 25 ->
                    "Healthy body composition"

                bmi < 30 ->
                    "Moderate fat loss may help"

                else ->
                    "Weight management recommended"
            }

            tvBmi.text = "BMI: ${format(bmi)}"
            tvCategory.text = "Category: $category"
            tvBmr.text = "BMR: ${format(bmr)} kcal/day"
            tvMaintenance.text = "Maintenance Calories: ${format(maintenance)} kcal/day"
            tvGoalCalories.text = "Goal Calories: ${format(goalCalories)} kcal/day"
            tvProtein.text =
                "Protein Target: ${format(proteinLow)}g - ${format(proteinHigh)}g/day"
            tvAdvice.text = "Advice: $advice"

        } catch (e: Exception) {
            showInvalid("Enter valid values")
        }
    }

    private fun getHeightInCm(): Double {
        return if (spinnerHeightMode.selectedItem.toString() == "Centimeters (cm)") {
            val cmText = etHeightCm.text.toString().trim()
            if (cmText.isEmpty()) 0.0 else cmText.toDouble()
        } else {
            val feetText = etFeet.text.toString().trim()
            val inchesText = etInches.text.toString().trim()

            if (feetText.isEmpty()) return 0.0

            val feet = feetText.toDouble()
            val inches = if (inchesText.isEmpty()) 0.0 else inchesText.toDouble()

            (feet * 30.48) + (inches * 2.54)
        }
    }

    private fun format(value: Double): String {
        return String.format(Locale.US, "%.2f", value)
    }

    private fun showInvalid(message: String) {
        tvBmi.text = "BMI: Invalid"
        tvCategory.text = "Category: Invalid"
        tvHealthyRange.text = message
        tvBmr.text = "BMR: Invalid"
        tvMaintenance.text = "Maintenance Calories: Invalid"
        tvGoalCalories.text = "Goal Calories: Invalid"
        tvProtein.text = "Protein Target: Invalid"
        tvAdvice.text = "Advice: Invalid"
    }

    private fun resetOutputs() {
        tvBmi.text = "BMI: -"
        tvCategory.text = "Category: -"
        tvHealthyRange.text = "Healthy Weight Range: -"
        tvBmr.text = "BMR: -"
        tvMaintenance.text = "Maintenance Calories: -"
        tvGoalCalories.text = "Goal Calories: -"
        tvProtein.text = "Protein Target: -"
        tvAdvice.text = "Advice: -"
    }
}