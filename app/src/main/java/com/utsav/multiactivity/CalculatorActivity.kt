package com.utsav.multiactivity

import android.os.Bundle
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Stack

class CalculatorActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView
    private lateinit var expressionScroll: HorizontalScrollView

    private var expression = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        findViewById<TextView>(R.id.btnBackCalculator).setOnClickListener {
            finish()
        }

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)
        expressionScroll = findViewById(R.id.expressionScroll)

        setupButtons()
    }

    private fun setupButtons() {
        val numberIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        numberIds.forEach {
            findViewById<Button>(it).setOnClickListener { btn ->
                appendValue((btn as Button).text.toString())
            }
        }

        findViewById<Button>(R.id.btnDot).setOnClickListener { appendDecimal() }
        findViewById<Button>(R.id.btnPlus).setOnClickListener { appendOperator("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { appendMinus() }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { appendOperator("*") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { appendOperator("/") }

        findViewById<Button>(R.id.btnParenthesis).setOnClickListener {
            appendSmartParenthesis()
        }

        findViewById<Button>(R.id.btnPercent).setOnClickListener {
            appendPercent()
        }

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            expression = ""
            updateDisplay()
        }

        findViewById<Button>(R.id.btnBackspace).setOnClickListener {
            if (expression.isNotEmpty()) {
                expression = expression.dropLast(1)
                updateDisplay()
            }
        }

        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            evaluateFinal()
        }
    }

    private fun appendValue(value: String) {
        expression += value
        updateDisplay()
    }

    private fun appendOperator(op: String) {
        if (expression.isEmpty()) return
        if (expression.last() in "+-*/.%(") return

        expression += op
        updateDisplay()
    }

    private fun appendMinus() {
        if (expression.isEmpty()) {
            expression = "-"
        } else {
            val last = expression.last()

            if (last in "+-*/(") {
                expression += "-"
            } else if (last !in ".%") {
                expression += "-"
            }
        }

        updateDisplay()
    }

    private fun appendDecimal() {
        val token = expression.takeLastWhile {
            it.isDigit() || it == '.' || it == '-'
        }

        if (token.contains(".")) return

        if (expression.isEmpty() || expression.last() in "+-*/(") {
            expression += "0."
        } else {
            expression += "."
        }

        updateDisplay()
    }

    private fun appendSmartParenthesis() {
        val open = expression.count { it == '(' }
        val close = expression.count { it == ')' }

        if (expression.isEmpty() || expression.last() in "+-*/(") {
            expression += "("
        } else if (open > close && (expression.last().isDigit() || expression.last() == '%')) {
            expression += ")"
        }

        updateDisplay()
    }

    private fun appendPercent() {
        if (expression.isNotEmpty() &&
            (expression.last().isDigit() || expression.last() == ')')) {
            expression += "%"
            updateDisplay()
        }
    }

    private fun updateDisplay() {
        tvExpression.text = expression

        expressionScroll.post {
            expressionScroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
        }

        if (expression.isEmpty()) {
            tvResult.text = "0"
            return
        }

        try {
            val processed = preprocessExpression(expression)
            val result = evaluate(processed)
            tvResult.text = formatResult(result)
        } catch (_: Exception) {
            tvResult.text = "..."
        }
    }

    private fun evaluateFinal() {
        try {
            val processed = preprocessExpression(expression)
            val result = evaluate(processed)
            val formatted = formatResult(result)

            expression = formatted
            tvExpression.text = formatted
            tvResult.text = formatted
        } catch (_: Exception) {
            tvResult.text = "Error"
        }
    }

    private fun preprocessExpression(expr: String): String {
        var processed = expr.replace("%", "/100")

        if (processed.startsWith("-")) {
            processed = "0$processed"
        }

        processed = processed.replace("(-", "(0-")

        return processed
    }

    private fun formatResult(value: Double): String {
        return if (value % 1 == 0.0) {
            value.toInt().toString()
        } else {
            value.toString()
        }
    }

    private fun evaluate(expr: String): Double {
        val values = Stack<Double>()
        val ops = Stack<Char>()
        var i = 0

        while (i < expr.length) {
            when {
                expr[i] == '(' -> {
                    ops.push(expr[i])
                    i++
                }

                expr[i].isDigit() || expr[i] == '.' -> {
                    val sb = StringBuilder()

                    while (i < expr.length &&
                        (expr[i].isDigit() || expr[i] == '.')) {
                        sb.append(expr[i++])
                    }

                    values.push(sb.toString().toDouble())
                }

                expr[i] == ')' -> {
                    while (ops.peek() != '(') {
                        values.push(applyOperation(ops.pop(), values.pop(), values.pop()))
                    }
                    ops.pop()
                    i++
                }

                expr[i] in "+-*/" -> {
                    while (ops.isNotEmpty() && hasPrecedence(expr[i], ops.peek())) {
                        values.push(applyOperation(ops.pop(), values.pop(), values.pop()))
                    }
                    ops.push(expr[i])
                    i++
                }

                else -> throw Exception()
            }
        }

        while (ops.isNotEmpty()) {
            values.push(applyOperation(ops.pop(), values.pop(), values.pop()))
        }

        return values.pop()
    }

    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        if (op2 == '(' || op2 == ')') return false
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false
        return true
    }

    private fun applyOperation(op: Char, b: Double, a: Double): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> {
                if (b == 0.0) throw ArithmeticException()
                a / b
            }
            else -> 0.0
        }
    }
}