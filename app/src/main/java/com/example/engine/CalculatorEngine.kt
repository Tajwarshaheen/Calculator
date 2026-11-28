package com.example.engine

import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

enum class AngleMode {
    DEG,
    RAD
}

enum class CalcError {
    DIVIDE_BY_ZERO,
    INVALID_EXPRESSION,
    MATH_DOMAIN,
    OVERFLOW
}

sealed class EvalResult {
    data class Success(val value: Double, val rawString: String) : EvalResult()
    data class Error(val error: CalcError) : EvalResult()
}

private sealed class Token {
    data class NumberToken(val value: Double) : Token()
    data class OperatorToken(val symbol: Char) : Token()
    data class UnaryToken(val symbol: Char) : Token()
    data class PostfixToken(val symbol: Char) : Token()
    data class FunctionToken(val name: String) : Token()
    data object LeftParen : Token()
    data object RightParen : Token()
}

class CalculatorEngine {

    fun evaluate(
        expression: String,
        angleMode: AngleMode = AngleMode.DEG,
        autoCloseParentheses: Boolean = true
    ): EvalResult {
        val trimmed = expression.trim()
        if (trimmed.isEmpty()) {
            return EvalResult.Error(CalcError.INVALID_EXPRESSION)
        }

        val normalized = normalizeExpression(trimmed, autoCloseParentheses)
            ?: return EvalResult.Error(CalcError.INVALID_EXPRESSION)

        val tokens = tokenize(normalized)
            ?: return EvalResult.Error(CalcError.INVALID_EXPRESSION)

        if (tokens.isEmpty()) {
            return EvalResult.Error(CalcError.INVALID_EXPRESSION)
        }

        val withImplicitMul = insertImplicitMultiplication(tokens)
        val rpn = toRpn(withImplicitMul)
            ?: return EvalResult.Error(CalcError.INVALID_EXPRESSION)

        return evaluateRpn(rpn, angleMode)
    }

    private fun normalizeExpression(input: String, autoCloseParentheses: Boolean): String? {
        val sb = StringBuilder()
        var openCount = 0
        var i = 0
        val clean = input
            .replace(",", "")
            .replace(" ", "")
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")
            .replace("sin⁻¹", "asin")
            .replace("cos⁻¹", "acos")
            .replace("tan⁻¹", "atan")
            .replace("√", "sqrt")

        while (i < clean.length) {
            val ch = clean[i]
            if (ch == '(') openCount++
            else if (ch == ')') {
                openCount--
                if (openCount < 0) return null
            }
            sb.append(ch)
            i++
        }

        if (openCount > 0) {
            if (!autoCloseParentheses) return null
            repeat(openCount) { sb.append(')') }
        }
        return sb.toString()
    }

    private fun tokenize(expr: String): List<Token>? {
        val tokens = mutableListOf<Token>()
        var i = 0
        val n = expr.length

        while (i < n) {
            val ch = expr[i]
            when {
                ch.isDigit() || ch == '.' -> {
                    val start = i
                    var hasDot = ch == '.'
                    i++
                    while (i < n && (expr[i].isDigit() || expr[i] == '.')) {
                        if (expr[i] == '.') {
                            if (hasDot) return null
                            hasDot = true
                        }
                        i++
                    }
                    // Scientific notation using uppercase E (e.g., 1.23E-5 or 1.23E+12)
                    if (i < n && expr[i] == 'E') {
                        i++
                        if (i < n && (expr[i] == '+' || expr[i] == '-')) {
                            i++
                        }
                        val expStart = i
                        while (i < n && expr[i].isDigit()) {
                            i++
                        }
                        if (i == expStart) return null
                    }
                    val numStr = expr.substring(start, i)
                    if (numStr == ".") return null
                    val num = numStr.toDoubleOrNull() ?: return null
                    tokens.add(Token.NumberToken(num))
                }

                ch == 'π' -> {
                    tokens.add(Token.NumberToken(Math.PI))
                    i++
                }

                ch == 'e' -> {
                    tokens.add(Token.NumberToken(Math.E))
                    i++
                }

                ch == '(' -> {
                    tokens.add(Token.LeftParen)
                    i++
                }

                ch == ')' -> {
                    tokens.add(Token.RightParen)
                    i++
                }

                ch == '%' || ch == '!' -> {
                    tokens.add(Token.PostfixToken(ch))
                    i++
                }

                ch == '+' || ch == '-' -> {
                    val prev = tokens.lastOrNull()
                    val isUnary = prev == null ||
                        prev is Token.OperatorToken ||
                        prev is Token.UnaryToken ||
                        prev is Token.LeftParen ||
                        prev is Token.FunctionToken
                    if (isUnary) {
                        tokens.add(Token.UnaryToken(ch))
                    } else {
                        tokens.add(Token.OperatorToken(ch))
                    }
                    i++
                }

                ch == '*' || ch == '/' || ch == '^' -> {
                    tokens.add(Token.OperatorToken(ch))
                    i++
                }

                ch.isLetter() -> {
                    val start = i
                    while (i < n && expr[i].isLetter()) {
                        i++
                    }
                    val name = expr.substring(start, i).lowercase()
                    when (name) {
                        "sin", "cos", "tan", "asin", "acos", "atan", "sqrt", "log", "ln" -> {
                            tokens.add(Token.FunctionToken(name))
                        }
                        "pi" -> tokens.add(Token.NumberToken(Math.PI))
                        else -> return null
                    }
                }

                else -> return null
            }
        }
        return tokens
    }

