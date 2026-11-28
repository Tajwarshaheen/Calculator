package com.example

import com.example.engine.AngleMode
import com.example.engine.CalcError
import com.example.engine.CalculatorEngine
import com.example.engine.EvalResult
import com.example.engine.NumberFormatter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculatorEngineTest {

    private val engine = CalculatorEngine()

    private fun evalSuccess(expr: String, mode: AngleMode = AngleMode.DEG): Double {
        val res = engine.evaluate(expr, mode)
        assertTrue("Expected success for '$expr', got $res", res is EvalResult.Success)
        return (res as EvalResult.Success).value
    }

    private fun evalError(expr: String, expected: CalcError, mode: AngleMode = AngleMode.DEG) {
        val res = engine.evaluate(expr, mode)
        assertEquals(EvalResult.Error(expected), res)
    }

    @Test
    fun basicPrecedenceAndBrackets() {
        assertEquals(14.0, evalSuccess("2+3×4"), 1e-9)
        assertEquals(20.0, evalSuccess("(2+3)×4"), 1e-9)
        assertEquals(0.3, evalSuccess("0.1+0.2"), 1e-9)
        assertEquals(15.0, evalSuccess("50%×30"), 1e-9)
        assertEquals(-25.0, evalSuccess("−5^2"), 1e-9)
        assertEquals(25.0, evalSuccess("(−5)^2"), 1e-9)
    }

    @Test
    fun scientificFunctionsDegAndRad() {
        assertEquals(0.5, evalSuccess("sin(30)", AngleMode.DEG), 1e-9)
        assertEquals(0.0, evalSuccess("cos(90)", AngleMode.DEG), 1e-9)
        assertEquals(1.0, evalSuccess("tan(45)", AngleMode.DEG), 1e-9)
        assertEquals(30.0, evalSuccess("sin⁻¹(0.5)", AngleMode.DEG), 1e-9)
        assertEquals(1.0, evalSuccess("sin(π÷2)", AngleMode.RAD), 1e-9)
        assertEquals(2.0, evalSuccess("log(100)"), 1e-9)
        assertEquals(1.0, evalSuccess("ln(e)"), 1e-9)
        assertEquals(120.0, evalSuccess("5!"), 1e-9)
        assertEquals(1.0, evalSuccess("0!"), 1e-9)
        assertEquals(8.0, evalSuccess("2^3"), 1e-9)
        assertEquals(5.0, evalSuccess("√(25)"), 1e-9)
    }

    @Test
    fun implicitMultiplicationAndAutoClose() {
        assertEquals(2.0 * Math.PI, evalSuccess("2π"), 1e-9)
        assertEquals(14.0, evalSuccess("2(3+4)"), 1e-9)
        assertEquals(1.0, evalSuccess("2sin(30", AngleMode.DEG), 1e-9)
    }

    @Test
    fun errorHandlingWorksSafely() {
        evalError("10÷0", CalcError.DIVIDE_BY_ZERO)
        evalError("√(−4)", CalcError.MATH_DOMAIN)
        evalError("log(0)", CalcError.MATH_DOMAIN)
        evalError("ln(−2)", CalcError.MATH_DOMAIN)
        evalError("sin⁻¹(2)", CalcError.MATH_DOMAIN)
        evalError("tan(90)", CalcError.MATH_DOMAIN, AngleMode.DEG)
        evalError("(−3)!", CalcError.MATH_DOMAIN)
        evalError("5+", CalcError.INVALID_EXPRESSION)
    }

    @Test
    fun numberFormattingGroupsThousandsCorrectly() {
        assertEquals("1,234,567.89", NumberFormatter.formatResult("1234567.89", true))
        assertEquals("1234567.89", NumberFormatter.formatResult("1234567.89", false))
        assertEquals("1,234+5,678.005", NumberFormatter.formatExpression("1234+5678.005", true))
    }
}
