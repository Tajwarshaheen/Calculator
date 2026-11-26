package com.example.engine

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs

object NumberFormatter {

    private val symbols = DecimalFormatSymbols(Locale.US)

    fun formatResult(raw: String, useThousandsSeparator: Boolean): String {
        if (raw.isEmpty()) return "0"
        val value = raw.toDoubleOrNull() ?: return raw
        if (value.isNaN() || value.isInfinite()) return raw

        val absVal = abs(value)
        if (absVal >= 1e12 || (absVal > 0.0 && absVal < 1e-8)) {
            return raw
        }

        if (!useThousandsSeparator) {
            return if (raw.contains('.')) {
                raw.trimEnd('0').trimEnd('.').ifEmpty { "0" }
            } else {
                raw
            }
        }

        val parts = raw.split('.')
        val integerPart = parts[0]
        val negative = integerPart.startsWith("-")
        val digits = if (negative) integerPart.substring(1) else integerPart
        val groupedInt = groupDigits(digits)
        val sign = if (negative) "-" else ""

        return if (parts.size > 1) {
            val frac = parts[1].trimEnd('0')
            if (frac.isEmpty()) "$sign$groupedInt" else "$sign$groupedInt.$frac"
        } else {
            "$sign$groupedInt"
        }
    }

    fun formatDouble(value: Double, useThousandsSeparator: Boolean): String {
        if (value == 0.0) return "0"
        val absVal = abs(value)
        if (absVal >= 1e12 || (absVal > 0.0 && absVal < 1e-8)) {
            val formatted = String.format(Locale.US, "%.6E", value)
            return formatted
                .replace(Regex("(\\.\\d*?[1-9])0+E"), "$1E")
                .replace(Regex("\\.0+E"), "E")
                .replace("E+", "E")
        }
        val df = DecimalFormat(if (useThousandsSeparator) "#,##0.##########" else "0.##########", symbols)
        return df.format(value)
    }

    fun formatExpression(expression: String, useThousandsSeparator: Boolean): String {
        if (expression.isEmpty() || !useThousandsSeparator) return expression

        val sb = StringBuilder(expression.length + 8)
        var i = 0
        val n = expression.length
        while (i < n) {
            val ch = expression[i]
            if (ch.isDigit()) {
                val start = i
                while (i < n && expression[i].isDigit()) {
                    i++
                }
                val intChunk = expression.substring(start, i)
                // Only add commas if not immediately after a decimal point or scientific E
                val prevChar = if (start > 0) expression[start - 1] else null
                val isFractionOrExp = prevChar == '.' || prevChar == 'E' ||
                    (start >= 2 && expression[start - 2] == 'E' && (prevChar == '+' || prevChar == '-'))
                if (isFractionOrExp) {
                    sb.append(intChunk)
                } else {
                    sb.append(groupDigits(intChunk))
                }
            } else {
                sb.append(ch)
                i++
            }
        }
        return sb.toString()
    }

    private fun groupDigits(digits: String): String {
        if (digits.length <= 3) return digits
        val sb = StringBuilder(digits.length + digits.length / 3)
        var count = 0
        for (i in digits.length - 1 downTo 0) {
            sb.append(digits[i])
            count++
            if (count == 3 && i != 0) {
                sb.append(',')
                count = 0
            }
        }
        return sb.reverse().toString()
    }
}