    private fun insertImplicitMultiplication(tokens: List<Token>): List<Token> {
        if (tokens.size < 2) return tokens
        val result = ArrayList<Token>(tokens.size + 4)
        for (idx in tokens.indices) {
            val current = tokens[idx]
            if (idx > 0) {
                val prev = tokens[idx - 1]
                val prevEndsValue = prev is Token.NumberToken ||
                    prev is Token.RightParen ||
                    prev is Token.PostfixToken
                val currentStartsValue = current is Token.NumberToken ||
                    current is Token.LeftParen ||
                    current is Token.FunctionToken
                if (prevEndsValue && currentStartsValue) {
                    result.add(Token.OperatorToken('*'))
                }
            }
            result.add(current)
        }
        return result
    }

    private fun precedence(token: Token): Int = when (token) {
        is Token.OperatorToken -> when (token.symbol) {
            '+', '-' -> 1
            '*', '/' -> 2
            '^' -> 4
            else -> 0
        }
        is Token.UnaryToken -> 3
        is Token.PostfixToken -> 5
        is Token.FunctionToken -> 6
        else -> 0
    }

    private fun isRightAssociative(token: Token): Boolean = when (token) {
        is Token.OperatorToken -> token.symbol == '^'
        is Token.UnaryToken -> true
        else -> false
    }

    private fun toRpn(tokens: List<Token>): List<Token>? {
        val output = mutableListOf<Token>()
        val stack = ArrayDeque<Token>()

        for (token in tokens) {
            when (token) {
                is Token.NumberToken -> output.add(token)
                is Token.PostfixToken -> {
                    while (stack.isNotEmpty() && precedence(stack.last()) >= precedence(token)) {
                        output.add(stack.removeLast())
                    }
                    output.add(token)
                }
                is Token.FunctionToken -> stack.addLast(token)
                is Token.OperatorToken, is Token.UnaryToken -> {
                    while (stack.isNotEmpty()) {
                        val top = stack.last()
                        if (top is Token.LeftParen) break
                        val pTop = precedence(top)
                        val pCurr = precedence(token)
                        if (pTop > pCurr || (pTop == pCurr && !isRightAssociative(token))) {
                            output.add(stack.removeLast())
                        } else {
                            break
                        }
                    }
                    stack.addLast(token)
                }
                is Token.LeftParen -> stack.addLast(token)
                is Token.RightParen -> {
                    var foundLeft = false
                    while (stack.isNotEmpty()) {
                        val top = stack.removeLast()
                        if (top is Token.LeftParen) {
                            foundLeft = true
                            break
                        }
                        output.add(top)
                    }
                    if (!foundLeft) return null
                    if (stack.isNotEmpty() && stack.last() is Token.FunctionToken) {
                        output.add(stack.removeLast())
                    }
                }
            }
        }

        while (stack.isNotEmpty()) {
            val top = stack.removeLast()
            if (top is Token.LeftParen || top is Token.RightParen) return null
            output.add(top)
        }
        return output
    }

