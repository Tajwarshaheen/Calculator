package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.AngleMode

@Composable
fun ScientificKeypad(
    hapticEnabled: Boolean,
    hasMemory: Boolean,
    angleMode: AngleMode,
    isInverseTrig: Boolean,
    onToggleAngleMode: () -> Unit,
    onToggleInverseTrig: () -> Unit,
    onDigit: (String) -> Unit,
    onDecimal: () -> Unit,
    onOperator: (String) -> Unit,
    onPercentage: () -> Unit,
    onLeftParen: () -> Unit,
    onRightParen: () -> Unit,
    onNegate: () -> Unit,
    onScientificFunction: (String) -> Unit,
    onConstant: (String) -> Unit,
    onPowerExponent: (String) -> Unit,
    onFactorial: () -> Unit,
    onDelete: () -> Unit,
    onClearAll: () -> Unit,
    onEquals: () -> Unit,
    onMemoryClear: () -> Unit,
    onMemoryRecall: () -> Unit,
    onMemoryAdd: () -> Unit,
    onMemorySubtract: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        // Mode & Memory Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalculatorButton(
                label = if (angleMode == AngleMode.DEG) "DEG" else "RAD",
                onClick = onToggleAngleMode,
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_angle",
                contentDesc = "Toggle Degrees or Radians",
                hapticEnabled = hapticEnabled,
                isActive = true,
                fontSize = 12.sp,
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier.weight(1.1f)
            )
            CalculatorButton(
                label = "INV",
                onClick = onToggleInverseTrig,
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_inv",
                contentDesc = "Toggle inverse trigonometric functions",
                hapticEnabled = hapticEnabled,
                isActive = isInverseTrig,
                fontSize = 12.sp,
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "MC",
                onClick = onMemoryClear,
                role = CalcButtonRole.MEMORY,
                testTag = "sci_btn_mc",
                contentDesc = "Memory Clear",
                hapticEnabled = hapticEnabled,
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "MR",
                onClick = onMemoryRecall,
                role = CalcButtonRole.MEMORY,
                testTag = "sci_btn_mr",
                contentDesc = "Memory Recall",
                hapticEnabled = hapticEnabled,
                isActive = hasMemory,
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "M+",
                onClick = onMemoryAdd,
                role = CalcButtonRole.MEMORY,
                testTag = "sci_btn_m_plus",
                contentDesc = "Memory Add",
                hapticEnabled = hapticEnabled,
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "M-",
                onClick = onMemorySubtract,
                role = CalcButtonRole.MEMORY,
                testTag = "sci_btn_m_minus",
                contentDesc = "Memory Subtract",
                hapticEnabled = hapticEnabled,
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier.weight(1f)
            )
        }

        // Scientific Row 1: x², x³, √, xʸ, !
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.88f),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            CalculatorButton(
                label = "x²",
                onClick = { onPowerExponent("2") },
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_square",
                contentDesc = "Square",
                hapticEnabled = hapticEnabled,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "x³",
                onClick = { onPowerExponent("3") },
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_cube",
                contentDesc = "Cube",
                hapticEnabled = hapticEnabled,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "√",
                onClick = { onScientificFunction("√(") },
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_sqrt",
                contentDesc = "Square root",
                hapticEnabled = hapticEnabled,
                fontSize = 18.sp,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "xʸ",
                onClick = { onOperator("^") },
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_power",
                contentDesc = "Power",
                hapticEnabled = hapticEnabled,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "x!",
                onClick = onFactorial,
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_factorial",
                contentDesc = "Factorial",
                hapticEnabled = hapticEnabled,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f)
            )
        }

        // Scientific Row 2: sin/sin⁻¹, cos/cos⁻¹, tan/tan⁻¹, log, ln
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.88f),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            val sinLabel = if (isInverseTrig) "sin⁻¹" else "sin"
            val cosLabel = if (isInverseTrig) "cos⁻¹" else "cos"
            val tanLabel = if (isInverseTrig) "tan⁻¹" else "tan"
            CalculatorButton(
                label = sinLabel,
                onClick = { onScientificFunction("$sinLabel(") },
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_sin",
                contentDesc = sinLabel,
                hapticEnabled = hapticEnabled,
                isActive = isInverseTrig,
                fontSize = if (isInverseTrig) 13.sp else 15.sp,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = cosLabel,
                onClick = { onScientificFunction("$cosLabel(") },
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_cos",
                contentDesc = cosLabel,
                hapticEnabled = hapticEnabled,
                isActive = isInverseTrig,
                fontSize = if (isInverseTrig) 13.sp else 15.sp,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = tanLabel,
                onClick = { onScientificFunction("$tanLabel(") },
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_tan",
                contentDesc = tanLabel,
                hapticEnabled = hapticEnabled,
                isActive = isInverseTrig,
                fontSize = if (isInverseTrig) 13.sp else 15.sp,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "log",
                onClick = { onScientificFunction("log(") },
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_log",
                contentDesc = "Base 10 logarithm",
                hapticEnabled = hapticEnabled,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "ln",
                onClick = { onScientificFunction("ln(") },
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_ln",
                contentDesc = "Natural logarithm",
                hapticEnabled = hapticEnabled,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f)
            )
        }

        // Scientific Row 3: (, ), π, e, %
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.88f),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            CalculatorButton(
                label = "(",
                onClick = onLeftParen,
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_left_paren",
                contentDesc = "Open parenthesis",
                hapticEnabled = hapticEnabled,
                fontSize = 17.sp,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = ")",
                onClick = onRightParen,
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_right_paren",
                contentDesc = "Close parenthesis",
                hapticEnabled = hapticEnabled,
                fontSize = 17.sp,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "π",
                onClick = { onConstant("π") },
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_pi",
                contentDesc = "Pi",
                hapticEnabled = hapticEnabled,
                fontSize = 17.sp,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "e",
                onClick = { onConstant("e") },
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_e",
                contentDesc = "Euler's number",
                hapticEnabled = hapticEnabled,
                fontSize = 17.sp,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "%",
                onClick = onPercentage,
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "sci_btn_percent",
                contentDesc = "Percent",
                hapticEnabled = hapticEnabled,
                fontSize = 17.sp,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f)
            )
        }

        // Basic Digits & Operators Grid
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                label = "AC",
                onClick = onClearAll,
                role = CalcButtonRole.ACTION,
                testTag = "sci_btn_ac",
                contentDesc = "Clear All",
                hapticEnabled = hapticEnabled,
                fontSize = 17.sp,
                shape = RoundedCornerShape(17.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "DEL",
                onClick = onDelete,
                role = CalcButtonRole.ACTION,
                testTag = "sci_btn_del",
                contentDesc = "Delete",
                hapticEnabled = hapticEnabled,
                fontSize = 17.sp,
                shape = RoundedCornerShape(17.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "+/−",
                onClick = onNegate,
                role = CalcButtonRole.ACTION,
                testTag = "sci_btn_negate",
                contentDesc = "Negate",
                hapticEnabled = hapticEnabled,
                fontSize = 17.sp,
                shape = RoundedCornerShape(17.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "÷",
                onClick = { onOperator("÷") },
                role = CalcButtonRole.OPERATOR,
                testTag = "sci_btn_divide",
                contentDesc = "Divide",
                hapticEnabled = hapticEnabled,
                fontSize = 23.sp,
                shape = RoundedCornerShape(17.dp),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("7", "8", "9").forEach { digit ->
                CalculatorButton(
                    label = digit,
                    onClick = { onDigit(digit) },
                    role = CalcButtonRole.DIGIT,
                    testTag = "sci_btn_$digit",
                    hapticEnabled = hapticEnabled,
                    fontSize = 22.sp,
                    shape = RoundedCornerShape(17.dp),
                    modifier = Modifier.weight(1f)
                )
            }
            CalculatorButton(
                label = "×",
                onClick = { onOperator("×") },
                role = CalcButtonRole.OPERATOR,
                testTag = "sci_btn_multiply",
                contentDesc = "Multiply",
                hapticEnabled = hapticEnabled,
                fontSize = 23.sp,
                shape = RoundedCornerShape(17.dp),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("4", "5", "6").forEach { digit ->
                CalculatorButton(
                    label = digit,
                    onClick = { onDigit(digit) },
                    role = CalcButtonRole.DIGIT,
                    testTag = "sci_btn_$digit",
                    hapticEnabled = hapticEnabled,
                    fontSize = 22.sp,
                    shape = RoundedCornerShape(17.dp),
                    modifier = Modifier.weight(1f)
                )
            }
            CalculatorButton(
                label = "−",
                onClick = { onOperator("−") },
                role = CalcButtonRole.OPERATOR,
                testTag = "sci_btn_subtract",
                contentDesc = "Subtract",
                hapticEnabled = hapticEnabled,
                fontSize = 23.sp,
                shape = RoundedCornerShape(17.dp),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("1", "2", "3").forEach { digit ->
                CalculatorButton(
                    label = digit,
                    onClick = { onDigit(digit) },
                    role = CalcButtonRole.DIGIT,
                    testTag = "sci_btn_$digit",
                    hapticEnabled = hapticEnabled,
                    fontSize = 22.sp,
                    shape = RoundedCornerShape(17.dp),
                    modifier = Modifier.weight(1f)
                )
            }
            CalculatorButton(
                label = "+",
                onClick = { onOperator("+") },
                role = CalcButtonRole.OPERATOR,
                testTag = "sci_btn_add",
                contentDesc = "Add",
                hapticEnabled = hapticEnabled,
                fontSize = 23.sp,
                shape = RoundedCornerShape(17.dp),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                label = "0",
                onClick = { onDigit("0") },
                role = CalcButtonRole.DIGIT,
                testTag = "sci_btn_0",
                hapticEnabled = hapticEnabled,
                fontSize = 22.sp,
                shape = RoundedCornerShape(17.dp),
                modifier = Modifier.weight(2f)
            )
            CalculatorButton(
                label = ".",
                onClick = onDecimal,
                role = CalcButtonRole.DIGIT,
                testTag = "sci_btn_decimal",
                contentDesc = "Decimal point",
                hapticEnabled = hapticEnabled,
                fontSize = 22.sp,
                shape = RoundedCornerShape(17.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "=",
                onClick = onEquals,
                role = CalcButtonRole.EQUALS,
                testTag = "sci_btn_equals",
                contentDesc = "Equals",
                hapticEnabled = hapticEnabled,
                fontSize = 25.sp,
                shape = RoundedCornerShape(17.dp),
                modifier = Modifier.weight(1f)
            )
        }
    }
}
