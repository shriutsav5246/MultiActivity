package com.utsav.multiactivity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

class CalculatorActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView

    private var expression = ""
    private var openBracketCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        findViewById<TextView>(R.id.btnBackCalculator).setOnClickListener {
            finish()
        }

        val buttonMap: Map<Int, String> = mapOf(
            R.id.btn0 to "0",
            R.id.btn1 to "1",
            R.id.btn2 to "2",
            R.id.btn3 to "3",
            R.id.btn4 to "4",
            R.id.btn5 to "5",
            R.id.btn6 to "6",
            R.id.btn7 to "7",
            R.id.btn8 to "8",
            R.id.btn9 to "9",
            R.id.btnDot to ".",
            R.id.btnPlus to "+",
            R.id.btnMinus to "-",
            R.id.btnMultiply to "*",
            R.id.btnDivide to "/",
            R.id.btnPercent to "%",
            R.id.btnParenthesis to "()"
        )

        for ((id, value) in buttonMap) {
            findViewById<TextView>(id).setOnClickListener {
                handleInput(value)
            }
        }

        findViewById<TextView>(R.id.btnClear).setOnClickListener {
            expression = ""
            openBracketCount = 0
            updateDisplay()
        }

        findViewById<TextView>(R.id.btnBackspace).setOnClickListener {
            if (expression.isNotEmpty()) {
                val last = expression.last()

                if (last == '(') openBracketCount--
                if (last == ')') openBracketCount++

                expression = expression.dropLast(1)
                updateDisplay()
            }
        }

        findViewById<TextView>(R.id.btnEquals).setOnClickListener {
            evaluateFinal()
        }

        updateDisplay()
    }

    private fun handleInput(value: String) {
        when (value) {
            "()" -> handleBracket()
            ".", "+", "-", "*", "/", "%" -> handleOperator(value)
            else -> {
                expression += value
                updateDisplay()
            }
        }
    }

    private fun handleBracket() {
        if (expression.isEmpty()) {
            expression += "("
            openBracketCount++
        } else {
            val last = expression.last()

            if (last.isDigit() || last == ')') {
                if (openBracketCount > 0) {
                    expression += ")"
                    openBracketCount--
                } else {
                    expression += "*("
                    openBracketCount++
                }
            } else {
                expression += "("
                openBracketCount++
            }
        }

        updateDisplay()
    }

    private fun handleOperator(operator: String) {
        if (expression.isEmpty()) {
            if (operator == "-") {
                expression += "-"
                updateDisplay()
            }
            return
        }

        val last = expression.last()

        if (operator == ".") {
            if (!canAddDecimal()) return
            expression += "."
            updateDisplay()
            return
        }

        if (last == '(') {
            if (operator == "-") {
                expression += "-"
                updateDisplay()
            }
            return
        }

        if (isOperator(last)) {
            if (operator == "-" && canUseUnaryMinus(last)) {
                expression += "-"
                updateDisplay()
            }
            return
        }

        expression += operator
        updateDisplay()
    }

    private fun canUseUnaryMinus(last: Char): Boolean {
        return last == '+' || last == '-' || last == '*' || last == '/' || last == '%'
    }

    private fun canAddDecimal(): Boolean {
        if (expression.isEmpty()) return true

        var i = expression.length - 1

        while (i >= 0) {
            val ch = expression[i]

            if (isOperator(ch) || ch == '(' || ch == ')') break
            if (ch == '.') return false

            i--
        }

        return true
    }

    private fun isOperator(ch: Char): Boolean {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%'
    }

    private fun isExpressionValid(): Boolean {
        if (expression.isEmpty()) return false
        if (openBracketCount != 0) return false

        val last = expression.last()

        if (isOperator(last) || last == '.') return false
        if (expression.contains("()")) return false

        for (i in 1 until expression.length) {
            val prev = expression[i - 1]
            val curr = expression[i]

            if (isOperator(prev) && isOperator(curr)) {
                if (!(curr == '-' && canUseUnaryMinus(prev))) {
                    return false
                }
            }
        }

        return true
    }

    private fun updateDisplay() {
        tvExpression.text = if (expression.isEmpty()) "0" else expression

        if (!isExpressionValid()) {
            tvResult.text = ""
            return
        }

        try {
            val exp = expression.replace("%", "/100")
            val result = ExpressionBuilder(exp).build().evaluate()

            if (result.isInfinite() || result.isNaN()) {
                tvResult.text = "Invalid"
            } else {
                tvResult.text =
                    if (result % 1 == 0.0)
                        result.toLong().toString()
                    else
                        result.toString()
            }

        } catch (e: Exception) {
            tvResult.text = ""
        }
    }

    private fun evaluateFinal() {
        if (!isExpressionValid()) {
            tvResult.text = "Invalid Expression"
            return
        }

        try {
            val exp = expression.replace("%", "/100")
            val result = ExpressionBuilder(exp).build().evaluate()

            if (result.isInfinite() || result.isNaN()) {
                tvResult.text = "Division by Zero"
                return
            }

            val finalResult =
                if (result % 1 == 0.0)
                    result.toLong().toString()
                else
                    result.toString()

            tvResult.text = finalResult
            expression = finalResult

        } catch (e: Exception) {
            tvResult.text = "Invalid Expression"
        }
    }
}