    private fun evaluateRpn(rpn: List<Token>, angleMode: AngleMode): EvalResult {
        val stack = ArrayDeque<Double>()

        for (token in rpn) {
            when (token) {
                is Token.NumberToken -> stack.addLast(token.value)

                is Token.UnaryToken -> {
                    if (stack.isEmpty()) return EvalResult.Error(CalcError.INVALID_EXPRESSION)
                    val v = stack.removeLast()
                    stack.addLast(if (token.symbol == '-') -v else v)
                }

                is Token.PostfixToken -> {
                    if (stack.isEmpty()) return EvalResult.Error(CalcError.INVALID_EXPRESSION)
                    val v = stack.removeLast()
                    when (token.symbol) {
                        '%' -> stack.addLast(v / 100.0)
                        '!' -> {
                            val fact = factorial(v) ?: return EvalResult.Error(CalcError.MATH_DOMAIN)
                            if (fact.isInfinite() || fact.isNaN()) {
                                return EvalResult.Error(CalcError.OVERFLOW)
                            }
                            stack.addLast(fact)
                        }
                    }
                }

                is Token.OperatorToken -> {
                    if (stack.size < 2) return EvalResult.Error(CalcError.INVALID_EXPRESSION)
                    val b = stack.removeLast()
                    val a = stack.removeLast()
                    val res = when (token.symbol) {
                        '+' -> a + b
                        '-' -> a - b
                        '*' -> a * b
                        '/' -> {
                            if (b == 0.0) return EvalResult.Error(CalcError.DIVIDE_BY_ZERO)
                            a / b
                        }
                        '^' -> {
                            if (a == 0.0 && b < 0.0) return EvalResult.Error(CalcError.DIVIDE_BY_ZERO)
                            if (a < 0.0 && b != floor(b)) return EvalResult.Error(CalcError.MATH_DOMAIN)
                            a.pow(b)
                        }
                        else -> return EvalResult.Error(CalcError.INVALID_EXPRESSION)
                    }
                    if (res.isNaN()) return EvalResult.Error(CalcError.MATH_DOMAIN)
                    if (res.isInfinite()) return EvalResult.Error(CalcError.OVERFLOW)
                    stack.addLast(res)
                }

                is Token.FunctionToken -> {
                    if (stack.isEmpty()) return EvalResult.Error(CalcError.INVALID_EXPRESSION)
                    val x = stack.removeLast()
                    val res = when (token.name) {
                        "sqrt" -> {
                            if (x < 0.0) return EvalResult.Error(CalcError.MATH_DOMAIN)
                            sqrt(x)
                        }
                        "log" -> {
                            if (x <= 0.0) return EvalResult.Error(CalcError.MATH_DOMAIN)
                            log10(x)
                        }
                        "ln" -> {
                            if (x <= 0.0) return EvalResult.Error(CalcError.MATH_DOMAIN)
                            ln(x)
                        }
                        "sin" -> {
                            val rad = if (angleMode == AngleMode.DEG) Math.toRadians(x) else x
                            sin(rad)
                        }
                        "cos" -> {
                            val rad = if (angleMode == AngleMode.DEG) Math.toRadians(x) else x
                            cos(rad)
                        }
                        "tan" -> {
                            val rad = if (angleMode == AngleMode.DEG) Math.toRadians(x) else x
                            if (abs(cos(rad)) < 1e-12) return EvalResult.Error(CalcError.MATH_DOMAIN)
                            tan(rad)
                        }
                        "asin" -> {
                            if (x < -1.0 || x > 1.0) return EvalResult.Error(CalcError.MATH_DOMAIN)
                            val rad = asin(x)
                            if (angleMode == AngleMode.DEG) Math.toDegrees(rad) else rad
                        }
                        "acos" -> {
                            if (x < -1.0 || x > 1.0) return EvalResult.Error(CalcError.MATH_DOMAIN)
                            val rad = acos(x)
                            if (angleMode == AngleMode.DEG) Math.toDegrees(rad) else rad
                        }
                        "atan" -> {
                            val rad = atan(x)
                            if (angleMode == AngleMode.DEG) Math.toDegrees(rad) else rad
                        }
                        else -> return EvalResult.Error(CalcError.INVALID_EXPRESSION)
                    }
                    if (res.isNaN()) return EvalResult.Error(CalcError.MATH_DOMAIN)
                    if (res.isInfinite()) return EvalResult.Error(CalcError.OVERFLOW)
                    stack.addLast(res)
                }

                Token.LeftParen, Token.RightParen -> return EvalResult.Error(CalcError.INVALID_EXPRESSION)
            }
        }

        if (stack.size != 1) return EvalResult.Error(CalcError.INVALID_EXPRESSION)

        val finalValue = cleanFloatingPoint(stack.last())
        if (finalValue.isNaN()) return EvalResult.Error(CalcError.MATH_DOMAIN)
        if (finalValue.isInfinite()) return EvalResult.Error(CalcError.OVERFLOW)

        val rawString = toRawString(finalValue)
        return EvalResult.Success(finalValue, rawString)
    }

    private fun factorial(x: Double): Double? {
        if (x < 0.0 || x != floor(x) || x > 170.0) return null
        val n = x.toInt()
        var result = 1.0
        for (k in 2..n) {
            result *= k
        }
        return result
    }

    private fun cleanFloatingPoint(value: Double): Double {
        if (value.isNaN() || value.isInfinite()) return value
        if (abs(value) < 1e-13) return 0.0
        return try {
            BigDecimal(value, MathContext(12)).toDouble()
        } catch (_: NumberFormatException) {
            value
        }
    }

    private fun toRawString(value: Double): String {
        if (value == 0.0) return "0"
        val absVal = abs(value)
        if (absVal >= 1e12 || (absVal > 0.0 && absVal < 1e-8)) {
            val formatted = String.format(java.util.Locale.US, "%.8E", value)
            return formatted
                .replace(Regex("(\\.\\d*?[1-9])0+E"), "$1E")
                .replace(Regex("\\.0+E"), "E")
                .replace("E+", "E")
        }
        val bd = BigDecimal.valueOf(value).stripTrailingZeros()
        return bd.toPlainString()
    }
}
