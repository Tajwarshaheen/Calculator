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

@Composable
fun BasicKeypad(
    hapticEnabled: Boolean,
    hasMemory: Boolean,
    onDigit: (String) -> Unit,
    onDecimal: () -> Unit,
    onOperator: (String) -> Unit,
    onPercentage: () -> Unit,
    onLeftParen: () -> Unit,
    onRightParen: () -> Unit,
    onNegate: () -> Unit,
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
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Memory & Brackets Utility Strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                label = "MC",
                onClick = onMemoryClear,
                role = CalcButtonRole.MEMORY,
                testTag = "btn_mc",
                contentDesc = "Memory Clear",
                hapticEnabled = hapticEnabled,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "MR",
                onClick = onMemoryRecall,
                role = CalcButtonRole.MEMORY,
                testTag = "btn_mr",
                contentDesc = "Memory Recall",
                hapticEnabled = hapticEnabled,
                isActive = hasMemory,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "M+",
                onClick = onMemoryAdd,
                role = CalcButtonRole.MEMORY,
                testTag = "btn_m_plus",
                contentDesc = "Memory Add",
                hapticEnabled = hapticEnabled,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "M-",
                onClick = onMemorySubtract,
                role = CalcButtonRole.MEMORY,
                testTag = "btn_m_minus",
                contentDesc = "Memory Subtract",
                hapticEnabled = hapticEnabled,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "(",
                onClick = onLeftParen,
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "btn_left_paren",
                contentDesc = "Open parenthesis",
                hapticEnabled = hapticEnabled,
                fontSize = 18.sp,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = ")",
                onClick = onRightParen,
                role = CalcButtonRole.SCIENTIFIC,
                testTag = "btn_right_paren",
                contentDesc = "Close parenthesis",
                hapticEnabled = hapticEnabled,
                fontSize = 18.sp,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            )
        }

        // Row 1: AC, DEL, %, ÷
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                label = "AC",
                onClick = onClearAll,
                role = CalcButtonRole.ACTION,
                testTag = "btn_ac",
                contentDesc = "Clear All",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "DEL",
                onClick = onDelete,
                role = CalcButtonRole.ACTION,
                testTag = "btn_del",
                contentDesc = "Delete last character",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "%",
                onClick = onPercentage,
                role = CalcButtonRole.ACTION,
                testTag = "btn_percent",
                contentDesc = "Percent",
                hapticEnabled = hapticEnabled,
                fontSize = 21.sp,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "÷",
                onClick = { onOperator("÷") },
                role = CalcButtonRole.OPERATOR,
                testTag = "btn_divide",
                contentDesc = "Divide",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f)
            )
        }

        // Row 2: 7, 8, 9, ×
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf("7", "8", "9").forEach { digit ->
                CalculatorButton(
                    label = digit,
                    onClick = { onDigit(digit) },
                    role = CalcButtonRole.DIGIT,
                    testTag = "btn_$digit",
                    hapticEnabled = hapticEnabled,
                    modifier = Modifier.weight(1f)
                )
            }
            CalculatorButton(
                label = "×",
                onClick = { onOperator("×") },
                role = CalcButtonRole.OPERATOR,
                testTag = "btn_multiply",
                contentDesc = "Multiply",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f)
            )
        }

        // Row 3: 4, 5, 6, −
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf("4", "5", "6").forEach { digit ->
                CalculatorButton(
                    label = digit,
                    onClick = { onDigit(digit) },
                    role = CalcButtonRole.DIGIT,
                    testTag = "btn_$digit",
                    hapticEnabled = hapticEnabled,
                    modifier = Modifier.weight(1f)
                )
            }
            CalculatorButton(
                label = "−",
                onClick = { onOperator("−") },
                role = CalcButtonRole.OPERATOR,
                testTag = "btn_subtract",
                contentDesc = "Subtract",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f)
            )
        }

        // Row 4: 1, 2, 3, +
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf("1", "2", "3").forEach { digit ->
                CalculatorButton(
                    label = digit,
                    onClick = { onDigit(digit) },
                    role = CalcButtonRole.DIGIT,
                    testTag = "btn_$digit",
                    hapticEnabled = hapticEnabled,
                    modifier = Modifier.weight(1f)
                )
            }
            CalculatorButton(
                label = "+",
                onClick = { onOperator("+") },
                role = CalcButtonRole.OPERATOR,
                testTag = "btn_add",
                contentDesc = "Add",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f)
            )
        }

        // Row 5: +/-, 0, ., =
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalculatorButton(
                label = "+/−",
                onClick = onNegate,
                role = CalcButtonRole.DIGIT,
                testTag = "btn_negate",
                contentDesc = "Toggle positive or negative",
                hapticEnabled = hapticEnabled,
                fontSize = 19.sp,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "0",
                onClick = { onDigit("0") },
                role = CalcButtonRole.DIGIT,
                testTag = "btn_0",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = ".",
                onClick = onDecimal,
                role = CalcButtonRole.DIGIT,
                testTag = "btn_decimal",
                contentDesc = "Decimal point",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                label = "=",
                onClick = onEquals,
                role = CalcButtonRole.EQUALS,
                testTag = "btn_equals",
                contentDesc = "Equals",
                hapticEnabled = hapticEnabled,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
